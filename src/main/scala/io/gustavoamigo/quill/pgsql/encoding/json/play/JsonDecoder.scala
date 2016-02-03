package io.gustavoamigo.quill.pgsql.encoding.json.play

import java.sql.ResultSet
import io.getquill.source.jdbc.JdbcSource

trait JsonDecoder {
  this: JdbcSource[_, _] =>

  import play.api.libs.json._

  private def genericDecoder[T](fnFromString: (String => T)) =
    new Decoder[T] {
      def apply(index: Int, row: ResultSet) = {
        fnFromString(row.getString(index + 1))
      }

    }

  implicit val jsonDecoder: Decoder[JsValue] = genericDecoder(Json.parse)

}
