package io.gustavoamigo.quill.pgsql

import java.sql.{ResultSet, PreparedStatement}

import io.getquill.naming.NamingStrategy
import io.getquill.source.jdbc.JdbcSource
import io.getquill.source.sql.idiom.PostgresDialect
import io.gustavoamigo.quill.pgsql.encoding.{Encoders, Decoders}

class PostgresJdbcSource[N <: NamingStrategy] extends JdbcSource[PostgresDialect, N] with Decoders with Encoders {

}
