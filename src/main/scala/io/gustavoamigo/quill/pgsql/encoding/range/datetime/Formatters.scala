package io.gustavoamigo.quill.pgsql.encoding.range.datetime

import java.text.SimpleDateFormat
import java.util.Date

private[datetime] object Formatters {
  private val df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

  def parse(s1: String, s2: String): (Date, Date) = (df.parse(s1), df.parse(s2))

  def format[Date](date: Date): String = df.format(date)
}
