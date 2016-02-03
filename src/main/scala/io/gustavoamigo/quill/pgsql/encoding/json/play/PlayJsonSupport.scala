package io.gustavoamigo.quill.pgsql.encoding.json.play

import io.getquill.source.jdbc.JdbcSource

trait PlayJsonSupport extends JsonDecoder with JsonEncoder {
  this: JdbcSource[_, _] =>
}
