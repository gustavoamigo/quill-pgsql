package io.gustavoamigo.quill.pgsql.encoding.range

import java.sql.ResultSet

import io.getquill.source.jdbc.JdbcSource

import scala.collection.immutable.NumericRange
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
  private def lastInRange(m: Match) = m.group(2).toInt

  private val rangePattern = """\[(\d+),(\d+)\)""".r

  private def decode[T](transform: Match => T) = genericDecoder(s =>
    rangePattern.findFirstMatchIn(s) match {
      case Some(matcher) => transform(matcher)
    }
  )

  implicit val intTupleDecoder: Decoder[(Int, Int)] = decode(m => (firstInRange(m), lastInRange(m) - 1))
  implicit val intRangeDecoder: Decoder[NumericRange[Int]] = decode(m => Range.Int(firstInRange(m), lastInRange(m), 1))
}