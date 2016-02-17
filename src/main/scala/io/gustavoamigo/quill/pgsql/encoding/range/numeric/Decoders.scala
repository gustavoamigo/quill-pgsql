package io.gustavoamigo.quill.pgsql.encoding.range.numeric

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

  private val rangePattern = """\[(\d+\.*\d*),(\d+\.*\d*)[\]|)]""".r

  private def decode[T](transform: (String, String) => T) = genericDecoder(s =>
    rangePattern.findFirstMatchIn(s) match {
      case Some(m) => transform(m.group(1), m.group(2))
    }
  )

  implicit val intTupleDecoder: Decoder[(Int, Int)] = decode((s1, s2) => (s1.toInt, s2.toInt - 1))
  implicit val intRangeDecoder: Decoder[NumericRange[Int]] = decode((s1, s2) => Range.Int(s1.toInt, s2.toInt, 1))
  implicit val bigIntTupleDecoder: Decoder[(BigInt, BigInt)] = decode((s1, s2) => (BigInt(s1), BigInt(s2) - BigInt(1)))
  implicit val bigIntRangeDecoder: Decoder[NumericRange[BigInt]] =
    decode((s1, s2) => Range.BigInt(BigInt(s1), BigInt(s2), BigInt(1)))
  implicit val longTupleDecoder: Decoder[(Long, Long)] = decode((s1, s2) => (s1.toLong, s2.toLong - 1))
  implicit val longRangeDecoder: Decoder[NumericRange[Long]] = decode((s1, s2) => Range.Long(s1.toLong, s2.toLong, 1))
  implicit val doubleTupleDecoder: Decoder[(Double, Double)] = decode((s1, s2) => (s1.toDouble, s2.toDouble))
  implicit val bigDecimalTupleDecoder: Decoder[(BigDecimal, BigDecimal)] = decode((s1, s2) => (BigDecimal(s1), BigDecimal(s2)))
  implicit val bigDecimalRangeDecoder: Decoder[NumericRange[BigDecimal]] = decode((s1, s2) => {
    val (d1, d2) = (BigDecimal(s1), BigDecimal(s2))
    Range.BigDecimal.inclusive(d1, d2, step(d1, d2))
  })

  private def step(d1: BigDecimal, d2: BigDecimal): BigDecimal = {
    val fraction1 = d1.remainder(BigDecimal(1)).toString.length
    val fraction2 = d2.remainder(BigDecimal(1)).toString.length

    val fraction = if (fraction1 > fraction2) d1 else d2
    BigDecimal(1) / BigDecimal(10).pow(fraction.precision - 1)
  }
}