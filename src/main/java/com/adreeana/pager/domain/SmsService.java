package com.adreeana.pager.domain;

public interface SmsService {
  void notify(String phoneNumber, String message);
}
