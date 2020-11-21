package com.adreeana.pager.domain;

public class MonitoredService {
  private String id;
  private Boolean healthy;

  public MonitoredService(String id) {
    this.id = id;
    this.healthy = true;
  }
  public boolean isHealthy() {
    return this.healthy;
  }

  public void unhealthy() {
    healthy = false;
  }
}
