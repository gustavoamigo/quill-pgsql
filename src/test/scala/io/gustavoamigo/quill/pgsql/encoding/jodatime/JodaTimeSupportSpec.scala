package io.gustavoamigo.quill.pgsql.encoding.jodatime

import io.getquill._
import io.getquill.naming.CamelCase
import io.gustavoamigo.quill.pgsql.PostgresJdbcSource
import org.specs2.mutable.Specification
import org.specs2.specification.BeforeAll
import org.joda.time._
import scala.util.{Failure, Success, Try}

class JodaTimeSupportSpec extends Specification with BeforeAll {

  object db extends PostgresJdbcSource[CamelCase] with JodaTimeSupport

  def beforeAll = {
    db.execute("DROP TABLE IF EXISTS JodaEncodeTimestamp")
    db.execute(
      """
        |CREATE TABLE JodaEncodeTimestamp (
        |   name TEXT,
        |   ts TIMESTAMP,
        |   tstz TIMESTAMP WITH TIME ZONE,
        |   d DATE,
        |   t TIME,
        |   ttz TIME WITH TIME ZONE)
        | """.stripMargin
    )
  }

  case class EncodeTimestamp3(name: String, tstz: DateTime)
  case class EncodeTimestamp4(name: String, d: LocalDate)
  case class EncodeTimestamp5(name: String, t: LocalTime)

  "DateTime mapped to TIMESTAMP WITH TIME ZONE" should {
    "just work" in {
      val encodeTimestamp3 = quote(query[EncodeTimestamp3]("JodaEncodeTimestamp"))
      val now = DateTime.now()
      val insert = quote(encodeTimestamp3.insert)
      val select = quote(encodeTimestamp3.filter(_.name == "test3"))

      Try(db.run(insert)(List(EncodeTimestamp3("test3", now)))) match {
        case Success(_) => // OK
        case Failure(e: java.sql.BatchUpdateException) => e.getNextException.printStackTrace()
        case Failure(e) => throw e
      }
      val found = db.run(select)
      found.head.tstz must beEqualTo(now)
    }
  }

  "LocalDate mapped to DATE" should {
    "just work" in {
      val encodeTimestamp4 = quote(query[EncodeTimestamp4]("JodaEncodeTimestamp"))
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
      val encodeTimestamp5 = quote(query[EncodeTimestamp5]("JodaEncodeTimestamp"))
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

}
