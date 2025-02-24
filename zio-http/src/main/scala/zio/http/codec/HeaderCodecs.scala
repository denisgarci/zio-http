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

package zio.http.codec

import zio.http.model.HeaderNames
import zio.http.model.headers.values._
import zio.stacktracer.TracingImplicits.disableAutoTrace // scalafix:ok;

private[codec] trait HeaderCodecs {
  private[http] def header[A](name: String, value: TextCodec[A]): HeaderCodec[A] =
    HttpCodec.Header(name, value)

  final val accept: HeaderCodec[Accept] =
    header(HeaderNames.accept.toString(), TextCodec.string)
      .transform(Accept.toAccept, Accept.fromAccept)

  final val acceptEncoding: HeaderCodec[AcceptEncoding] =
    header(HeaderNames.acceptEncoding.toString(), TextCodec.string)
      .transform(AcceptEncoding.toAcceptEncoding, AcceptEncoding.fromAcceptEncoding)

  final val acceptLanguage: HeaderCodec[AcceptLanguage] =
    header(HeaderNames.acceptLanguage.toString(), TextCodec.string)
      .transform(AcceptLanguage.toAcceptLanguage, AcceptLanguage.fromAcceptLanguage)

  final val acceptRanges: HeaderCodec[AcceptRanges] =
    header(HeaderNames.acceptRanges.toString(), TextCodec.string)
      .transform(AcceptRanges.to, AcceptRanges.from)

  final val acceptPatch: HeaderCodec[AcceptPatch] =
    header(HeaderNames.acceptPatch.toString(), TextCodec.string)
      .transform(AcceptPatch.toAcceptPatch, AcceptPatch.fromAcceptPatch)

  final val accessControlAllowCredentials: HeaderCodec[AccessControlAllowCredentials] =
    header(HeaderNames.accessControlAllowCredentials.toString, TextCodec.string)
      .transform(
        AccessControlAllowCredentials.toAccessControlAllowCredentials,
        AccessControlAllowCredentials.fromAccessControlAllowCredentials,
      )

  final val accessControlAllowHeaders: HeaderCodec[AccessControlAllowHeaders] =
    header(HeaderNames.accessControlAllowHeaders.toString, TextCodec.string)
      .transform(
        AccessControlAllowHeaders.toAccessControlAllowHeaders,
        AccessControlAllowHeaders.fromAccessControlAllowHeaders,
      )

  final val accessControlAllowMethods: HeaderCodec[AccessControlAllowMethods] =
    header(HeaderNames.accessControlAllowMethods.toString, TextCodec.string)
      .transform(
        AccessControlAllowMethods.toAccessControlAllowMethods,
        AccessControlAllowMethods.fromAccessControlAllowMethods,
      )

  final val accessControlAllowOrigin: HeaderCodec[AccessControlAllowOrigin] =
    header(HeaderNames.accessControlAllowOrigin.toString, TextCodec.string)
      .transform(
        AccessControlAllowOrigin.toAccessControlAllowOrigin,
        AccessControlAllowOrigin.fromAccessControlAllowOrigin,
      )

  final val accessControlExposeHeaders: HeaderCodec[AccessControlExposeHeaders] =
    header(HeaderNames.accessControlExposeHeaders.toString(), TextCodec.string)
      .transform(
        AccessControlExposeHeaders.toAccessControlExposeHeaders,
        AccessControlExposeHeaders.fromAccessControlExposeHeaders,
      )

  final val accessControlMaxAge: HeaderCodec[AccessControlMaxAge] =
    header(HeaderNames.accessControlMaxAge.toString, TextCodec.string)
      .transform[AccessControlMaxAge](
        AccessControlMaxAge.toAccessControlMaxAge,
        AccessControlMaxAge.fromAccessControlMaxAge,
      )

  final val accessControlRequestHeaders: HeaderCodec[AccessControlRequestHeaders] =
    header(HeaderNames.accessControlRequestHeaders.toString(), TextCodec.string)
      .transform(
        AccessControlRequestHeaders.toAccessControlRequestHeaders,
        AccessControlRequestHeaders.fromAccessControlRequestHeaders,
      )

  final val accessControlRequestMethod: HeaderCodec[AccessControlRequestMethod] =
    header(HeaderNames.accessControlRequestMethod.toString(), TextCodec.string)
      .transform(
        AccessControlRequestMethod.toAccessControlRequestMethod,
        AccessControlRequestMethod.fromAccessControlRequestMethod,
      )

  final val age: HeaderCodec[Age] =
    header(HeaderNames.age.toString(), TextCodec.string).transform(Age.toAge, Age.fromAge)

  final val allow: HeaderCodec[Allow] =
    header(HeaderNames.allow.toString(), TextCodec.string)
      .transform[Allow](Allow.toAllow, Allow.fromAllow)

  final val authorization: HeaderCodec[Authorization] =
    header(HeaderNames.authorization.toString(), TextCodec.string)
      .transform(Authorization.toAuthorization, Authorization.fromAuthorization)

  final val cacheControl: HeaderCodec[CacheControl] =
    header(HeaderNames.cacheControl.toString(), TextCodec.string)
      .transform[CacheControl](CacheControl.toCacheControl, CacheControl.fromCacheControl)

  final val connection: HeaderCodec[Connection] = header(HeaderNames.connection.toString(), TextCodec.string)
    .transform[Connection](Connection.toConnection, Connection.fromConnection)

  final val contentBase: HeaderCodec[ContentBase] =
    header(HeaderNames.contentBase.toString, TextCodec.string)
      .transform(ContentBase.toContentBase, ContentBase.fromContentBase)

  final val contentEncoding: HeaderCodec[ContentEncoding] =
    header(HeaderNames.contentEncoding.toString, TextCodec.string)
      .transform[ContentEncoding](ContentEncoding.toContentEncoding, ContentEncoding.fromContentEncoding)

  final val contentLanguage: HeaderCodec[ContentLanguage] =
    header(HeaderNames.contentLanguage.toString, TextCodec.string)
      .transform[ContentLanguage](ContentLanguage.toContentLanguage, ContentLanguage.fromContentLanguage)

  final val contentLength: HeaderCodec[ContentLength] =
    header(HeaderNames.contentLength.toString, TextCodec.string)
      .transform(ContentLength.toContentLength, ContentLength.fromContentLength)

  final val contentLocation: HeaderCodec[ContentLocation] =
    header(HeaderNames.contentLocation.toString, TextCodec.string)
      .transform(ContentLocation.toContentLocation, ContentLocation.fromContentLocation)

  final val contentTransferEncoding: HeaderCodec[ContentTransferEncoding] =
    header(HeaderNames.contentTransferEncoding.toString, TextCodec.string)
      .transform[ContentTransferEncoding](
        ContentTransferEncoding.toContentTransferEncoding,
        ContentTransferEncoding.fromContentTransferEncoding,
      )

  final val contentDisposition: HeaderCodec[ContentDisposition] =
    header(HeaderNames.contentDisposition.toString, TextCodec.string)
      .transform[ContentDisposition](
        ContentDisposition.toContentDisposition,
        ContentDisposition.fromContentDisposition,
      )

  final val contentMd5: HeaderCodec[ContentMd5] =
    header(HeaderNames.contentMd5.toString, TextCodec.string)
      .transform[ContentMd5](ContentMd5.toContentMd5, ContentMd5.fromContentMd5)

  final val contentRange: HeaderCodec[ContentRange] =
    header(HeaderNames.contentRange.toString, TextCodec.string)
      .transform[ContentRange](ContentRange.toContentRange, ContentRange.fromContentRange)

  final val contentSecurityPolicy: HeaderCodec[ContentSecurityPolicy] =
    header(HeaderNames.contentSecurityPolicy.toString, TextCodec.string)
      .transform[ContentSecurityPolicy](
        ContentSecurityPolicy.toContentSecurityPolicy,
        ContentSecurityPolicy.fromContentSecurityPolicy,
      )

  final val contentType: HeaderCodec[ContentType] =
    header(HeaderNames.contentType.toString, TextCodec.string)
      .transform(ContentType.toContentType, ContentType.fromContentType)

  final val cookie: HeaderCodec[RequestCookie] = header(HeaderNames.cookie.toString(), TextCodec.string).transform(
    RequestCookie.toCookie,
    RequestCookie.fromCookie,
  )

  final val date: HeaderCodec[Date] = header(HeaderNames.date.toString(), TextCodec.string)
    .transform(Date.toDate, Date.fromDate)

  final val dnt: HeaderCodec[DNT] = header(HeaderNames.dnt.toString(), TextCodec.string)
    .transform(DNT.toDNT(_), DNT.fromDNT(_))

  final val etag: HeaderCodec[ETag] = header(HeaderNames.etag.toString, TextCodec.string)
    .transform(ETag.toETag, ETag.fromETag)

  final val expect: HeaderCodec[Expect] =
    header(HeaderNames.expect.toString, TextCodec.string)
      .transform(Expect.toExpect, Expect.fromExpect)

  final val expires: HeaderCodec[Expires] =
    header(HeaderNames.expires.toString, TextCodec.string).transform[Expires](Expires.toExpires, Expires.fromExpires)

  final val from: HeaderCodec[From] = header(HeaderNames.from.toString, TextCodec.string)
    .transform(From.toFrom, From.fromFrom)

  final val host: HeaderCodec[Host] = header(HeaderNames.host.toString, TextCodec.string)
    .transform(Host.toHost, Host.fromHost)

  final val ifMatch: HeaderCodec[IfMatch] = header(HeaderNames.ifMatch.toString, TextCodec.string)
    .transform(IfMatch.toIfMatch, IfMatch.fromIfMatch)

  final val ifModifiedSince: HeaderCodec[IfModifiedSince] =
    header(HeaderNames.ifModifiedSince.toString, TextCodec.string)
      .transform[IfModifiedSince](
        IfModifiedSince.toIfModifiedSince,
        IfModifiedSince.fromIfModifiedSince,
      )

  final val ifNoneMatch: HeaderCodec[IfNoneMatch] =
    header(HeaderNames.ifNoneMatch.toString(), TextCodec.string).transform(
      IfNoneMatch.toIfNoneMatch,
      IfNoneMatch.fromIfNoneMatch,
    )

  final val ifRange: HeaderCodec[IfRange] =
    header(HeaderNames.ifRange.toString, TextCodec.string)
      .transform(IfRange.toIfRange, IfRange.fromIfRange)

  final val ifUnmodifiedSince: HeaderCodec[IfUnmodifiedSince] =
    header(HeaderNames.ifUnmodifiedSince.toString(), TextCodec.string)
      .transform(
        IfUnmodifiedSince.toIfUnmodifiedSince,
        IfUnmodifiedSince.fromIfUnmodifiedSince,
      )

  final val lastModified: HeaderCodec[LastModified] =
    header(HeaderNames.lastModified.toString(), TextCodec.string)
      .transform(LastModified.toLastModified, LastModified.fromLastModified)

  final val location: HeaderCodec[Location] =
    header(HeaderNames.location.toString(), TextCodec.string).transform(Location.toLocation, Location.fromLocation)

  final val maxForwards: HeaderCodec[MaxForwards] =
    header(HeaderNames.maxForwards.toString(), TextCodec.string)
      .transform(MaxForwards.toMaxForwards(_), MaxForwards.fromMaxForwards(_))

  final val origin: HeaderCodec[Origin] =
    header(HeaderNames.origin.toString(), TextCodec.string)
      .transform(Origin.toOrigin, Origin.fromOrigin)

  final val pragma: HeaderCodec[Pragma] = header(HeaderNames.pragma.toString(), TextCodec.string)
    .transform(Pragma.toPragma, Pragma.fromPragma)

  final val proxyAuthenticate: HeaderCodec[ProxyAuthenticate] =
    header(HeaderNames.proxyAuthenticate.toString(), TextCodec.string)
      .transform(ProxyAuthenticate.toProxyAuthenticate, ProxyAuthenticate.fromProxyAuthenticate)

  final val proxyAuthorization: HeaderCodec[ProxyAuthorization] =
    header(HeaderNames.proxyAuthorization.toString(), TextCodec.string)
      .transform(ProxyAuthorization.toProxyAuthorization, ProxyAuthorization.fromProxyAuthorization)

  final val range: HeaderCodec[Range] = header(HeaderNames.range.toString(), TextCodec.string).transform(
    Range.toRange,
    Range.fromRange,
  )

  final val referer: HeaderCodec[Referer] = header(HeaderNames.referer.toString(), TextCodec.string)
    .transform(Referer.toReferer, Referer.fromReferer)

  final val retryAfter: HeaderCodec[RetryAfter] =
    header(HeaderNames.retryAfter.toString(), TextCodec.string)
      .transform(RetryAfter.toRetryAfter, RetryAfter.fromRetryAfter)

  final val secWebSocketLocation: HeaderCodec[SecWebSocketLocation] =
    header(HeaderNames.secWebSocketLocation.toString(), TextCodec.string)
      .transform(SecWebSocketLocation.toSecWebSocketLocation, SecWebSocketLocation.fromSecWebSocketLocation)

  final val secWebSocketOrigin: HeaderCodec[SecWebSocketOrigin] =
    header(HeaderNames.secWebSocketOrigin.toString(), TextCodec.string)
      .transform(SecWebSocketOrigin.toSecWebSocketOrigin, SecWebSocketOrigin.fromSecWebSocketOrigin)

  final val secWebSocketProtocol: HeaderCodec[SecWebSocketProtocol] =
    header(HeaderNames.secWebSocketProtocol.toString(), TextCodec.string).transform(
      SecWebSocketProtocol.toSecWebSocketProtocol,
      SecWebSocketProtocol.fromSecWebSocketProtocol,
    )

  final val secWebSocketVersion: HeaderCodec[SecWebSocketVersion] =
    header(HeaderNames.secWebSocketVersion.toString(), TextCodec.string).transform(
      SecWebSocketVersion.toSecWebSocketVersion,
      SecWebSocketVersion.fromSecWebSocketVersion,
    )

  final val secWebSocketKey: HeaderCodec[SecWebSocketKey] =
    header(HeaderNames.secWebSocketKey.toString(), TextCodec.string).transform(
      SecWebSocketKey.toSecWebSocketKey,
      SecWebSocketKey.fromSecWebSocketKey,
    )

  final val secWebSocketAccept: HeaderCodec[SecWebSocketAccept] =
    header(HeaderNames.secWebSocketAccept.toString(), TextCodec.string).transform(
      SecWebSocketAccept.toSecWebSocketAccept,
      SecWebSocketAccept.fromSecWebSocketAccept,
    )

  final val secWebSocketExtensions: HeaderCodec[SecWebSocketExtensions] =
    header(HeaderNames.secWebSocketExtensions.toString(), TextCodec.string).transform(
      SecWebSocketExtensions.toSecWebSocketExtensions,
      SecWebSocketExtensions.fromSecWebSocketExtensions,
    )

  final val server: HeaderCodec[Server] =
    header(HeaderNames.server.toString(), TextCodec.string).transform(Server.toServer, Server.fromServer)

  final val setCookie: HeaderCodec[ResponseCookie] = header(HeaderNames.setCookie.toString(), TextCodec.string)
    .transform(ResponseCookie.toCookie, ResponseCookie.fromCookie)

  final val te: HeaderCodec[Te] = header(HeaderNames.te.toString(), TextCodec.string).transform(
    Te.toTe,
    Te.fromTe,
  )

  final val trailer: HeaderCodec[Trailer] = header(HeaderNames.trailer.toString(), TextCodec.string)
    .transform(Trailer.toTrailer, Trailer.fromTrailer)

  final val transferEncoding: HeaderCodec[TransferEncoding] = header(
    HeaderNames.transferEncoding.toString(),
    TextCodec.string,
  ).transform(TransferEncoding.toTransferEncoding, TransferEncoding.fromTransferEncoding)

  final val upgrade: HeaderCodec[Upgrade] = header(HeaderNames.upgrade.toString(), TextCodec.string)
    .transform(Upgrade.toUpgrade, Upgrade.fromUpgrade)

  final val upgradeInsecureRequests: HeaderCodec[UpgradeInsecureRequests] =
    header(HeaderNames.upgradeInsecureRequests.toString(), TextCodec.string)
      .transform(UpgradeInsecureRequests.toUpgradeInsecureRequests, UpgradeInsecureRequests.fromUpgradeInsecureRequests)

  final val userAgent: HeaderCodec[UserAgent] =
    header(HeaderNames.userAgent.toString(), TextCodec.string).transform(UserAgent.toUserAgent, UserAgent.fromUserAgent)

  final val vary: HeaderCodec[Vary] = header(HeaderNames.vary.toString(), TextCodec.string)
    .transform(Vary.toVary, Vary.fromVary)

  final val via: HeaderCodec[Via] = header(HeaderNames.via.toString(), TextCodec.string).transform(
    Via.toVia,
    Via.fromVia,
  )

  final val warning: HeaderCodec[Warning] =
    header(HeaderNames.warning.toString(), TextCodec.string).transform[Warning](Warning.toWarning, Warning.fromWarning)

  final val webSocketLocation: HeaderCodec[SecWebSocketLocation] =
    header(HeaderNames.webSocketLocation.toString(), TextCodec.string).transform(
      SecWebSocketLocation.toSecWebSocketLocation,
      SecWebSocketLocation.fromSecWebSocketLocation,
    )

  final val webSocketOrigin: HeaderCodec[SecWebSocketOrigin] =
    header(HeaderNames.webSocketOrigin.toString(), TextCodec.string).transform(
      SecWebSocketOrigin.toSecWebSocketOrigin,
      SecWebSocketOrigin.fromSecWebSocketOrigin,
    )

  final val webSocketProtocol: HeaderCodec[SecWebSocketProtocol] =
    header(HeaderNames.webSocketProtocol.toString(), TextCodec.string).transform(
      SecWebSocketProtocol.toSecWebSocketProtocol,
      SecWebSocketProtocol.fromSecWebSocketProtocol,
    )

  final val wwwAuthenticate: HeaderCodec[WWWAuthenticate] =
    header(HeaderNames.wwwAuthenticate.toString(), TextCodec.string).transform(
      WWWAuthenticate.toWWWAuthenticate,
      WWWAuthenticate.fromWWWAuthenticate,
    )

  final val xFrameOptions: HeaderCodec[XFrameOptions] =
    header(HeaderNames.xFrameOptions.toString(), TextCodec.string).transform(
      XFrameOptions.toXFrameOptions,
      XFrameOptions.fromXFrameOptions,
    )

  final val xRequestedWith: HeaderCodec[XRequestedWith] =
    header(HeaderNames.xRequestedWith.toString(), TextCodec.string).transform(
      XRequestedWith.toXRequestedWith,
      XRequestedWith.fromXRequestedWith,
    )
}
