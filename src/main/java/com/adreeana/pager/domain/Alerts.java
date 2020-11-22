package com.adreeana.pager.domain;

public interface Alerts {
  void save(Alert alert);

  Alert fetchNotAcknowledged(MonitoredService monitoredService);
}
