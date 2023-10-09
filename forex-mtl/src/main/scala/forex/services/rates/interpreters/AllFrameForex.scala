package forex.services.rates.interpreters

import forex.services.rates.Algebra
import cats.Applicative
import cats.syntax.applicative._
import cats.syntax.either._
import forex.domain.{ Price, Rate, Timestamp }
import forex.services.rates.errors._

class AllFrameForex[F[_]: Applicative] extends Algebra[F] {

  override def get(pairs: Rate.Pair): F[Error Either Rate] = {
    println("AllFrameForex.get")
    print(pairs)
    Rate(pairs, Price(BigDecimal(100)), Timestamp.now).asRight[Error].pure[F]
  }
}
