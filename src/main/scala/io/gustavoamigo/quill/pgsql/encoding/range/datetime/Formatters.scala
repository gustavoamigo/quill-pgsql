package io.gustavoamigo.quill.pgsql.encoding.range.datetime

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

private[datetime] object Formatters {
  private val df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

  val dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

  def parseDate(strDate: String): Date = df.parse(strDate)

  def formatDate(date: Date): String = df.format(date)

  def parseLocalDateTime(strDate: String): LocalDateTime = LocalDateTime.parse(strDate, dateTimeFormat)

  def formatLocalDateTime(dateTime: LocalDateTime): String = dateTime.format(dateTimeFormat)
}
