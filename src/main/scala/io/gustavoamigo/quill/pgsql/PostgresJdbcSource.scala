package io.gustavoamigo.quill.pgsql

import java.sql.{ResultSet, PreparedStatement}

import io.getquill.naming.NamingStrategy
import io.getquill.source.jdbc.JdbcSource
import io.getquill.source.sql.idiom.PostgresDialect

class PostgresJdbcSource[N <: NamingStrategy] extends JdbcSource[PostgresDialect, N] {

  private def encoder[T](f: PreparedStatement => (Int, T) => Unit): Encoder[T] =
    new Encoder[T] {
      override def apply(index: Int, value: T, row: PreparedStatement) = {
        f(row)(index + 1, value)
        row
      }
    }

  private def decoder[T](f: ResultSet => Int => T): Decoder[T] =
    new Decoder[T] {
      def apply(index: Int, row: ResultSet) =
        f(row)(index + 1)
    }

  implicit val timestampDecoder = decoder(_.getTimestamp)
  implicit val timestampEncoder = encoder(_.setTimestamp)

}
