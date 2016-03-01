package io.gustavoamigo.quill.pgsql.encoding.range.datetime

import io.getquill.source.jdbc.JdbcSource

trait DateTimeRangeSupport extends Decoders with Encoders {
  this: JdbcSource[_, _] =>
}