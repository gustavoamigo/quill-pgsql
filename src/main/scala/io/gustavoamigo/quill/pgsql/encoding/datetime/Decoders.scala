package io.gustavoamigo.quill.pgsql.encoding.datetime

import java.sql.ResultSet
import java.time._

import io.getquill.source.jdbc.JdbcSource
import io.gustavoamigo.quill.pgsql.encoding.GenericDecoder

trait Decoders extends GenericDecoder {
  this: JdbcSource[_, _] =>

  import Formatters._

  private def decoder[T](f: ResultSet => Int => T): Decoder[T] =
    new Decoder[T] {
      def apply(index: Int, row: ResultSet) =
        f(row)(index + 1)
    }

  implicit val localDateTimeDecoder: Decoder[LocalDateTime] = decoder { (row) => (index) =>
    row.getTimestamp(index).toLocalDateTime
  }
  implicit val zonedDateTimeDecoder: Decoder[ZonedDateTime] = decode(ZonedDateTime.parse(_, bpTzDateTimeFormatter))
  implicit val LocalDateDecoder: Decoder[LocalDate] = decode(LocalDate.parse(_, bpDateFormatter))
  implicit val localTimeDecoder: Decoder[LocalTime] = decode(LocalTime.parse(_, bpTimeFormatter))
  implicit val offsetTimeDecoder: Decoder[OffsetTime] = decode(OffsetTime.parse(_, bpTzTimeFormatter))

}
