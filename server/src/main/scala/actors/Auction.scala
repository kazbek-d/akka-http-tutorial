package actors

import model.Auction._

import akka.actor.{Actor, ActorLogging}

class Auction extends Actor with ActorLogging {
  var bidss = Bids(List.empty)
  def receive: Receive = {
    case GetBids =>{
      log.info(s"GetBids comes.")
      sender ! bidss
    }
    case Bid(userId, bid) => {
      log.info(s"Bid ($userId, $bid)")
      bidss = Bids(Bid(userId, bid) :: bidss.bids)
    }
  }
}
