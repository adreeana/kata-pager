package com.adreeana.pager.domain;

public class Notification {
  private String id;
  private Alert alert;
  private Target target;
  private int level;

  public Notification(Alert alert, Target target, int level) {
    this.alert = alert;
    this.target = target;
    this.level = level;
  }

  public Alert getAlert() {
    return alert;
  }

  public Target getTarget() {
    return target;
  }

  public int getLevel() {
    return level;
  }
}
