package forex.scheduler.rates

import akka.actor.{Actor, ActorLogging, Props}
import cats.effect.{ConcurrentEffect, ContextShift, IO}
import forex.config.{ApplicationConfig, Config, Forex}
import forex.domain.{Currency, Rate}
import forex.restclient.response.Response
import forex.restclient.response.RestClient.makeGetRequest
import forex.scheduler.rates.CurrencyServiceScheduler.FetchCurrencyRates

import scala.concurrent.ExecutionContext.global


class CurrencyServiceScheduler extends Actor with ActorLogging {

  // pair of all currency from domain.Currency
  val currencyPairs: List[Rate.Pair] = Currency.Values.flatMap(from => Currency.Values.map(Rate.Pair(from, _)))
  implicit val contextShift: ContextShift[IO] = IO.contextShift(global)
  implicit val concurrentEffect: ConcurrentEffect[IO] = IO.ioConcurrentEffect

  def fetchAndCacheCurrencyRates(): Unit = {
    log.info("Fetching and caching currency rates")

    val config: ApplicationConfig = Config.stream[IO]("app").compile.lastOrError.unsafeRunSync()
    log.info("Fetched config")

    val request: Forex = config.forex

    val uri = s"http://${request.host}:${request.port}/${request.endpoint}" +
      s"?pair=${currencyPairs.map(a => a.from.toString + "" + a.to).mkString("&pair=")}"

    val forexResponseList: List[Response.ForexResponse] = makeGetRequest[IO](uri, request.token).unsafeRunSync()
    // store the response in cache

    println(s"response is $uri $forexResponseList")
  }

  override def receive: Receive = {
    case FetchCurrencyRates => fetchAndCacheCurrencyRates()
  }

}

object CurrencyServiceScheduler {

  case object FetchCurrencyRates

  def props: Props = Props[CurrencyServiceScheduler]()

}
