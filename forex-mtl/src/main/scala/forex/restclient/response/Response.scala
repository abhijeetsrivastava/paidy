package forex.restclient.response

import io.circe.Decoder
import io.circe.generic.extras.Configuration
import io.circe.generic.semiauto.deriveDecoder

import java.time.OffsetDateTime

object Response {
  implicit val configuration: Configuration = Configuration.default.withSnakeCaseMemberNames

  implicit val ForexResponseListDecoder: Decoder[ForexResponseList] = deriveDecoder[ForexResponseList]

  case class ForexResponseList(pairs: List[ForexResponse])

  implicit val ForexResponseDecoder: Decoder[ForexResponse] = deriveDecoder[ForexResponse]
//  implicit val ListForexResponseDecoder: Decoder[List[ForexResponse]] = deriveDecoder[List[ForexResponse]]

  case class ForexResponse(
      from: String,
      to: String,
      bid: BigDecimal,
      ask: BigDecimal,
      price: BigDecimal,
      time_stamp: OffsetDateTime
  )
}
