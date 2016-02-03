package io.gustavoamigo.quill.pgsql.encoding

import java.sql.{PreparedStatement, Timestamp, Types}
import java.time

import io.getquill._
import io.getquill.source.jdbc.JdbcSource

trait Encoders {
  this: JdbcSource[_, _] =>

  import Formatters._

  private def genericEncoder[T](valueToString: (T => String) = (r: T) => r.toString): Encoder[T] =
    new Encoder[T] {
      override def apply(index: Int, value: T, row: PreparedStatement) = {
        val sqlLiteral = s"'${valueToString(value)}'"
        row.setObject(index + 1, sqlLiteral, Types.OTHER)
        row
      }
    }

  private def encoder[T](f: PreparedStatement => (Int, T) => Unit): Encoder[T] =
    new Encoder[T] {
      override def apply(index: Int, value: T, row: PreparedStatement) = {
        f(row)(index + 1, value)
        row
      }
    }

  implicit val localDateTimeEncoder: Encoder[time.LocalDateTime] = encoder { (row: PreparedStatement) =>
    (index: Int, d: time.LocalDateTime) => row.setObject(index, Timestamp.valueOf(d), Types.TIMESTAMP)
  }
  implicit val zonedDateTimeEncoder: Encoder[time.ZonedDateTime] = genericEncoder(_.format(bpTzDateTimeFormatter))
  implicit val localDateEncoder: Encoder[time.LocalDate] = genericEncoder(_.format(bpDateFormatter))
  implicit val localTimeEncoder: Encoder[time.LocalTime] = genericEncoder(_.format(bpTimeFormatter))
  implicit val offsetTimeEncoder: Encoder[time.OffsetTime] = genericEncoder(_.format(bpTzTimeFormatter))

}
