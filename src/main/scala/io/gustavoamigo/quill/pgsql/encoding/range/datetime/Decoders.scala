package io.gustavoamigo.quill.pgsql.encoding.range.datetime

import java.time.LocalDateTime
import java.util.Date

import io.getquill.source.jdbc.JdbcSource
import io.gustavoamigo.quill.pgsql.encoding.GenericDecoder

trait Decoders extends GenericDecoder {
  this: JdbcSource[_, _] =>

  import Formatters._

  private val rangePattern = """([0-9\- :]+)""".r

  private def decoder[T](transform: (String, String) => T) = decode(s => {
    val dates = rangePattern.findAllIn(s).toList
    transform(dates.head, dates.last)
  })

  implicit val dateTupleDecoder: Decoder[(Date, Date)] = decoder(parse)
  implicit val localDateTimeTupleDecoder: Decoder[(LocalDateTime, LocalDateTime)] = decoder(parseLocalDateTime)
}