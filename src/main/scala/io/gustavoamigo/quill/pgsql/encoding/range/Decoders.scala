package io.gustavoamigo.quill.pgsql.encoding.range

import java.sql.ResultSet

import io.getquill.source.jdbc.JdbcSource

import scala.collection.immutable.NumericRange

trait Decoders {
  this: JdbcSource[_, _] =>

  private def genericDecoder[T](fromString: (String => T)) = {
    new Decoder[T] {
      override def apply(index: Int, row: ResultSet): T = {
        fromString(row.getString(index + 1))
      }
    }
  }

  private val rangePattern = """\[(\d+),(\d+)\)""".r

  private def decode[T](transform: (String, String) => T) = genericDecoder(s =>
    rangePattern.findFirstMatchIn(s) match {
      case Some(m) => transform(m.group(1), m.group(2))
    }
  )

  implicit val intTupleDecoder: Decoder[(Int, Int)] = decode((s1, s2) => (s1.toInt, s2.toInt - 1))
  implicit val intRangeDecoder: Decoder[NumericRange[Int]] = decode((s1, s2) => Range.Int(s1.toInt, s2.toInt, 1))
  implicit val bigIntTupleDecoder: Decoder[(BigInt, BigInt)] = decode((s1, s2) => (BigInt(s1), BigInt(s2) - BigInt(1)))
}