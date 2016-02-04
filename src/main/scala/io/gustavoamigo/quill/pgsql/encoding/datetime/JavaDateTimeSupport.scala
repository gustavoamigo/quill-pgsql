package io.gustavoamigo.quill.pgsql.encoding.datetime

import io.getquill.source.jdbc.JdbcSource

trait JavaDateTimeSupport extends Decoders with Encoders{
  this: JdbcSource[_, _] =>
}
