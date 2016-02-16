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

  case class EncodeIntRange1(name: String, ir: (Int, Int))
  case class EncodeIntRange2(name: String, ir: NumericRange[Int])

  "Tuple (Int, Int) mapped to INT4RANGE" should {
    "just work" in {
      val encodeRange1 = quote(query[EncodeIntRange1]("EncodeRange"))
      val range = (1, 5)
      val insert = quote(encodeRange1.insert)
      val select = quote(encodeRange1.filter(_.name == "test1"))

      db.run(insert)(List(EncodeIntRange1("test1", range)))

      val found = db.run(select)
      found.head.ir must beEqualTo(range)
    }
  }

  "NumericRange[Int] mapped to INT4RANGE" should {
    "just work" in {
      val encodeRange1 = quote(query[EncodeIntRange2]("EncodeRange"))
      val range = Range.Int(2, 8, 1)
      val insert = quote(encodeRange1.insert)
      val select = quote(encodeRange1.filter(_.name == "test2"))

      db.run(insert)(List(EncodeIntRange2("test2", range)))

      val found = db.run(select)
      found.head.ir must beEqualTo(range)
    }
  }
}