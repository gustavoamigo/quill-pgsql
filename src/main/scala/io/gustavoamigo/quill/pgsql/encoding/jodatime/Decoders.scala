package io.gustavoamigo.quill.pgsql.encoding.jodatime

import io.getquill.source.jdbc.JdbcSource
import io.gustavoamigo.quill.pgsql.encoding.GenericDecoder
import org.joda.time._

trait Decoders extends GenericDecoder {
  this: JdbcSource[_, _] =>

  import Formatters._

  implicit val jodaDateTimeDecoder: Decoder[DateTime] = decode(s => DateTime.parse(s,
    if (s.indexOf(":") > 2) {
      if (s.indexOf(".") > 0) jodaTzDateTimeFormatter else jodaTzDateTimeFormatter_NoFraction
    }
    else {
      if (s.indexOf(".") > 0) jodaTzTimeFormatter else jodaTzTimeFormatter_NoFraction
    }))

  implicit val jodaLocalDateDecoder: Decoder[LocalDate] = decode(LocalDate.parse(_, jodaDateFormatter))
  implicit val jodalocalTimeDecoder: Decoder[LocalTime] = decode(s => LocalTime.parse(s,
    if (s.indexOf(".") > 0) jodaTimeFormatter else jodaTimeFormatter_NoFraction))
}