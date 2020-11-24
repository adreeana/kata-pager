package com.adreeana.alert;

import com.adreeana.living_documentation.DomainService;

import java.util.Optional;

@DomainService
public class AlertService {
  private final Alerts alerts;
  private final NotificationsService notificationsService;
  private final EscalationPolicyService escalationPolicyService;
  private final TimerService timerService;

  public AlertService(Alerts alerts,
                      NotificationsService notificationsService,
                      EscalationPolicyService escalationPolicyService,
                      TimerService timerService) {
    this.alerts = alerts;
    this.notificationsService = notificationsService;
    this.escalationPolicyService = escalationPolicyService;
    this.timerService = timerService;
  }

  public void receiveAlert(Alert alert) {
    if (alerts.isAlertReceived(alert.getMonitoredService())) return;

    EscalationPolicy escalationPolicy = escalationPolicyService.findEscalationPolicy(alert.getMonitoredService());

    alert.getMonitoredService().unhealthy();
    assignLevel(alert, escalationPolicy.getLevels().first());
  }

  public void receiveAcknowledgementTimeout(Alert alert) {
    if (alert.isAcknowledged()
        || alert.isLevelAcknowledged()
        || alert.getMonitoredService().isHealthy()) return;

    EscalationPolicy escalationPolicy = escalationPolicyService.findEscalationPolicy(alert.getMonitoredService());

    Optional<Level> nextLevel = escalationPolicy.getLevels().next(alert.getLevel());
    nextLevel.ifPresent(level -> assignLevel(alert, level));
  }

  public void receiveLevelAcknowledgement(Alert alert) {
    alert.acknowledgeLevel();
    alerts.save(alert);
  }

  private void assignLevel(Alert alert, Level level) {
    alert.setLevel(level);
    alerts.save(alert);

    notificationsService.notifyLevel(alert);

    timerService.startTimer(alert);
  }
}