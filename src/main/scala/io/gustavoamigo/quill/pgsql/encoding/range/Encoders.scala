package io.gustavoamigo.quill.pgsql.encoding.range

import java.sql.{Types, PreparedStatement}

import io.getquill.source.jdbc.JdbcSource

import scala.collection.immutable.NumericRange

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

  private def rangeFormat[T](first: T, last: T) = s"[${first}, ${last}]"

  implicit val intTupleEncoder: Encoder[(Int, Int)] = genericEncoder(t => rangeFormat(t._1, t._2))
  implicit val intRangeEncoder: Encoder[NumericRange[Int]] = genericEncoder(r => rangeFormat(r.head, r.last))
  implicit val bigIntTupleEncoder: Encoder[(BigInt, BigInt)] = genericEncoder(t => rangeFormat(t._1, t._2))
}

