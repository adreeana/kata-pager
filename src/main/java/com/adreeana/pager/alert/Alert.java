package com.adreeana.pager.alert;

import com.adreeana.living_documentation.Entity;

@Entity
public class Alert {
  private String id;
  private MonitoredService monitoredService;
  private String message;
  private Boolean acknowledged;
  private Level level;

  public Alert(MonitoredService monitoredService, String message) {
    this.monitoredService = monitoredService;
    this.message = message;
    this.acknowledged = false;
  }

  public MonitoredService getMonitoredService() {
    return monitoredService;
  }

  public String getMessage() {
    return message;
  }

  public void acknowledge() {
    acknowledged = true;
  }

  public Boolean isAcknowledged() {
    return acknowledged;
  }

  public Level getLevel() {
    return level;
  }

  public void setLevel(Level level) {
    this.level = level;
  }

  public void acknowledgeLevel() {
    if (level == null) return;

    level.acknowledge();
  }

  public Boolean isLevelAcknowledged() {
    if (level == null) return false;

    return level.isAcknowledged();
  }
}
