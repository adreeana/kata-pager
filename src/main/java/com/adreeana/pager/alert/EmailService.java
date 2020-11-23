package com.adreeana.pager.alert;

import com.adreeana.living_documentation.ExternalActor;

import static com.adreeana.living_documentation.ExternalActor.ActorType.SYSTEM;
import static com.adreeana.living_documentation.ExternalActor.Direction.SPI;

@ExternalActor(name = "Email Infrastructure Service", direction = SPI, type = SYSTEM)
public interface EmailService {
  void notify(String email, String message);
}
