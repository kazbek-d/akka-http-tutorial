package model

object Auction{
  case class Bid(userId: String, bid: Int)
  case object GetBids
  case class Bids(bids: List[Bid])
}