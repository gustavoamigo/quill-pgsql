package io.gustavoamigo.quill.pgsql.encoding.range

import io.getquill._
import io.getquill.naming.CamelCase
import io.gustavoamigo.quill.pgsql.PostgresJdbcSource
import org.specs2.mutable.Specification
import org.specs2.specification.BeforeAll

import scala.collection.immutable.NumericRange

class RangeSupportSpec extends Specification with BeforeAll {

  object db extends PostgresJdbcSource[CamelCase] with RangeSupport

  def beforeAll = {
    db.execute("DROP TABLE IF EXISTS EncodeRange")
    db.execute(
      """
        |CREATE TABLE EncodeRange (
        |   name TEXT,
        |   ir INT4RANGE,
        |   br INT8RANGE,
        |   nr NUMRANGE,
        |   tsr TSRANGE,
        |   tstzr TSTZRANGE,
        |   dr DATERANGE)
        | """.stripMargin
    )
  }

  case class EncodeIntTuple(name: String, ir: (Int, Int))
  case class EncodeIntRange(name: String, ir: NumericRange[Int])
  case class EncodeBigIntTuple(name: String, br: (BigInt, BigInt))
  case class EncodeBigIntRange(name: String, br: NumericRange[BigInt])
  case class EncodeLongTuple(name: String, br: (Long, Long))
  case class EncodeLongRange(name: String, br: NumericRange[Long])
  case class EncodeDoubleTuple(name: String, nr: (Double, Double))
  case class EncodeDoubleRange(name: String, nr: NumericRange[Double])
  case class EncodeBigDecimalTuple(name: String, nr: (BigDecimal, BigDecimal))
  case class EncodeBigDecimalRange(name: String, nr: NumericRange[BigDecimal])

  "Tuple (Int, Int) mapped to INT4RANGE" should {
    "just work" in {
      val encodeIntTuple = quote(query[EncodeIntTuple]("EncodeRange"))
      val range = (1, 5)
      val insert = quote(encodeIntTuple.insert)
      val select = quote(encodeIntTuple.filter(_.name == "test1"))

      db.run(insert)(List(EncodeIntTuple("test1", range)))

      val found = db.run(select)
      found.head.ir must beEqualTo(range)
    }
  }

  "NumericRange[Int] mapped to INT4RANGE" should {
    "just work" in {
      val encodeIntRange = quote(query[EncodeIntRange]("EncodeRange"))
      val range = Range.Int(2, 8, 1)
      val insert = quote(encodeIntRange.insert)
      val select = quote(encodeIntRange.filter(_.name == "test2"))

      db.run(insert)(List(EncodeIntRange("test2", range)))

      val found = db.run(select)
      found.head.ir must beEqualTo(range)
    }
  }

  "Tuple (BigInt, BigInt) mapped to INT8RANGE" should {
    "just work" in {
      val encodeBigIntTuple = quote(query[EncodeBigIntTuple]("EncodeRange"))
      val range = (BigInt(15), BigInt(30))
      val insert = quote(encodeBigIntTuple.insert)
      val select = quote(encodeBigIntTuple.filter(_.name == "test3"))

      db.run(insert)(List(EncodeBigIntTuple("test3", range)))

      val found = db.run(select)
      found.head.br must beEqualTo(range)
    }
  }

  "NumericRange[BigInt] mapped to INT8RANGE" should {
    "just work" in {
      val encodeBigIntRange = quote(query[EncodeBigIntRange]("EncodeRange"))
      val range = Range.BigInt(BigInt(11), BigInt(99), BigInt(1))
      val insert = quote(encodeBigIntRange.insert)
      val select = quote(encodeBigIntRange.filter(_.name == "test4"))

      db.run(insert)(List(EncodeBigIntRange("test4", range)))

      val found = db.run(select)
      found.head.br must beEqualTo(range)
    }
  }

  "Tuple (Long, Long) mapped to INT8RANGE" should {
    "just work" in {
      val encodeLongTuple = quote(query[EncodeLongTuple]("EncodeRange"))
      val range: (Long, Long) = (290, 320)
      val insert = quote(encodeLongTuple.insert)
      val select = quote(encodeLongTuple.filter(_.name == "test5"))

      db.run(insert)(List(EncodeLongTuple("test5", range)))

      val found = db.run(select)
      found.head.br must beEqualTo(range)
    }
  }

  "NumericRange[Long] mapped to INT8RANGE" should {
    "just work" in {
      val encodeBigIntRange = quote(query[EncodeLongRange]("EncodeRange"))
      val range = Range.Long(9000, 9002, 1)
      val insert = quote(encodeBigIntRange.insert)
      val select = quote(encodeBigIntRange.filter(_.name == "test6"))

      db.run(insert)(List(EncodeLongRange("test6", range)))

      val found = db.run(select)
      found.head.br must beEqualTo(range)
    }
  }

  "Tuple (Double, Double) mapped to NUMRANGE" should {
    "just work" in {
      val encodeLongTuple = quote(query[EncodeDoubleTuple]("EncodeRange"))
      val range: (Double, Double) = (1.2000006, 5.4)
      val insert = quote(encodeLongTuple.insert)
      val select = quote(encodeLongTuple.filter(_.name == "test7"))

      db.run(insert)(List(EncodeDoubleTuple("test7", range)))

      val found = db.run(select)
      found.head.nr must beEqualTo(range)
    }
  }

  "NumericRange[Double] mapped to NUMRANGE" should {
    "just work" in {
      val encodeDoubleRange = quote(query[EncodeDoubleRange]("EncodeRange"))
      val range = Range.Double(1.8, 2.004, 0.001)
      val insert = quote(encodeDoubleRange.insert)
      val select = quote(encodeDoubleRange.filter(_.name == "test8"))

      db.run(insert)(List(EncodeDoubleRange("test8", range)))

      val found = db.run(select)
      found.head.nr must beEqualTo(range)
    }
  }

  "Tuple (BigDecimal, BigDecimal) mapped to NUMRANGE" should {
    "just work" in {
      val encodeBigDecimalTuple = quote(query[EncodeBigDecimalTuple]("EncodeRange"))
      val range = (BigDecimal(15), BigDecimal(30.0102030405))
      val insert = quote(encodeBigDecimalTuple.insert)
      val select = quote(encodeBigDecimalTuple.filter(_.name == "test9"))

      db.run(insert)(List(EncodeBigDecimalTuple("test9", range)))

      val found = db.run(select)
      found.head.nr must beEqualTo(range)
    }
  }

  "NumericRange[BigDecimal] mapped to NUMRANGE" should {
    "just work" in {
      val encodeBigDecimalRange = quote(query[EncodeBigDecimalRange]("EncodeRange"))
      val range = Range.BigDecimal.inclusive(BigDecimal(1.8), BigDecimal(2.001), BigDecimal(0.001))
      val insert = quote(encodeBigDecimalRange.insert)
      val select = quote(encodeBigDecimalRange.filter(_.name == "test10"))

      db.run(insert)(List(EncodeBigDecimalRange("test10", range)))

      val found = db.run(select)
      found.head.nr must beEqualTo(range)
    }
  }
}