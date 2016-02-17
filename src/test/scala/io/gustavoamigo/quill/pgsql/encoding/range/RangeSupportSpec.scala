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
}