package io.gustavoamigo.quill.pgsql.encoding.range

import java.sql.ResultSet

import io.getquill.source.jdbc.JdbcSource

import scala.util.matching.Regex.Match

trait Decoders {
  this: JdbcSource[_, _] =>

  private def genericDecoder[T](fromString: (String => T)) = {
    new Decoder[T] {
      override def apply(index: Int, row: ResultSet): T = {
        fromString(row.getString(index + 1))
      }
    }
  }

  private def firstInRange(m: Match) = m.group(1).toInt
  private def lastInRange(m: Match) = m.group(2).toInt - 1

  private val rangePattern = """\[(\d+),(\d+)\)""".r

  implicit val intTupleDecoder: Decoder[(Int, Int)] = genericDecoder(s => {
    rangePattern.findFirstMatchIn(s) match {
      case Some(m) => (firstInRange(m), lastInRange(m))
    }
  })
}
