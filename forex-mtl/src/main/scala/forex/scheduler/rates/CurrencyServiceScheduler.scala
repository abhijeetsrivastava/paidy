package forex.scheduler.rates

import akka.actor.{Actor, ActorLogging, Props}
import forex.scheduler.rates.CurrencyServiceScheduler.FetchCurrencyRates

//import scala.concurrent.ExecutionContext.Implicits.global

class CurrencyServiceScheduler extends Actor with ActorLogging {

  def fetchAndCacheCurrencyRates(): Unit = {
    log.info("Fetching and caching currency rates")

    // use Module to get rates by calling ratesHttpRoutes

    println("fetching...")
  }

  override def receive: Receive = {
    case FetchCurrencyRates => fetchAndCacheCurrencyRates()
  }

}

object CurrencyServiceScheduler {

  case object FetchCurrencyRates

  def props: Props = Props[CurrencyServiceScheduler]()

}
