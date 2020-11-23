package com.adreeana.pager.alert;

import com.adreeana.living_documentation.Repository;

@Repository
public interface Alerts {
  void save(Alert alert);

  boolean alertReceived(MonitoredService monitoredService);

  void targetNotification(Target target);

  boolean targetNotified(Target target);
}