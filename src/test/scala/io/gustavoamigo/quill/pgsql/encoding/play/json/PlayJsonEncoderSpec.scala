package io.gustavoamigo.quill.pgsql.encoding.play.json

import io.getquill._
import io.getquill.naming.CamelCase
import io.gustavoamigo.quill.pgsql.PostgresJdbcSource
import io.gustavoamigo.quill.pgsql.encoding.json.play.PlayJsonSupport
import org.specs2.mutable.Specification
import org.specs2.specification.BeforeAll
import play.api.libs.json._

import scala.util.{Failure, Success, Try}

class PlayJsonEncoderSpec extends Specification with BeforeAll {

  object db extends PostgresJdbcSource[CamelCase] with PlayJsonSupport

  def beforeAll = {
    db.execute("DROP TABLE IF EXISTS EncodeJson")
    db.execute(
      """
        |CREATE TABLE EncodeJson (
        |   name TEXT,
        |   json JSONB)
        | """.stripMargin
    )
  }

  case class EncodeJson(name: String, json: JsValue)

  "Postgres Json" should {
    "just work" in {
      val encodeJson = quote(query[EncodeJson]("EncodeJson"))
      val insert = quote(encodeJson.insert)
      val select = quote(encodeJson.filter(_.name == "test1"))
      val json = Json.parse("{\"myJson\":true}")
      Try(db.run(insert)(List(EncodeJson("test1", json)))) match {
        case Success(_) => // OK
        case Failure(e: java.sql.BatchUpdateException) => e.getNextException.printStackTrace()
        case Failure(e) => throw e
      }
      val found = db.run(select)
      found.head.json must beEqualTo(json)
    }
  }

}
