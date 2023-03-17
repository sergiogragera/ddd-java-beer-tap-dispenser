package com.rviewer.skeletons.infrastructure.persistence.adapters;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateAdapter {
  public static LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
    if (dateToConvert != null) {
      return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
    return null;
  }

  public static Date convertToDateViaInstant(LocalDateTime dateToConvert) {
    if (dateToConvert != null) {
      return java.util.Date.from(dateToConvert.atZone(ZoneId.systemDefault()).toInstant());
    }
    return null;
  }
}
