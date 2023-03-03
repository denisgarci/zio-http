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

package zio.http.model.headers

/**
 * List of commonly use HeaderValues. They are provided to reduce bugs caused by
 * typos and also to improve performance. `HeaderValues` arent encoded everytime
 * one needs to send them over the wire.
 */
trait HeaderValues {
  val applicationJson: CharSequence
  val applicationXWWWFormUrlencoded: CharSequence
  val applicationOctetStream: CharSequence
  val applicationXhtml: CharSequence
  val applicationXml: CharSequence
  val applicationZstd: CharSequence
  val attachment: CharSequence
  val base64: CharSequence
  val binary: CharSequence
  val boundary: CharSequence
  val bytes: CharSequence
  val charset: CharSequence
  val chunked: CharSequence
  val close: CharSequence
  val compress: CharSequence
  val continue: CharSequence
  val deflate: CharSequence
  val xDeflate: CharSequence
  val file: CharSequence
  val filename: CharSequence
  val formData: CharSequence
  val gzip: CharSequence
  val br: CharSequence
  val zstd: CharSequence
  val gzipDeflate: CharSequence
  val xGzip: CharSequence
  val identity: CharSequence
  val keepAlive: CharSequence
  val maxAge: CharSequence
  val maxStale: CharSequence
  val minFresh: CharSequence
  val multipartFormData: CharSequence
  val multipartMixed: CharSequence
  val mustRevalidate: CharSequence
  val name: CharSequence
  val noCache: CharSequence
  val noStore: CharSequence
  val noTransform: CharSequence
  val none: CharSequence
  val zero: CharSequence
  val onlyIfCached: CharSequence
  val `private`: CharSequence
  val proxyRevalidate: CharSequence
  val public: CharSequence
  val quotedPrintable: CharSequence
  val sMaxAge: CharSequence
  val textCss: CharSequence
  val textHtml: CharSequence
  val textEventStream: CharSequence
  val textPlain: CharSequence
  val trailers: CharSequence
  val upgrade: CharSequence
  val webSocket: CharSequence
  val xmlHttpRequest: CharSequence
}
