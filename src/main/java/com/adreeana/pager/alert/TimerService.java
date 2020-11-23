package com.adreeana.pager.alert;

import com.adreeana.living_documentation.ExternalActor;

import static com.adreeana.living_documentation.ExternalActor.ActorType.SYSTEM;
import static com.adreeana.living_documentation.ExternalActor.Direction.API_SPI;

@ExternalActor(name = "Acknowledgement timer Infrastructure Service", direction = API_SPI, type = SYSTEM)
public interface TimerService {
  void startTimer(Alert alert);
}