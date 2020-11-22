package com.adreeana.pager.domain;

import java.util.Optional;

public class PagerService {
  private final Alerts alerts;
  private final AlertService alertService;
  private final EscalationPolicyService escalationPolicyService;
  private final TimerService timerService;

  public PagerService(Alerts alerts,
                      AlertService alertService,
                      EscalationPolicyService escalationPolicyService,
                      TimerService timerService) {
    this.alerts = alerts;
    this.alertService = alertService;
    this.escalationPolicyService = escalationPolicyService;
    this.timerService = timerService;
  }

  public void receiveAlert(Alert alert) {
    alert.getMonitoredService().unhealthy();

    EscalationPolicy escalationPolicy = escalationPolicyService.findEscalationPolicy(alert.getMonitoredService());

    alert.setLevel(escalationPolicy.getLevels().first());
    alerts.save(alert);

    alertService.notifyLevel(alert);

    timerService.startTimer(alert);
  }

  public void receiveAcknowledgementTimeout(Alert alert) {
    if (alert.isAcknowledged()) return;

    if (alert.isLevelAcknowledged()) return;

    if (alert.getMonitoredService().isHealthy()) return;

    EscalationPolicy escalationPolicy = escalationPolicyService.findEscalationPolicy(alert.getMonitoredService());

    Level lastNotifiedLevel = alert.getLevel();

    Optional<Level> nextLevel = escalationPolicy.getLevels().next(lastNotifiedLevel);
    if (nextLevel.isPresent()) {
      alert.setLevel(nextLevel.get());
      alerts.save(alert);

      alertService.notifyLevel(alert);

      timerService.startTimer(alert);
    }
  }

  public void receiveLevelAcknowledgement(Alert alert) {
    alert.acknowledgeLevel();
    alerts.save(alert);
  }
}