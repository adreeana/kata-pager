package com.adreeana.pager.domain;

import java.util.ArrayList;
import java.util.List;

public class Levels {
  private List<Level> levels;

  public Levels() {
    this.levels = new ArrayList();;
  }

  public void addLevel(Level level) {
    levels.add(level);
  }

  public Level first() {
    return levels.get(0);
  }
}