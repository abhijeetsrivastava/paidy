package forex.restclient.response

case class ForexResponseList(pairs: List[ForexResponse])
case class ForexResponse(
    from: String,
    to: String,
    bid: BigDecimal,
    ask: BigDecimal,
    price: BigDecimal,
    time_stamp: String
)
