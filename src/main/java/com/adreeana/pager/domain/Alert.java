package com.adreeana.pager.domain;

public class Alert {
  private MonitoredService monitoredService;
  private String message;

  public Alert(MonitoredService monitoredService, String message) {
    this.monitoredService = monitoredService;
    this.message = message;
  }

  public MonitoredService getMonitoredService() {
    return monitoredService;
  }

  public String getMessage() {
    return message;
  }
}
