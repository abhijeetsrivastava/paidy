package forex.config

import scala.concurrent.duration.FiniteDuration

case class ApplicationConfig(
    http: HttpConfig,
    schedulerConfig: SchedulerConfig
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