package com.adreeana.pager.alert;

import com.adreeana.living_documentation.ValueObject;

@ValueObject
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
