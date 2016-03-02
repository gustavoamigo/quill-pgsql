package io.gustavoamigo.quill.pgsql.encoding.range.datetime

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

private[datetime] object Formatters {
  private val df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

  val dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

  def parse(s1: String, s2: String): (Date, Date) = (df.parse(s1), df.parse(s2))

  def format(date: Date): String = df.format(date)

  def parseLocalDateTime(s1: String, s2: String): (LocalDateTime, LocalDateTime) =
    (LocalDateTime.parse(s1, dateTimeFormat), LocalDateTime.parse(s2, dateTimeFormat))

  def formatLocalDateTime(dateTime: LocalDateTime): String = dateTime.format(dateTimeFormat)
}
