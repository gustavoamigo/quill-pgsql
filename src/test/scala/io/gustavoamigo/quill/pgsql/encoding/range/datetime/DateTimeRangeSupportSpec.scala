package io.gustavoamigo.quill.pgsql.encoding.range.datetime

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
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

  case class EncodeLocalDateTimeTuple(name: String, tsr: (LocalDateTime, LocalDateTime))

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

  "Tuple (LocalDateTime, LocalDateTime) mapped to TSRANGE" should {
    "just work" in {
      val encodeLocalDateTimeTuple = quote(query[EncodeLocalDateTimeTuple]("EncodeRange"))
      val now = LocalDateTime.now
      val tomorrow = LocalDateTime.now.plusDays(1)

      val insert = quote(encodeLocalDateTimeTuple.insert)
      val select = quote(encodeLocalDateTimeTuple.filter(_.name == "test2"))
      db.run(insert)(List(EncodeLocalDateTimeTuple("test2", (now, tomorrow))))
      val found = db.run(select)
      found.head.tsr must beEqualTo(
        (now.truncatedTo(ChronoUnit.SECONDS), tomorrow.truncatedTo(ChronoUnit.SECONDS))
      )
    }
  }
}
