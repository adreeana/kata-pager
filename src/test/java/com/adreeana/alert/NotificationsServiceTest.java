package com.adreeana.alert;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.adreeana.alert.fixtures.LevelsFixture.emailFirstTarget;
import static com.adreeana.alert.fixtures.LevelsFixture.oneLevels;
import static com.adreeana.alert.fixtures.LevelsFixture.smsFirstTarget;
import static com.adreeana.alert.fixtures.MonitoredServiceFixture.monitoredService;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class NotificationsServiceTest {

  private SmsService smsService = mock(SmsService.class);
  private EmailService emailService = mock(EmailService.class);
  private Alerts alerts = mock(Alerts.class);

  private NotificationsService notificationsService;

  @BeforeEach
  void setUp() {
    notificationsService = new NotificationsService(alerts, smsService, emailService);
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

        notificationsService.notifyLevel(alert);

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

          when(alerts.isTargetNotified(smsFirstTarget)).thenReturn(true);
          when(alerts.isTargetNotified(emailFirstTarget)).thenReturn(false);

          notificationsService.notifyLevel(alert);

          verify(alerts).isTargetNotified(smsFirstTarget);
          verify(alerts).isTargetNotified(emailFirstTarget);

          verifyNoInteractions(smsService);
          verify(emailService).notify(emailFirstTarget.contact(), alert.getMessage());

          verify(alerts).targetNotified(emailFirstTarget);
          verifyNoMoreInteractions(alerts);
        }
      }
    }
  }
}