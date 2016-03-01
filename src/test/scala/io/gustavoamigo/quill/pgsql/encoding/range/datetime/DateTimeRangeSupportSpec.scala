package io.gustavoamigo.quill.pgsql.encoding.range.datetime

import java.util.Date
import java.util.concurrent.TimeUnit

import io.getquill._
import io.getquill.naming.CamelCase
import io.gustavoamigo.quill.pgsql.PostgresJdbcSource
import org.specs2.mutable.Specification
import org.specs2.specification.BeforeAll

class DateTimeRangeSupportSpec extends Specification with BeforeAll {

  object db extends PostgresJdbcSource[CamelCase] with DateTimeRangeSupport

  def beforeAll = {
    db.execute("DROP TABLE IF EXISTS EncodeRange")
    db.execute(
      """
        |CREATE TABLE EncodeRange (
        |   name TEXT,
        |   tsr TSRANGE,
        |   tstzr TSTZRANGE,
        |   dr DATERANGE)
        | """.stripMargin
    )
  }

  case class EncodeDateTuple(name: String, tsr: (Date, Date))

  "Tuple (Date, Date) mapped to TSRANGE" should {
    "just work" in {
      val encodeDateTuple = quote(query[EncodeDateTuple]("EncodeRange"))
      val now = new Date()
      val tomorrowTime = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1l);
      val tomorrow = new Date(tomorrowTime)

      val insert = quote(encodeDateTuple.insert)
      val select = quote(encodeDateTuple.filter(_.name == "test1"))
      db.run(insert)(List(EncodeDateTuple("test1", (now, tomorrow))))
      val found = db.run(select)
      found.head.tsr.toString must beEqualTo((now, tomorrow).toString)
    }
  }
}
