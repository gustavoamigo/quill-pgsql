package io.gustavoamigo.quill.pgsql.encoding.range.datetime

import java.time.{ZonedDateTime, LocalDateTime}
import java.util.Date

import io.getquill.source.jdbc.JdbcSource
import io.gustavoamigo.quill.pgsql.encoding.GenericDecoder

trait Decoders extends GenericDecoder {
  this: JdbcSource[_, _] =>

  import Formatters._

  private val rangePattern = """([0-9\-\+\. :]+)""".r

  private def decoder[T](map: String => T) = decode(s => {
    val dates = rangePattern.findAllIn(s).toList
    (map(dates.head), map(dates.last))
  })

  implicit val dateTupleDecoder: Decoder[(Date, Date)] = decoder(parseDate)
  implicit val localDateTimeTupleDecoder: Decoder[(LocalDateTime, LocalDateTime)] = decoder(parseLocalDateTime)
  implicit val zonedDateTimeTupleDecoder: Decoder[(ZonedDateTime, ZonedDateTime)] = decoder(parseZonedDateTime)
}