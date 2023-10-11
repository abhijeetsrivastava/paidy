package forex

import akka.actor.ActorSystem
import cats.effect._
import forex.config.{ApplicationConfig, Config}
import forex.scheduler.rates.CurrencyServiceScheduler
import forex.scheduler.rates.CurrencyServiceScheduler.props
import org.http4s.server.blaze.BlazeServerBuilder
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.ExecutionContext

object Main extends IOApp {

  val log: Logger = LoggerFactory.getLogger("Main")

  override def run(args: List[String]): IO[ExitCode] = {
    implicit val config: ApplicationConfig =
      Config.stream[IO]("app").compile.lastOrError.unsafeRunSync()

    // Resource for managing the Actor System
    val actorSystemResource =
      Resource.make(IO(ActorSystem("CurrenyServiceSchedulerActor")))(sys => IO(sys.terminate()).void)

    actorSystemResource.use { actorSystem =>
      // Create the Actor
      val currencyActor = actorSystem.actorOf(props, "CurrenyServiceSchedulerActor")

      // Schedule periodic task
      actorSystem.scheduler.scheduleWithFixedDelay(
        config.scheduler.initialDelay,
        config.scheduler.interval,
        currencyActor,
        CurrencyServiceScheduler.FetchCurrencyRates
      )(actorSystem.dispatcher)

      // Keep the app running
      IO.never.as(ExitCode.Success)
    }

//    new Application[IO]
//      .server(ExecutionContext.global)
//      .compile.drain.as(ExitCode.Success)
//
  }

}

class Application[F[_]: ConcurrentEffect: Timer] {
  import fs2.Stream

  def server(ec: ExecutionContext): Stream[F, Unit] =
    for {
      config <- Config.stream("app")
      module = new Module[F](config)
      _ <- BlazeServerBuilder[F](ec)
            .bindHttp(config.http.port, config.http.host)
            .withHttpApp(module.httpApp)
            .serve
    } yield ()

}
