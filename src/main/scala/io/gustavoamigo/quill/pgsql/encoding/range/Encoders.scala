package io.gustavoamigo.quill.pgsql.encoding.range

import java.sql.{Types, PreparedStatement}

import io.getquill.source.jdbc.JdbcSource

trait Encoders {
  this: JdbcSource[_, _] =>

  private def genericEncoder[T](valueToString: (T => String)): Encoder[T] = {
    new Encoder[T] {
      override def apply(index: Int, value: T, row: PreparedStatement) = {
        val sqlLiteral = valueToString(value)
        row.setObject(index + 1, sqlLiteral, Types.OTHER)
        row
      }
    }
  }

  implicit val tupleEncoder: Encoder[(Int, Int)] = genericEncoder(t => s"[${t._1}, ${t._2}]")
}

