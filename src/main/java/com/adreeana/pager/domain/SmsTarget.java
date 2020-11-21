package com.adreeana.pager.domain;

public class SmsTarget implements Target {
  private final String phoneNumber;

  public SmsTarget(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  @Override
  public String contact() {
    return phoneNumber;
  }
}
