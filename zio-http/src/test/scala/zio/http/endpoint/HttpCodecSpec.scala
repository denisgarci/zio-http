package zio.http.endpoint

import zio._
import zio.test._

import zio.http._
import zio.http.codec._
import zio.http.model._

object HttpCodecSpec extends ZIOSpecDefault {
  val googleUrl     = URL.fromString("http://google.com").toOption.get
  val usersUrl      = URL.fromString("http://mywebservice.com/users").toOption.get
  val usersIdUrl    = URL.fromString("http://mywebservice.com/users/42").toOption.get
  val postURL       = URL.fromString("http://mywebservice.com/users/42/post").toOption.get
  val postidURL     = URL.fromString("http://mywebservice.com/users/42/post/42").toOption.get
  val postidfontURL = URL.fromString("http://mywebservice.com/users/42/post/42/fontstyle").toOption.get

  val headerExample =
    Headers.contentType("application/json") ++ Headers("X-Trace-ID", "1234")

  val emptyJson = Body.fromString("{}")

  def spec = suite("HttpCodecSpec")(
    suite("fallback") {
      test("path fallback") {
        val usersURL = URL.fromString("http://mywebservice.com/users").toOption.get
        val postsURL = URL.fromString("http://mywebservice.com/posts").toOption.get

        val codec1 = PathCodec.literal("users")
        val codec2 = PathCodec.literal("posts")

        val fallback = codec1 | codec2

        for {
          result1 <- fallback.decodeRequest(Request.get(url = usersURL))
          result2 <- fallback.decodeRequest(Request.get(url = postsURL))
        } yield assertTrue(result1 == (())) && assertTrue(result2 == (()))
      } +
        test("query fallback") {
          val codec1 = QueryCodec.query("skip")
          val codec2 = QueryCodec.query("limit")

          val fallback = codec1 | codec2

          val skipRequest  = Request.get(url = usersUrl.copy(queryParams = QueryParams("skip" -> "10")))
          val limitRequest = Request.get(url = usersUrl.copy(queryParams = QueryParams("limit" -> "20")))

          for {
            result1 <- fallback.decodeRequest(skipRequest)
            result2 <- fallback.decodeRequest(limitRequest)
          } yield assertTrue(result1 == "10") && assertTrue(result2 == "20")
        } +
        test("header fallback") {
          val codec1 = HeaderCodec.header("Authentication", TextCodec.string)
          val codec2 = HeaderCodec.header("X-Token-ID", TextCodec.string)

          val fallback = codec1 | codec2

          val authRequest  = Request.get(url = usersUrl).copy(headers = Headers("Authentication" -> "1234"))
          val tokenRequest = Request.get(url = usersUrl).copy(headers = Headers("X-Token-ID" -> "5678"))

          for {
            result1 <- fallback.decodeRequest(authRequest)
            result2 <- fallback.decodeRequest(tokenRequest)
          } yield assertTrue(result1 == "1234") && assertTrue(result2 == "5678")
        } +
        test("composite fallback") {
          val usersURL = URL.fromString("http://mywebservice.com/users").toOption.get
          val postsURL = URL.fromString("http://mywebservice.com/posts").toOption.get

          val codec1 = PathCodec.literal("users") ++ QueryCodec.query("skip") ++ HeaderCodec.header(
            "Authentication",
            TextCodec.string,
          )
          val codec2 = PathCodec.literal("posts") ++ QueryCodec.query("limit") ++ HeaderCodec.header(
            "X-Token-ID",
            TextCodec.string,
          )

          val fallback = codec1 | codec2

          val usersRequest = Request
            .get(url = usersURL.copy(queryParams = QueryParams("skip" -> "10")))
            .copy(headers = Headers("Authentication" -> "1234"))
          val postsRequest = Request
            .get(url = postsURL.copy(queryParams = QueryParams("limit" -> "20")))
            .copy(headers = Headers("X-Token-ID" -> "567"))

          for {
            result1 <- fallback.decodeRequest(usersRequest)
            result2 <- fallback.decodeRequest(postsRequest)
          } yield assertTrue(result1 == (("10", "1234"))) && assertTrue(result2 == (("20", "567")))
        } +
        test("no fallback for defects") {
          val usersURL = URL.fromString("http://mywebservice.com/users").toOption.get
          val e        = new RuntimeException("boom")

          val codec1 = PathCodec.literal("users").transform[Unit](_ => throw e, _ => ()).const("route1")
          val codec2 = PathCodec.literal("users").const("route2")

          val fallback = codec1 | codec2

          for {
            result <- fallback.decodeRequest(Request.get(url = usersURL)).exit
          } yield assertTrue(result.causeOption.get.defects.forall(_ == e))
        }
    },
    suite("PathCodec") {
      test("decode route with one path segment") {
        val codec = PathCodec.literal("users")

        for {
          result <- codec.decodeRequest(Request.get(url = usersUrl))
        } yield assertTrue(result == (()))
      } +
        test("encode route with one path segment") {
          val codec = PathCodec.literal("users")

          val request = codec.encodeRequest(())

          assertTrue(request.path.toString() == "users")
        } +
        test("decode route with two path segments") {
          val userCodec = PathCodec.literal("users")
          val intCodec  = PathCodec.int("userId")

          // /users/<int id>
          val fullCodec = userCodec ++ intCodec

          for {
            result <- fullCodec.decodeRequest(Request.get(url = usersIdUrl))
          } yield assertTrue(result == 42)
        } +
        test("encode route with two path segments") {
          val userCodec = PathCodec.literal("users")
          val intCodec  = PathCodec.int("userId")

          // /users/<int id>
          val fullCodec = userCodec ++ intCodec

          val request = fullCodec.encodeRequest(42)

          assertTrue(request.path.toString() == "users/42")

          // test comment
        } +
        test("decode route with three path segments") {
          val userCodec = PathCodec.literal("users")
          val intCodec  = PathCodec.int("userId")
          val postCodec = PathCodec.literal("post")

          val fullCodec = userCodec ++ intCodec ++ postCodec

          for {
            result <- fullCodec.decodeRequest(Request.get(url = postURL))
          } yield assertTrue(result == 42)
        } +
        test("encode route with three path segments") {
          val userCodec = PathCodec.literal("users")
          val intCodec  = PathCodec.int("userId")
          val postCodec = PathCodec.literal("post")

          val fullCodec = userCodec ++ intCodec ++ postCodec

          val request = fullCodec.encodeRequest(42)

          assertTrue(request.path.toString() == "users/42/post")
        } +
        test("decode route with four path segments") {
          val userCodec   = PathCodec.literal("users")
          val intCodec    = PathCodec.int("userId")
          val postCodec   = PathCodec.literal("post")
          val postIdCodec = PathCodec.int("postId")

          val fullCodec = userCodec ++ intCodec ++ postCodec ++ postIdCodec
          for {
            result <- fullCodec.decodeRequest(Request.get(url = postidURL))
          } yield assertTrue(result == ((42, 42)))
        } +
        test("encode route with four path segments") {
          val userCodec   = PathCodec.literal("users")
          val intCodec    = PathCodec.int("userId")
          val postCodec   = PathCodec.literal("post")
          val postIdCodec = PathCodec.int("postId")

          val fullCodec = userCodec ++ intCodec ++ postCodec ++ postIdCodec

          val request = fullCodec.encodeRequest((42, 42))

          assertTrue(request.path.toString() == "users/42/post/42")
        } +
        test("decode route with five path segments") {
          val userCodec       = PathCodec.literal("users")
          val intCodec        = PathCodec.int("userId")
          val postCodec       = PathCodec.literal("post")
          val postIdCodec     = PathCodec.int("postId")
          val postIdfontCodec = PathCodec.literal("fontstyle")

          val fullCodec = userCodec ++ intCodec ++ postCodec ++ postIdCodec ++ postIdfontCodec
          for {
            result <- fullCodec.decodeRequest(Request.get(url = postidfontURL))
          } yield assertTrue(result == ((42, 42)))
        } +
        test("encode route with five path segments") {
          val userCodec       = PathCodec.literal("users")
          val intCodec        = PathCodec.int("userId")
          val postCodec       = PathCodec.literal("post")
          val postIdCodec     = PathCodec.int("postId")
          val postIdfontCodec = PathCodec.literal("fontstyle")

          val fullCodec = userCodec ++ intCodec ++ postCodec ++ postIdCodec ++ postIdfontCodec
          val request   = fullCodec.encodeRequest((42, 42))
          assertTrue(request.path.toString() == "users/42/post/42/fontstyle")
        }

    } +
      suite("HeaderCodec") {
        test("dummy test") {
          assertTrue(true)
        }
      } +
      suite("BodyCodec") {
        test("dummy test") {
          assertTrue(true)
        }
      } +
      suite("QueryCodec") {
        test("dummy test") {
          assertTrue(true)
        }
      },
  )
}
