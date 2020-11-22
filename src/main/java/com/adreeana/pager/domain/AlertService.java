package com.adreeana.pager.domain;

public class AlertService {
  private final Alerts alerts;
  private final SmsService smsService;
  private final EmailService emailService;

  public AlertService(Alerts alerts,
                      SmsService smsService,
                      EmailService emailService) {
    this.alerts = alerts;
    this.smsService = smsService;
    this.emailService = emailService;
  }

  public void notifyLevel(Alert alert) {
    alert.getLevel().getTargets().forEach(target -> {
      if (alerts.targetNotified(target)) return;

      if (target instanceof SmsTarget) {
        smsService.notify(target.contact(), alert.getMessage());
      } else if (target instanceof EmailTarget) {
        emailService.notify(target.contact(), alert.getMessage());
      } else {
        throw new IllegalArgumentException("Unknown Target type");
      }
      alerts.targetNotification(target);
    });
  }
}
