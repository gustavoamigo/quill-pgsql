package io.gustavoamigo.quill.pgsql

import java.sql.Timestamp
import java.time.LocalDateTime

import io.getquill._

object Encoders {

  implicit val encodeLocalDateTime = mappedEncoding[Timestamp, LocalDateTime](_.toLocalDateTime)
  implicit val decodeLocalDateTime = mappedEncoding[LocalDateTime, Timestamp](Timestamp.valueOf)

}
