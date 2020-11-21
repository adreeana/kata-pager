package com.adreeana.pager.domain;

public interface TimerService {
  void startAcknowledgementTimer(MonitoredService service, Level level);
}