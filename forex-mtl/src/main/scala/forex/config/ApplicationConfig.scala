package forex.config

import scala.concurrent.duration.FiniteDuration

case class ApplicationConfig(
    http: HttpConfig,
    scheduler: SchedulerConfig,
    forex: Forex
)

case class HttpConfig(
    host: String,
    port: Int,
    timeout: FiniteDuration
)

case class SchedulerConfig(
    initialDelay: FiniteDuration,
    interval: FiniteDuration
)

case class Forex(
    host: String,
    port: Int,
    timeout: FiniteDuration,
    endpoint: String,
    token: String
)
