package io.gustavoamigo.quill.pgsql

import java.sql.{ResultSet, PreparedStatement}

import io.getquill.naming.NamingStrategy
import io.getquill.source.jdbc.JdbcSource
import io.getquill.source.sql.idiom.PostgresDialect
import io.gustavoamigo.quill.pgsql.encoding.datetime.JavaDateTimeSupport

class PostgresJdbcSource[N <: NamingStrategy] extends JdbcSource[PostgresDialect, N] with JavaDateTimeSupport {

}
