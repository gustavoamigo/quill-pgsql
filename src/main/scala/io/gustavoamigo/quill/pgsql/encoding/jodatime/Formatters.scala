package io.gustavoamigo.quill.pgsql.encoding.jodatime

import org.joda.time.format.{DateTimeFormat, ISODateTimeFormat}

private[jodatime] object Formatters {
  val jodaDateFormatter = ISODateTimeFormat.date()
  val jodaTimeFormatter = DateTimeFormat.forPattern("HH:mm:ss.SSSSSS")
  val jodaTimeFormatter_NoFraction = DateTimeFormat.forPattern("HH:mm:ss")
  val jodaDateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
  val jodaDateTimeFormatter_NoFraction = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
  val jodaTzTimeFormatter = DateTimeFormat.forPattern("HH:mm:ss.SSSSSSZ")
  val jodaTzTimeFormatter_NoFraction = DateTimeFormat.forPattern("HH:mm:ssZ")
  val jodaTzDateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSSSSSZ")
  val jodaTzDateTimeFormatter_NoFraction = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ssZ")
}
