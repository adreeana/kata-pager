package com.adreeana.pager.domain;

public class PagerService {
  private final EscalationPolicyService escalationPolicyService;
  private final SmsService smsService;
  private final EmailService emailService;
  private final TimerService timerService;

  public PagerService(
    EscalationPolicyService escalationPolicyService,
    SmsService smsService,
    EmailService emailService,
    TimerService timerService
  ) {
    this.escalationPolicyService = escalationPolicyService;
    this.smsService = smsService;
    this.emailService = emailService;
    this.timerService = timerService;
  }

  public void receiveAlert(Alert alert) {
    alert.getMonitoredService().unhealthy();

    EscalationPolicy escalationPolicy = escalationPolicyService.findEscalationPolicy(alert.getMonitoredService());

    Levels levels = escalationPolicy.getLevels();

    Level firstLevel = levels.first();
    notifyLevel(alert, firstLevel);

    timerService.startAcknowledgementTimer(alert.getMonitoredService(), firstLevel);
  }

  private void notifyLevel(Alert alert, Level firstLevel) {
    firstLevel.getTargets().forEach(t -> {
      if (t instanceof SmsTarget) {
        smsService.notify(t.contact(), alert.getMessage());
      } else if (t instanceof EmailTarget) {
        emailService.notify(t.contact(), alert.getMessage());
      }
    });
  }
}
