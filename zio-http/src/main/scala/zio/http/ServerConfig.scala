/*
 * Copyright 2021 - 2023 Sporta Technologies PVT LTD & the ZIO HTTP contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package zio.http

import java.net.{InetAddress, InetSocketAddress}

import zio.stacktracer.TracingImplicits.disableAutoTrace
import zio.{Trace, ZLayer}

import zio.http.ServerConfig.ResponseCompressionConfig
import zio.http.netty.{ChannelType, EventLoopGroups}

final case class ServerConfig(
  sslConfig: Option[SSLConfig] = None,
  address: InetSocketAddress = new InetSocketAddress(8080),
  acceptContinue: Boolean = false,
  keepAlive: Boolean = true,
  consolidateFlush: Boolean = false,
  flowControl: Boolean = true,
  requestDecompression: Decompression = Decompression.No,
  responseCompression: Option[ResponseCompressionConfig] = None,
  objectAggregator: Int = 1024 * 100,
  channelType: ChannelType = ChannelType.AUTO,
  nThreads: Int = 0,
  maxHeaderSize: Int = 8192,
) extends EventLoopGroups.Config {
  self =>
  def useAggregator: Boolean = objectAggregator >= 0

  /**
   * Configure the server to use HttpServerExpectContinueHandler to send a 100
   * HttpResponse if necessary.
   */
  def acceptContinue(enable: Boolean): ServerConfig = self.copy(acceptContinue = enable)

  /**
   * Configure the server to listen on the provided hostname and port.
   */
  def binding(hostname: String, port: Int): ServerConfig =
    self.copy(address = new InetSocketAddress(hostname, port))

  /**
   * Configure the server to listen on the provided InetAddress and port.
   */
  def binding(address: InetAddress, port: Int): ServerConfig =
    self.copy(address = new InetSocketAddress(address, port))

  /**
   * Configure the server to listen on the provided InetSocketAddress.
   */
  def binding(inetSocketAddress: InetSocketAddress): ServerConfig = self.copy(address = inetSocketAddress)

  /**
   * Configure the server to use FlushConsolidationHandler to control the flush
   * operations in a more efficient way if enabled (@see <a
   * href="https://netty.io/4.1/api/io/netty/handler/flush/FlushConsolidationHandler.html">FlushConsolidationHandler<a>).
   */
  def consolidateFlush(enable: Boolean): ServerConfig = self.copy(consolidateFlush = enable)

  /**
   * Configure the server to use netty FlowControlHandler if enable (@see <a
   * href="https://netty.io/4.1/api/io/netty/handler/flow/FlowControlHandler.html">FlowControlHandler</a>).
   */
  def flowControl(enable: Boolean): ServerConfig = self.copy(flowControl = enable)

  /**
   * Configure the server to use netty's HttpServerKeepAliveHandler to close
   * persistent connections when enable is true (@see <a
   * href="https://netty.io/4.1/api/io/netty/handler/codec/http/HttpServerKeepAliveHandler.html">HttpServerKeepAliveHandler</a>).
   */
  def keepAlive(enable: Boolean): ServerConfig = self.copy(keepAlive = enable)

  /**
   * Configure the server to use HttpObjectAggregator with the specified max
   * size of the aggregated content.
   */
  def objectAggregator(maxRequestSize: Int = 1024 * 100): ServerConfig =
    self.copy(objectAggregator = maxRequestSize)

  /**
   * Configure the server to listen on the provided port.
   */
  def port(port: Int): ServerConfig = self.copy(address = new InetSocketAddress(port))

  /**
   * Configure the server to use netty's HttpContentDecompressor to decompress
   * Http requests (@see <a href =
   * "https://netty.io/4.1/api/io/netty/handler/codec/http/HttpContentDecompressor.html">HttpContentDecompressor</a>).
   */
  def requestDecompression(isStrict: Boolean): ServerConfig =
    self.copy(requestDecompression = if (isStrict) Decompression.Strict else Decompression.NonStrict)

  /**
   * Configure the new server with netty's HttpContentCompressor to compress
   * Http responses (@see <a href =
   * "https://netty.io/4.1/api/io/netty/handler/codec/http/HttpContentCompressor.html"HttpContentCompressor</a>).
   */
  def responseCompression(rCfg: ResponseCompressionConfig = ServerConfig.responseCompressionConfig()): ServerConfig =
    self.copy(responseCompression = Option(rCfg))

  /**
   * Configure the server with the following ssl options.
   */
  def ssl(sslConfig: SSLConfig): ServerConfig = self.copy(sslConfig = Some(sslConfig))

  /**
   * Configure the server to use a maximum of nThreads to process requests.
   */
  def maxThreads(nThreads: Int): ServerConfig = self.copy(nThreads = nThreads)

  /**
   * Configure the server to use `maxHeaderSize` value when encode/decode
   * headers.
   */
  def maxHeaderSize(headerSize: Int): ServerConfig = self.copy(maxHeaderSize = headerSize)
}

object ServerConfig {
  val default: ServerConfig = ServerConfig()

  val live: ZLayer[Any, Nothing, ServerConfig] = {
    implicit val trace = Trace.empty
    ZLayer.succeed(ServerConfig.default)
  }

  def live(config: ServerConfig)(implicit trace: Trace): ZLayer[Any, Nothing, ServerConfig] =
    ZLayer.succeed(config)

  private[http] def liveOnOpenPort(implicit trace: Trace): ZLayer[Any, Any, ServerConfig] =
    ZLayer.succeed(
      ServerConfig.default.port(0),
    )

  def responseCompressionConfig(
    contentThreshold: Int = 0,
    options: IndexedSeq[CompressionOptions] = IndexedSeq(CompressionOptions.gzip(), CompressionOptions.deflate()),
  ): ResponseCompressionConfig = ResponseCompressionConfig(contentThreshold, options)

  final case class ResponseCompressionConfig(
    contentThreshold: Int = 0,
    options: IndexedSeq[CompressionOptions] = IndexedSeq.empty,
  )

  /**
   * @param level
   *   defines compression level, {@code 1} yields the fastest compression and
   *   {@code 9} yields the best compression. {@code 0} means no compression.
   * @param bits
   *   defines windowBits, The base two logarithm of the size of the history
   *   buffer. The value should be in the range {@code 9} to {@code 15}
   *   inclusive. Larger values result in better compression at the expense of
   *   memory usage
   * @param mem
   *   defines memlevel, How much memory should be allocated for the internal
   *   compression state. {@code 1} uses minimum memory and {@code 9} uses
   *   maximum memory. Larger values result in better and faster compression at
   *   the expense of memory usage
   */
  final case class CompressionOptions(
    level: Int,
    bits: Int,
    mem: Int,
    kind: CompressionOptions.CompressionType,
  )

  object CompressionOptions {
    val DefaultLevel = 6
    val DefaultBits  = 15
    val DefaultMem   = 8

    /**
     * Creates GZip CompressionOptions. Defines defaults as per
     * io.netty.handler.codec.compression.GzipOptions#DEFAULT
     */
    def gzip(level: Int = DefaultLevel, bits: Int = DefaultBits, mem: Int = DefaultMem): CompressionOptions =
      CompressionOptions(level, bits, mem, GZip)

    /**
     * Creates Deflate CompressionOptions. Defines defaults as per
     * io.netty.handler.codec.compression.DeflateOptions#DEFAULT
     */
    def deflate(level: Int = DefaultLevel, bits: Int = DefaultBits, mem: Int = DefaultMem): CompressionOptions =
      CompressionOptions(level, bits, mem, Deflate)

    sealed trait CompressionType

    private[http] case object GZip extends CompressionType

    private[http] case object Deflate extends CompressionType
  }
}
