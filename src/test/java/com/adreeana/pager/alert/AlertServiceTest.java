package com.adreeana.pager.alert;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.adreeana.pager.alert.LevelsFixture.emailFirstTarget;
import static com.adreeana.pager.alert.LevelsFixture.oneLevels;
import static com.adreeana.pager.alert.LevelsFixture.smsFirstTarget;
import static com.adreeana.pager.alert.MonitoredServiceFixture.monitoredService;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class AlertServiceTest {

  private SmsService smsService = mock(SmsService.class);
  private EmailService emailService = mock(EmailService.class);
  private Alerts alerts = mock(Alerts.class);

  private AlertService alertService;

  @BeforeEach
  void setUp() {
    alertService = new AlertService(alerts, smsService, emailService);
  }

  @Nested
  class NotifyLevel {
    @Nested
    class HappyPath {
      private Alert alert;

      @BeforeEach
      void setUp() {
         alert = new Alert(monitoredService, "Oups!");
      }

      @Test
      void notifyLevel() {
        Levels levels = oneLevels();
        alert.setLevel(levels.first());

        alertService.notifyLevel(alert);

        verify(smsService).notify(smsFirstTarget.contact(), alert.getMessage());
        verify(emailService).notify(emailFirstTarget.contact(), alert.getMessage());
      }

      @Nested
      class AvoidDuplicateNotificationsForTheSameTarget {
        @Test
        void notifyLevel() {
          Level level = new Level();
          level.addTarget(smsFirstTarget);
          level.addTarget(emailFirstTarget);
          alert.setLevel(level);

          when(alerts.targetNotified(smsFirstTarget)).thenReturn(true);
          when(alerts.targetNotified(emailFirstTarget)).thenReturn(false);

          alertService.notifyLevel(alert);

          verify(alerts).targetNotified(smsFirstTarget);
          verify(alerts).targetNotified(emailFirstTarget);

          verifyNoInteractions(smsService);
          verify(emailService).notify(emailFirstTarget.contact(), alert.getMessage());

          verify(alerts).targetNotification(emailFirstTarget);
          verifyNoMoreInteractions(alerts);
        }
      }
    }
  }
}