package io.gustavoamigo.quill.pgsql.encoding.json.play

import java.sql.{Types, PreparedStatement}

import io.getquill.source.jdbc.JdbcSource

trait JsonEncoder {
  this: JdbcSource[_, _] =>

  import play.api.libs.json._

  private def genericEncoder[T](valueToString: (T => String) = (r: T) => r.toString): Encoder[T] =
    new Encoder[T] {
      override def apply(index: Int, value: T, row: PreparedStatement) = {
        row.setObject(index + 1, valueToString(value), Types.OTHER)
        row
      }
    }

  implicit val jsonEncoder: Encoder[JsValue] = genericEncoder(Json.stringify)
}
