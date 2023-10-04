package forex

import akka.actor.ActorSystem
import cats.effect._
import forex.scheduler.rates.CurrencyServiceScheduler
import forex.scheduler.rates.CurrencyServiceScheduler.props

import scala.concurrent.duration._

object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    // Resource for managing the Actor System
    val actorSystemResource = Resource.make(IO(ActorSystem("CurrenyServiceSchedulerActor")))(sys => IO(sys.terminate()).void)

    actorSystemResource.use {
        actorSystem =>
            // Create the Actor
            val currencyActor = actorSystem.actorOf(props, "CurrenyServiceSchedulerActor")

            // Schedule periodic task
            actorSystem.scheduler.scheduleWithFixedDelay(
                0.seconds,
                5.seconds,
                currencyActor,
                CurrencyServiceScheduler.FetchCurrencyRates
            )(actorSystem.dispatcher)

             // Keep the app running
            IO.never.as(ExitCode.Success)
    }

  }

}

// unused code
//class Application[F[_]: ConcurrentEffect: Timer] {
//
//  def stream(ec: ExecutionContext): Stream[F, Unit] =
//    for {
//      config <- Config.stream("app")
//      module = new Module[F](config)
//      _ <- BlazeServerBuilder[F](ec)
//            .bindHttp(config.http.port, config.http.host)
//            .withHttpApp(module.httpApp)
//            .serve
//    } yield ()
//
//}
