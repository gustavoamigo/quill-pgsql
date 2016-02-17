package io.gustavoamigo.quill.pgsql.encoding.range.numeric

import io.getquill.source.jdbc.JdbcSource

trait NumericRangeSupport extends Decoders with Encoders {
  this: JdbcSource[_, _] =>
}
