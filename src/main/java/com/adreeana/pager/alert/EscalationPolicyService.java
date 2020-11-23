package com.adreeana.pager.alert;

import com.adreeana.living_documentation.ExternalActor;

import static com.adreeana.living_documentation.ExternalActor.ActorType.SYSTEM;
import static com.adreeana.living_documentation.ExternalActor.Direction.SPI;

@ExternalActor(name = "Escalation Policy Infrastructure Service", direction = SPI, type = SYSTEM)
public interface EscalationPolicyService {
  EscalationPolicy findEscalationPolicy(MonitoredService monitoredService);
}
