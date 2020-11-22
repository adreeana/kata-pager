package com.adreeana.pager.domain;

import java.util.ArrayList;
import java.util.List;

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

  public Boolean isAcknowledged() {
    return acknowledged;
  }

  public Level getLevel() {
    return level;
  }

  public void setLevel(Level level) {
    this.level = level;
  }
}
