package com.adreeana.pager.domain;

public interface Alerts {
  void save(Alert alert);

  boolean alertReceived(MonitoredService monitoredService);

  void targetNotification(Target target);

  boolean targetNotified(Target target);
}