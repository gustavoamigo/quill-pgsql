package io.gustavoamigo.quill.pgsql.encoding.datetime

import java.sql.ResultSet
import java.time._
import io.getquill.source.jdbc.JdbcSource
import io.gustavoamigo.quill.pgsql.encoding.datetime

trait Decoders {
  this: JdbcSource[_, _] =>

  import Formatters._

  private def genericDecoder[T](fnFromString: (String => T)) =
    new Decoder[T] {
      def apply(index: Int, row: ResultSet) = {
        fnFromString(row.getString(index + 1))
      }

    }

  private def decoder[T](f: ResultSet => Int => T): Decoder[T] =
    new Decoder[T] {
      def apply(index: Int, row: ResultSet) =
        f(row)(index + 1)
    }

  implicit val localDateTimeDecoder: Decoder[LocalDateTime] = decoder { (row) => (index) =>
    row.getTimestamp(index).toLocalDateTime
  }
  implicit val zonedDateTimeDecoder: Decoder[ZonedDateTime] = genericDecoder(ZonedDateTime.parse(_, bpTzDateTimeFormatter))
  implicit val LocalDateDecoder: Decoder[LocalDate] = genericDecoder(LocalDate.parse(_, bpDateFormatter))
  implicit val localTimeDecoder: Decoder[LocalTime] = genericDecoder(LocalTime.parse(_, bpTimeFormatter))
  implicit val offsetTimeDecoder: Decoder[OffsetTime] = genericDecoder(OffsetTime.parse(_, bpTzTimeFormatter))

}
