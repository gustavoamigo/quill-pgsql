package io.gustavoamigo.quill.pgsql.encoding.range

import io.getquill._
import io.getquill.naming.CamelCase
import io.gustavoamigo.quill.pgsql.PostgresJdbcSource
import org.specs2.mutable.Specification
import org.specs2.specification.BeforeAll

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

  case class EncodeRange1(name: String, ir: (Int, Int))

  "Tuple (Int, Int) mapped to INT4RANGE" should {
    "just work" in {
      val encodeRange1 = quote(query[EncodeRange1]("EncodeRange"))
      val range = (1, 5)
      val insert = quote(encodeRange1.insert)
      val select = quote(encodeRange1.filter(_.name == "test1"))

      db.run(insert)(List(EncodeRange1("test1", range)))

      val found = db.run(select)
      found.head.ir must beEqualTo(range)
    }
  }
}