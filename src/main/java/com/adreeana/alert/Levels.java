package com.adreeana.alert;

import com.adreeana.living_documentation.ValueObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ValueObject
public class Levels {
  private List<Level> levels;

  public Levels() {
    this.levels = new ArrayList();
  }

  public void addLevel(Level level) {
    levels.add(level);
  }

  public Level first() {
    return levels.get(0);
  }

  public Optional<Level> next(Level from) {
    int fromIndex = levels.indexOf(from);
    if (fromIndex != -1 && levels.size() > fromIndex + 1)
      return Optional.of(levels.get(fromIndex + 1));

    return Optional.empty();
  }
}