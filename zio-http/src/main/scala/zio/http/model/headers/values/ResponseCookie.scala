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

package zio.http.model.headers.values

import zio.http.{CookieDecoder, Response, model}

sealed trait ResponseCookie

object ResponseCookie {
  final case class CookieValue(value: model.Cookie[Response]) extends ResponseCookie
  final case class InvalidCookieValue(error: Exception)       extends ResponseCookie

  def toCookie(value: String): zio.http.model.headers.values.ResponseCookie = {
    implicit val decoder = CookieDecoder.ResponseCookieDecoder
    model.Cookie.decode(value) match {
      case Left(value)  => InvalidCookieValue(value)
      case Right(value) => CookieValue(value)
    }
  }

  def fromCookie(cookie: ResponseCookie): String = cookie match {
    case CookieValue(value)    =>
      value.encode.getOrElse("")
    case InvalidCookieValue(_) => ""
  }
}
