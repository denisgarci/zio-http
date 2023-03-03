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

package zio.http.middleware

import zio.stacktracer.TracingImplicits.disableAutoTrace
import zio.{Trace, ZIO}

import zio.http.{Http, HttpAppMiddleware, Middleware, Request, Response}

private[zio] trait HttpRoutesMiddlewares extends Cors {

  /**
   * Creates a middleware which can allow or disallow access to an http based on
   * the predicate
   */
  def allow(condition: Request => Boolean): HttpAppMiddleware[Any, Nothing] =
    Middleware.allow[Request, Response](condition)

  /**
   * Creates a middleware which can allow or disallow access to an http based on
   * the predicate effect
   */
  def allowZIO[R, Err](condition: Request => ZIO[R, Err, Boolean]): HttpAppMiddleware[R, Err] =
    Middleware.allowZIO[Request, Response](condition)

  /**
   * Removes the trailing slash from the path.
   */
  def dropTrailingSlash: HttpAppMiddleware[Any, Nothing] =
    new HttpAppMiddleware[Any, Nothing] {
      override def apply[R1 <: Any, Err1 >: Nothing](
        http: Http[R1, Err1, Request, Response],
      )(implicit trace: Trace): Http[R1, Err1, Request, Response] =
        Http.fromHandlerZIO[Request] { request =>
          if (request.url.queryParams.isEmpty)
            http.runHandler(request.dropTrailingSlash)
          else
            http.runHandler(request)
        }
    }
}

object HttpRoutesMiddlewares extends HttpRoutesMiddlewares
