package io.gustavoamigo.quill.pgsql.encoding.json.play

import io.getquill.source.jdbc.JdbcSource
import io.gustavoamigo.quill.pgsql.encoding.GenericDecoder

trait JsonDecoder extends GenericDecoder {
  this: JdbcSource[_, _] =>

  import play.api.libs.json._

  implicit val jsonDecoder: Decoder[JsValue] = decode(Json.parse)
}