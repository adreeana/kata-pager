package com.adreeana.pager.alert;

import com.adreeana.living_documentation.ValueObject;

@ValueObject
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
