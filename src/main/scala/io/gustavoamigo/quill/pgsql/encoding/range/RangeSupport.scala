package io.gustavoamigo.quill.pgsql.encoding.range

import io.getquill.source.jdbc.JdbcSource

trait RangeSupport  extends Decoders with Encoders {
  this: JdbcSource[_, _] =>
}
