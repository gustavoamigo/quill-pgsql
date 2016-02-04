package io.gustavoamigo.quill.pgsql.encoding.jodatime

import java.sql.{Types, PreparedStatement}

import io.getquill.source.jdbc.JdbcSource
import org.joda.time._

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

  implicit val jodaDateTimeEncoder: Encoder[DateTime] = genericEncoder(_.toString(jodaTzDateTimeFormatter))
  implicit val jodaLocalDateEncoder: Encoder[LocalDate] = genericEncoder(_.toString(jodaDateFormatter))
  implicit val jodaLocalTimeEncoder: Encoder[LocalTime] = genericEncoder(_.toString(jodaTimeFormatter))
}
