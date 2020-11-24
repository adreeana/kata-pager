package com.adreeana.alert;

import com.adreeana.living_documentation.DomainService;

@DomainService
public class NotificationsService {
  private final Alerts alerts;
  private final SmsService smsService;
  private final EmailService emailService;

  public NotificationsService(Alerts alerts,
                              SmsService smsService,
                              EmailService emailService) {
    this.alerts = alerts;
    this.smsService = smsService;
    this.emailService = emailService;
  }

  public void notifyLevel(Alert alert) {
    alert.getLevel().getTargets().forEach(target -> {
      if (alerts.isTargetNotified(target)) return;

      if (target instanceof SmsTarget) {
        smsService.notify(target.contact(), alert.getMessage());
      } else if (target instanceof EmailTarget) {
        emailService.notify(target.contact(), alert.getMessage());
      } else {
        throw new IllegalArgumentException("Unknown Target type");
      }
      alerts.targetNotified(target);
    });
  }
}
