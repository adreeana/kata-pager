package com.adreeana.alert;

import com.adreeana.living_documentation.ValueObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ValueObject
public class Level {
  private final List<Target> targets;

  public Level() {
    this.targets = new ArrayList<>();
  }

  public void addTarget(Target target) {
    targets.add(target);
  }

  public List<Target> getTargets() {
    return Collections.unmodifiableList(targets);
  }
}
