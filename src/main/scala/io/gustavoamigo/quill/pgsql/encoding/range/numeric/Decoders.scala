package io.gustavoamigo.quill.pgsql.encoding.range.numeric

import io.getquill.source.jdbc.JdbcSource
import io.gustavoamigo.quill.pgsql.encoding.GenericDecoder

import scala.collection.immutable.NumericRange

trait Decoders extends GenericDecoder {
  this: JdbcSource[_, _] =>

  private val rangePattern = """\[(\d+\.*\d*),(\d+\.*\d*)[\]|)]""".r

  private def decoder[T](transform: (String, String) => T) = decode(s =>
    rangePattern.findFirstMatchIn(s) match {
      case Some(m) => transform(m.group(1), m.group(2))
    }
  )

  implicit val intTupleDecoder: Decoder[(Int, Int)] = decoder((s1, s2) => (s1.toInt, s2.toInt - 1))
  implicit val intRangeDecoder: Decoder[NumericRange[Int]] = decoder((s1, s2) => Range.Int(s1.toInt, s2.toInt, 1))
  implicit val bigIntTupleDecoder: Decoder[(BigInt, BigInt)] = decoder((s1, s2) => (BigInt(s1), BigInt(s2) - BigInt(1)))
  implicit val bigIntRangeDecoder: Decoder[NumericRange[BigInt]] =
    decoder((s1, s2) => Range.BigInt(BigInt(s1), BigInt(s2), BigInt(1)))
  implicit val longTupleDecoder: Decoder[(Long, Long)] = decoder((s1, s2) => (s1.toLong, s2.toLong - 1))
  implicit val longRangeDecoder: Decoder[NumericRange[Long]] = decoder((s1, s2) => Range.Long(s1.toLong, s2.toLong, 1))
  implicit val doubleTupleDecoder: Decoder[(Double, Double)] = decoder((s1, s2) => (s1.toDouble, s2.toDouble))
  implicit val bigDecimalTupleDecoder: Decoder[(BigDecimal, BigDecimal)] = decoder((s1, s2) => (BigDecimal(s1), BigDecimal(s2)))
  implicit val bigDecimalRangeDecoder: Decoder[NumericRange[BigDecimal]] = decoder((s1, s2) => {
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