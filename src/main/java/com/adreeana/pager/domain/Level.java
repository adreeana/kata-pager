package com.adreeana.pager.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Level {
  private List<Target> targets;
  private Boolean acknowledged;

  public Level() {
    this.targets = new ArrayList<Target>();
    this.acknowledged = false;
  }

  public void addTarget(Target target) {
    targets.add(target);
  }

  public List<Target> getTargets() {
    return Collections.unmodifiableList(targets);
  }

  public Boolean isAcknowledged() {
    return acknowledged;
  }

  void acknowledge() {
    acknowledged = true;
  }
}
