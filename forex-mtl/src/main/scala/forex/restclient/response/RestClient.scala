package forex.restclient.response

import cats.effect.{ConcurrentEffect, ContextShift, IO}
import forex.restclient.response.Response.ForexResponse
import org.http4s._
import org.http4s.circe.jsonOf
import org.http4s.client.blaze.BlazeClientBuilder

import scala.concurrent.ExecutionContext.global

object RestClient {

  case class TokenQueryParam(token: String)

  implicit val tokenQueryParamEncoder: QueryParamEncoder[TokenQueryParam] =
    QueryParamEncoder[String].contramap(_.token)

//  def makeGetRequest[F[_]: ConcurrentEffect](url: String, token: String): F[ForexResponseList] = {
//    BlazeClientBuilder[F](global).resource.use { client =>
//      val uri = Uri.fromString(url).getOrElse(throw new Exception(s"Invalid URL $url"))
//
//      val request = Request[F](Method.GET, uri, headers = Headers.of(Header("token", s"$token")))
//
//      client.expect[ForexResponseList](request)(jsonOf[F, ForexResponseList])
//    }
//  }


  def makeGetRequest[F[_] : ConcurrentEffect](url: String, token: String): F[List[ForexResponse]] = {
    BlazeClientBuilder[F](global).resource.use { client =>
      val uri = Uri.fromString(url).getOrElse(throw new Exception(s"Invalid URL $url"))

      val request = Request[F](Method.GET, uri, headers = Headers.of(Header("token", s"$token")))

      client.expect[List[ForexResponse]](request)(jsonOf[F, List[ForexResponse]])
    }
  }

  def main(args: Array[String]): Unit = {
    val uri = "http://localhost:8080/rates?pair=AUDAUD&pair=AUDCAD"
    val token = "10dc303535874aeccc86a8251e6992f5"
    implicit val contextShift: ContextShift[IO] = IO.contextShift(global)
    implicit val concurrentEffect: ConcurrentEffect[IO] = IO.ioConcurrentEffect

    val forexResponseList = makeGetRequest[IO](uri, token).unsafeRunSync()

    print(
      forexResponseList
    )

  }

}
