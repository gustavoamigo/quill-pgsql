package io.gustavoamigo.quill.pgsql

import java.time._
import java.util.Date

import io.getquill._
import io.getquill.naming.CamelCase
import org.specs2.mutable.Specification
import org.specs2.specification.BeforeAll

import scala.util.{Try, Success, Failure}

class EncodersSpec extends Specification with BeforeAll {

  object db extends PostgresJdbcSource[CamelCase]

  def beforeAll = {
    db.execute("DROP TABLE IF EXISTS EncodeTimestamp")
    db.execute(
      """
        |CREATE TABLE EncodeTimestamp (
        |   name TEXT,
        |   ts TIMESTAMP,
        |   tstz TIMESTAMP WITH TIME ZONE,
        |   d DATE,
        |   t TIME,
        |   ttz TIME WITH TIME ZONE)
        | """.stripMargin
    )
  }

  case class EncodeTimestamp1(name: String, ts: Date)
  case class EncodeTimestamp2(name: String, ts: LocalDateTime)
  case class EncodeTimestamp3(name: String, tstz: ZonedDateTime)
  case class EncodeTimestamp4(name: String, d: LocalDate)
  case class EncodeTimestamp5(name: String, t: LocalTime)
  case class EncodeTimestamp6(name: String, ttz: OffsetTime)

  "Date mapped to TIMESTAMP" should {
    "just work" in {
      val encodeTimestamp1 = quote(query[EncodeTimestamp1]("EncodeTimestamp"))
      val now = new Date()
      val insert = quote(encodeTimestamp1.insert)
      val select = quote(encodeTimestamp1.filter(_.name == "test1"))
      db.run(insert)(List(EncodeTimestamp1("test1", now)))
      val found = db.run(select)
      found.head.ts must beEqualTo(now)
    }
  }

  "LocalDateTime mapped to TIMESTAMP" should {
    "just work" in {
      val encodeTimestamp2 = quote(query[EncodeTimestamp2]("EncodeTimestamp"))
      val now = LocalDateTime.now()
      val insert = quote(encodeTimestamp2.insert)
      val select = quote(encodeTimestamp2.filter(_.name == "test2"))
      db.run(insert)(List(EncodeTimestamp2("test2", now)))
      val found = db.run(select)
      found.head.ts must beEqualTo(now)
    }
  }

  "ZonedDateTime mapped to TIMESTAMP WITH TIME ZONE" should {
    "just work" in {
      val encodeTimestamp3 = quote(query[EncodeTimestamp3]("EncodeTimestamp"))
      val now = ZonedDateTime.now()
      val insert = quote(encodeTimestamp3.insert)
      val select = quote(encodeTimestamp3.filter(_.name == "test3"))

      Try(db.run(insert)(List(EncodeTimestamp3("test3", now)))) match {
        case Success(_) => // OK
        case Failure(e: java.sql.BatchUpdateException) => e.getNextException.printStackTrace()
        case Failure(e) => throw e
      }
      val found = db.run(select)
      found.head.tstz.toEpochSecond must beEqualTo(now.toEpochSecond)
    }
  }

  "LocalDate mapped to DATE" should {
    "just work" in {
      val encodeTimestamp4 = quote(query[EncodeTimestamp4]("EncodeTimestamp"))
      val now = LocalDate.now()
      val insert = quote(encodeTimestamp4.insert)
      val select = quote(encodeTimestamp4.filter(_.name == "test4"))

      Try(db.run(insert)(List(EncodeTimestamp4("test4", now)))) match {
        case Success(_) => // OK
        case Failure(e: java.sql.BatchUpdateException) => e.getNextException.printStackTrace()
        case Failure(e) => throw e
      }
      val found = db.run(select)
      found.head.d must beEqualTo(now)
    }
  }

  "LocalTime mapped to TIME" should {
    "just work" in {
      val encodeTimestamp5 = quote(query[EncodeTimestamp5]("EncodeTimestamp"))
      val now = LocalTime.now()
      val insert = quote(encodeTimestamp5.insert)
      val select = quote(encodeTimestamp5.filter(_.name == "test5"))

      Try(db.run(insert)(List(EncodeTimestamp5("test5", now)))) match {
        case Success(_) => // OK
        case Failure(e: java.sql.BatchUpdateException) => e.getNextException.printStackTrace()
        case Failure(e) => throw e
      }
      val found = db.run(select)
      found.head.t must beEqualTo(now)
    }
  }

  "OffsetTime mapped to TIME WITH TIMEZONE" should {
    "just work" in {
      val encodeTimestamp6 = quote(query[EncodeTimestamp6]("EncodeTimestamp"))
      val now = OffsetTime.now()
      val insert = quote(encodeTimestamp6.insert)
      val select = quote(encodeTimestamp6.filter(_.name == "test6"))

      Try(db.run(insert)(List(EncodeTimestamp6("test6", now)))) match {
        case Success(_) => // OK
        case Failure(e: java.sql.BatchUpdateException) => e.getNextException.printStackTrace()
        case Failure(e) => throw e
      }
      val found = db.run(select)
      found.head.ttz must beEqualTo(now)
    }
  }

}
