package io.gustavoamigo.quill.pgsql.encoding.range.numeric

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

  private def tuple[T](t: (T, T)) = s"[${t._1}, ${t._2}]"

  private def range[T](r: NumericRange[T]) = s"[${r.head}, ${r.last}]"

  implicit val intTupleEncoder: Encoder[(Int, Int)] = genericEncoder(tuple)
  implicit val intRangeEncoder: Encoder[NumericRange[Int]] = genericEncoder(range)
  implicit val bigIntTupleEncoder: Encoder[(BigInt, BigInt)] = genericEncoder(tuple)
  implicit val bigIntRangeEncoder: Encoder[NumericRange[BigInt]] = genericEncoder(range)
  implicit val longTupleEncoder: Encoder[(Long, Long)] = genericEncoder(tuple)
  implicit val longRangeEncoder: Encoder[NumericRange[Long]] = genericEncoder(range)
  implicit val doubleTupleEncoder: Encoder[(Double, Double)] = genericEncoder(tuple)
  implicit val bigDecimalTupleEncoder: Encoder[(BigDecimal, BigDecimal)] = genericEncoder(tuple)
  implicit val bigDecimalRangeEncoder: Encoder[NumericRange[BigDecimal]] = genericEncoder(range)
}

