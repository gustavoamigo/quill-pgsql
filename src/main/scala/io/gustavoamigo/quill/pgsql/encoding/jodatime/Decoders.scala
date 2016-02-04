package io.gustavoamigo.quill.pgsql.encoding.jodatime

import java.sql.ResultSet

import io.getquill.source.jdbc.JdbcSource
import org.joda.time._

trait Decoders {
  this: JdbcSource[_, _] =>

  import Formatters._


  private def genericDecoder[T](fnFromString: (String => T)) =
    new Decoder[T] {
      def apply(index: Int, row: ResultSet) = {
        fnFromString(row.getString(index + 1))
      }

    }

  implicit val jodaDateTimeDecoder: Decoder[DateTime] = genericDecoder(s => DateTime.parse(s,
    if (s.indexOf(":") > 2) {
      if (s.indexOf(".") > 0) jodaTzDateTimeFormatter else jodaTzDateTimeFormatter_NoFraction
    }
    else {
      if (s.indexOf(".") > 0) jodaTzTimeFormatter else jodaTzTimeFormatter_NoFraction
    }))

  implicit val jodaLocalDateDecoder: Decoder[LocalDate] = genericDecoder(LocalDate.parse(_, jodaDateFormatter))
  implicit val jodalocalTimeDecoder: Decoder[LocalTime] = genericDecoder(s => LocalTime.parse(s,
    if (s.indexOf(".") > 0) jodaTimeFormatter else jodaTimeFormatter_NoFraction))

}
