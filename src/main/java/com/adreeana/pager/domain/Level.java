package com.adreeana.pager.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Level {
  private Set<Target> targets;

  public Level() {
    this.targets = new HashSet<Target>();
  }

  public void addTarget(Target target) {
    targets.add(target);
  }

  public Set<Target> getTargets() {
    return Collections.unmodifiableSet(targets);
  }
}
