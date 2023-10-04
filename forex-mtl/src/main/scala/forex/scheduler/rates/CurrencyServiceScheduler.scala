package forex.scheduler.rates

import akka.actor.{Actor, ActorLogging, Props}
import forex.scheduler.rates.CurrencyServiceScheduler.FetchCurrencyRates

class CurrencyServiceScheduler extends Actor with ActorLogging {

  def fetchAndCacheCurrencyRates(): Unit = {
    log.info("Fetching and caching currency rates")
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
