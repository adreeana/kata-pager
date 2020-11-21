package com.adreeana.pager.domain;

public class EscalationPolicy {
  private final MonitoredService monitoredService;
  private final Levels levels;

  public EscalationPolicy(MonitoredService monitoredService, Levels levels) {
    this.monitoredService = monitoredService;
    this.levels = levels;
  }

  public MonitoredService getMonitoredService() {
    return monitoredService;
  }

  public Levels getLevels() {
    return levels;
  }
}
