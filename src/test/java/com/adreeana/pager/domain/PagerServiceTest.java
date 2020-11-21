package com.adreeana.pager.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class PagerServiceTest {
  private PagerService pagerService;

  private EscalationPolicyService escalationPolicyService = mock(EscalationPolicyService.class);
  private SmsService smsService = mock(SmsService.class);
  private EmailService emailService = mock(EmailService.class);
  private TimerService timerService = mock(TimerService.class);

  @BeforeEach
  void setUp() {
    pagerService = new PagerService(escalationPolicyService, smsService, emailService, timerService);
  }

  @Nested
  class ReceiveAlert {
    @Nested
    class HappyPath {
      @Test
      void receiveAlert() {
        MonitoredService monitoredService = new MonitoredService("1");

        Levels levels = new Levels();

        Level firstLevel = new Level();
        Target smsFirstTarget = new SmsTarget("0601");
        firstLevel.addTarget(smsFirstTarget);
        Target emailFirstTarget = new EmailTarget("email01");
        firstLevel.addTarget(emailFirstTarget);
        levels.addLevel(firstLevel);

        Level secondLevel = new Level();
        Target smsSecondTarget = new SmsTarget("0602");
        secondLevel.addTarget(smsSecondTarget);
        Target emailSecondTarget = new EmailTarget("email02");
        secondLevel.addTarget(emailSecondTarget);
        levels.addLevel(secondLevel);

        EscalationPolicy escalationPolicy = new EscalationPolicy(monitoredService, levels);

        when(escalationPolicyService.findEscalationPolicy(monitoredService)).thenReturn(escalationPolicy);

        Alert alert = new Alert(monitoredService, "Oups!");

        pagerService.receiveAlert(alert);

        assertFalse(monitoredService.isHealthy());

        verify(smsService).notify(smsFirstTarget.contact(), alert.getMessage());
        verify(emailService).notify(emailFirstTarget.contact(), alert.getMessage());

        verify(timerService).startAcknowledgementTimer(monitoredService, firstLevel);
      }
    }
  }
}