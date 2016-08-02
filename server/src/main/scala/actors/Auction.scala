package actors

import java.time.ZonedDateTime

import model.Auction._
import akka.actor.{Actor, ActorLogging}
import model.{Tariff, TariffRejected, TariffSaved}


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





class FeesActor extends Actor with ActorLogging {
  var prevTariff = Tariff(0,0,0, ZonedDateTime.now())

  def receive: Receive = {
    case newTariff : Tariff => {
      log.info(s"Tariff comes.")

      if (prevTariff.activeStarting.isBefore(newTariff.activeStarting)) {
        prevTariff = newTariff
        sender ! TariffSaved("New Tariff is saved")
        log.info(s"New Tariff is saved.")
      }
      else {
        sender ! TariffRejected( "newTariff activeStarting is befor then activeStarting activeStarting ")
        log.info(s"newTariff activeStarting is befor then activeStarting activeStarting ")
      }


    }
//    case Bid(userId, bid) => {
//      log.info(s"Bid ($userId, $bid)")
//      bidss = Bids(Bid(userId, bid) :: bidss.bids)
//    }
  }
}
