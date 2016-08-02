package model

import java.time.ZonedDateTime

object Auction{
  case class Bid(userId: String, bid: Int)
  case object GetBids
  case class Bids(bids: List[Bid])
}







case class Tariff(startFee: BigDecimal, hourlyFee: BigDecimal, feePerKWh: BigDecimal, activeStarting: ZonedDateTime)

trait Responce
case class TariffSaved(message: String) extends Responce
case class TariffRejected(message: String) extends Responce