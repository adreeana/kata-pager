package com.adreeana.alert;

import com.adreeana.living_documentation.Repository;

@Repository
public interface Alerts {
  void save(Alert alert);

  boolean isAlertReceived(MonitoredService monitoredService);

  void targetNotified(Target target);

  boolean isTargetNotified(Target target);
}