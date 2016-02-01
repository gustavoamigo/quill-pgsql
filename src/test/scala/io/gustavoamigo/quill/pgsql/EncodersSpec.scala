package io.gustavoamigo.quill.pgsql

import java.time.LocalDateTime
import java.util.Date

import io.getquill._
import io.getquill.naming.CamelCase
import org.specs2.mutable.Specification
import org.specs2.specification.AfterAll

import Encoders._

class EncodersSpec extends Specification with AfterAll{

  object db extends PostgresJdbcSource[CamelCase]

  def afterAll = {
    db.execute("DELETE FROM encodeTimestamp")
  }

  case class EncodeTimestamp1(name: String, d: Date)
  case class EncodeTimestamp2(name: String, d: LocalDateTime)

  "Date mapped to TIMESTAMP" should {
    "just work" in {
      val encodeTimestamp1 = quote(query[EncodeTimestamp1]("EncodeTimestamp"))
      val now = new Date()
      val insert = quote(encodeTimestamp1.insert)
      val select = quote(encodeTimestamp1.filter(_.name == "test1"))
      db.run(insert)(List(EncodeTimestamp1("test1", now)))
      val found = db.run(select)
      found.head.d must beEqualTo(now)
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
      found.head.d must beEqualTo(now)
    }
  }

}
