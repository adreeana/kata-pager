package com.adreeana.pager.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.adreeana.pager.domain.EscalationPolicyFixture.emailFirstTarget;
import static com.adreeana.pager.domain.EscalationPolicyFixture.monitoredService;
import static com.adreeana.pager.domain.EscalationPolicyFixture.smsFirstTarget;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AlertServiceTest {

  private SmsService smsService = mock(SmsService.class);
  private EmailService emailService = mock(EmailService.class);

  private AlertService alertService;

  @BeforeEach
  void setUp() {
    alertService = new AlertService(smsService, emailService);
  }

  @Nested
  class NotifyLevel {
    @Nested
    class HappyPath {

      @Test
      void notifyLevel() {
        Alert alert = new Alert(monitoredService, "Oups!");
        alert.setLevel(EscalationPolicyFixture.twoLevels(monitoredService).getLevels().first());

        alertService.notifyLevel(alert);

        verify(smsService).notify(smsFirstTarget.contact(), alert.getMessage());
        verify(emailService).notify(emailFirstTarget.contact(), alert.getMessage());
      }
    }
  }
}