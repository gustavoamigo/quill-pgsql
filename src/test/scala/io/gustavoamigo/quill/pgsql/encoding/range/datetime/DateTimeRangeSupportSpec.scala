package io.gustavoamigo.quill.pgsql.encoding.range.datetime

import java.time.temporal.ChronoUnit
import java.time.{Duration, LocalDateTime, ZonedDateTime}
import java.util.Date

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

  case class EncodeZonedDateTimeTuple(name: String, tstzr: (ZonedDateTime, ZonedDateTime))

  "Tuple (Date, Date) mapped to TSRANGE" should {
    "just work" in {
      val encodeDateTuple = quote(query[EncodeDateTuple]("EncodeRange"))
      val now = new Date
      val tomorrowTime = System.currentTimeMillis + Duration.ofDays(1).toMillis;
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
      val tomorrow = now.plusDays(1)

      val insert = quote(encodeLocalDateTimeTuple.insert)
      val select = quote(encodeLocalDateTimeTuple.filter(_.name == "test2"))
      db.run(insert)(List(EncodeLocalDateTimeTuple("test2", (now, tomorrow))))
      val found = db.run(select)
      found.head.tsr must beEqualTo(
        (now.truncatedTo(ChronoUnit.SECONDS), tomorrow.truncatedTo(ChronoUnit.SECONDS))
      )
    }
  }

  "Tuple (ZonedDateTime, ZonedDateTime) mapped to TSTZRANGE" should {
    "just work" in {
      val encodeZonedDateTimeTuple = quote(query[EncodeZonedDateTimeTuple]("EncodeRange"))
      val now = ZonedDateTime.now
      val tomorrow = now.plusDays(1)

      val insert = quote(encodeZonedDateTimeTuple.insert)
      val select = quote(encodeZonedDateTimeTuple.filter(_.name == "test3"))
      db.run(insert)(List(EncodeZonedDateTimeTuple("test3", (now, tomorrow))))
      val found = db.run(select)
      found.head.tstzr._1.toEpochSecond must beEqualTo(now.toEpochSecond)
      found.head.tstzr._2.toEpochSecond must beEqualTo(tomorrow.toEpochSecond)
    }
  }
}
