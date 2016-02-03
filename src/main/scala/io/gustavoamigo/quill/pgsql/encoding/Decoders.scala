package io.gustavoamigo.quill.pgsql.encoding

import java.sql.ResultSet
import java.time
import java.time.{OffsetTime, LocalTime, LocalDate, ZonedDateTime}

import io.getquill._
import io.getquill.source.jdbc.JdbcSource

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

  implicit val localDateTimeDecoder: Decoder[time.LocalDateTime] = decoder { (row) => (index) =>
    row.getTimestamp(index).toLocalDateTime
  }
  implicit val zonedDateTimeDecoder: Decoder[time.ZonedDateTime] = genericDecoder(ZonedDateTime.parse(_, bpTzDateTimeFormatter))
  implicit val LocalDateDecoder: Decoder[time.LocalDate] = genericDecoder(LocalDate.parse(_, bpDateFormatter))
  implicit val localTimeDecoder: Decoder[time.LocalTime] = genericDecoder(LocalTime.parse(_, bpTimeFormatter))
  implicit val offsetTimeDecoder: Decoder[time.OffsetTime] = genericDecoder(OffsetTime.parse(_, bpTzTimeFormatter))

}
