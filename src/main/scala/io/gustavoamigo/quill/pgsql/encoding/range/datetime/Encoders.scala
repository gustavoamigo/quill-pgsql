package io.gustavoamigo.quill.pgsql.encoding.range.datetime

import java.sql.{PreparedStatement, Types}
import java.time.{LocalDate, ZonedDateTime, LocalDateTime}
import java.util.Date

import io.getquill.source.jdbc.JdbcSource

trait Encoders {
  this: JdbcSource[_, _] =>

  import Formatters._

  private def genericEncoder[T](valueToString: (T => String)): Encoder[T] = {
    new Encoder[T] {
      override def apply(index: Int, value: T, row: PreparedStatement) = {
        val sqlLiteral = valueToString(value)
        row.setObject(index + 1, sqlLiteral, Types.OTHER)
        row
      }
    }
  }

  private def tuple[T](t: (T, T))(valToStr: T => String) = s"[${valToStr(t._1)}, ${valToStr(t._2)}]"

  implicit val dateTupleEncoder: Encoder[(Date, Date)] = genericEncoder(tuple(_)(formatDate))
  implicit val localDateTimeTupleEncoder: Encoder[(LocalDateTime, LocalDateTime)] =
    genericEncoder(tuple(_)(formatLocalDateTime))
  implicit val zonedDateTimeTupleEncoder: Encoder[(ZonedDateTime, ZonedDateTime)] =
    genericEncoder(tuple(_)(formatZonedDateTime))
  implicit val dateTimeTupleEncoder: Encoder[(LocalDate, LocalDate)] =
    genericEncoder(t => s"[${formatLocalDate(t._1)}, ${formatLocalDate(t._2)})")
}