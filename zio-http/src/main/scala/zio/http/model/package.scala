package zio.http

import java.nio.charset.{Charset, StandardCharsets}

import zio.stacktracer.TracingImplicits.disableAutoTrace

import zio.http.model.headers._
import zio.http.netty.model.headers.{NettyHeaderNames, NettyHeaderValues}

package object model {
  type Header = Headers.Header
  val Header: Headers.Header.type = Headers.Header

  /**
   * Default HTTP Charset
   */
  val HTTP_CHARSET: Charset = StandardCharsets.UTF_8

  object HeaderNames extends HeaderNames with NettyHeaderNames

  object HeaderValues extends HeaderValues with NettyHeaderValues
}
