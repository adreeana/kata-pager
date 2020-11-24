package com.adreeana.alert;

import com.adreeana.living_documentation.Entity;

@Entity
public class Alert {
  private String id;
  private MonitoredService monitoredService;
  private String message;
  private Boolean acknowledged;
  private Level level;
  private Boolean levelAcknowledged;

  public Alert(MonitoredService monitoredService, String message) {
    this.monitoredService = monitoredService;
    this.message = message;
    this.acknowledged = false;
    this.levelAcknowledged = false;
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
    this.levelAcknowledged = false;
  }

  public void acknowledgeLevel() {
    if (level == null) return;

    levelAcknowledged = true;
  }

  public Boolean isLevelAcknowledged() {
    return levelAcknowledged;
  }
}
