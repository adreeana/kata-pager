package com.adreeana.pager.alert;

import com.adreeana.living_documentation.ExternalActor;

import static com.adreeana.living_documentation.ExternalActor.ActorType.SYSTEM;
import static com.adreeana.living_documentation.ExternalActor.Direction.SPI;

@ExternalActor(name = "SMS Infrastructure Service", direction = SPI, type = SYSTEM)
public interface SmsService {
  void notify(String phoneNumber, String message);
}
