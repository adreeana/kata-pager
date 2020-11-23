package com.adreeana.pager.alert;

import com.adreeana.living_documentation.DomainService;

import java.util.Optional;

@DomainService
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
    if (alerts.alertReceived(alert.getMonitoredService())) return;

    EscalationPolicy escalationPolicy = escalationPolicyService.findEscalationPolicy(alert.getMonitoredService());

    alert.getMonitoredService().unhealthy();
    alert.setLevel(escalationPolicy.getLevels().first());
    alerts.save(alert);

    alertService.notifyLevel(alert);

    timerService.startTimer(alert);
  }

  public void receiveAcknowledgementTimeout(Alert alert) {
    if (alert.isAcknowledged()
        || alert.isLevelAcknowledged()
        || alert.getMonitoredService().isHealthy()) return;

    EscalationPolicy escalationPolicy = escalationPolicyService.findEscalationPolicy(alert.getMonitoredService());

    Optional<Level> nextLevel = escalationPolicy.getLevels().next(alert.getLevel());
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