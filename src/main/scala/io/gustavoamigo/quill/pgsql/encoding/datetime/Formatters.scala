package io.gustavoamigo.quill.pgsql.encoding.datetime

import java.time.format.{DateTimeFormatter, DateTimeFormatterBuilder}
import java.time.temporal.ChronoField

private[datetime] object Formatters {
  val bpDateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
  val bpTimeFormatter = DateTimeFormatter.ISO_LOCAL_TIME
  val bpTzTimeFormatter =
    new DateTimeFormatterBuilder()
      .append(DateTimeFormatter.ofPattern("HH:mm:ss"))
      .optionalStart()
      .appendFraction(ChronoField.NANO_OF_SECOND,0,6,true)
      .optionalEnd()
      .appendOffset("+HH:mm","+00")
      .toFormatter()
  val bpTzDateTimeFormatter =
    new DateTimeFormatterBuilder()
      .append(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
      .optionalStart()
      .appendFraction(ChronoField.NANO_OF_SECOND,0,6,true)
      .optionalEnd()
      .appendOffset("+HH:mm","+00")
      .toFormatter()
}
