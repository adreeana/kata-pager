package com.adreeana.alert;

import com.adreeana.living_documentation.ValueObject;

@ValueObject
public class EmailTarget implements Target {
  private final String email;

  public EmailTarget(String email) {
    this.email = email;
  }

  @Override
  public String contact() {
    return email;
  }
}
