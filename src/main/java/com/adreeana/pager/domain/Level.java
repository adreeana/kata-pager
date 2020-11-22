package com.adreeana.pager.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Level {
  private Set<Target> targets;
  private Boolean acknowledged;

  public Level() {
    this.targets = new HashSet<Target>();
    this.acknowledged = false;
  }

  public void addTarget(Target target) {
    targets.add(target);
  }

  public Set<Target> getTargets() {
    return Collections.unmodifiableSet(targets);
  }

  public Boolean isAcknowledged() {
    return acknowledged;
  }

  void acknowledge() {
    acknowledged = true;
  }
}
