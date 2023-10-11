package forex.restclient.response

import cats.effect.ConcurrentEffect
import org.http4s._
import org.http4s.client.blaze.BlazeClientBuilder

import scala.concurrent.ExecutionContext.global

object RestClient {

  case class TokenQueryParam(token: String)

  implicit val tokenQueryParamEncoder: QueryParamEncoder[TokenQueryParam] =
    QueryParamEncoder[String].contramap(_.token)

  def makeGetRequest[F[_]: ConcurrentEffect](url: String, token: String): F[String] = {
    BlazeClientBuilder[F](global).resource.use { client =>
      val uri = Uri.fromString(url).getOrElse(throw new Exception(s"Invalid URL $url"))

      val request = Request[F](Method.GET, uri, headers = Headers.of(Header("token", s"$token")))

      print(request.toString())
      client.expect[String](request)
    }
  }

}
