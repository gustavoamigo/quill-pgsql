package io.gustavoamigo.quill.pgsql.encoding.jodatime

import io.getquill.source.jdbc.JdbcSource

trait JodaTimeSupport extends Decoders with Encoders {
  this: JdbcSource[_, _] =>

}
