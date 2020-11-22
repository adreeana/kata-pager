package com.adreeana.pager.domain;

public class AlertService {
  private final SmsService smsService;
  private final EmailService emailService;

  public AlertService(SmsService smsService, EmailService emailService) {
    this.smsService = smsService;
    this.emailService = emailService;
  }

  public void notifyLevel(Alert alert) {
    alert.getLevel().getTargets().forEach(t -> {
      if (t instanceof SmsTarget) {
        smsService.notify(t.contact(), alert.getMessage());
      } else if (t instanceof EmailTarget) {
        emailService.notify(t.contact(), alert.getMessage());
      }
    });
  }
}
