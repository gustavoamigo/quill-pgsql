package io.gustavoamigo.quill.pgsql.encoding

import java.sql.ResultSet

import io.getquill.source.jdbc.JdbcSource

trait GenericDecoder {
  this: JdbcSource[_, _] =>

  def decode[T](fnFromString: (String => T)) =
    new Decoder[T] {
      def apply(index: Int, row: ResultSet) = {
        fnFromString(row.getString(index + 1))
      }
    }
}