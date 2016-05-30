package routs

import akka.actor.Props
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout
import model.{Auction => m}
import spray.json.DefaultJsonProtocol._

import scala.concurrent.Future
import scala.concurrent.duration._

object auction {

  implicit val bidFormat = jsonFormat2(m.Bid)
  implicit val bidsFormat = jsonFormat1(m.Bids)

  import common.implicits._

  private val auction = system.actorOf(Props[actors.Auction], "auction")
  val auction_route =
    path("auction") {
      put {
        parameter("bid".as[Int], "user") { (bid, user) =>
          auction ! m.Bid(user, bid)
          complete((StatusCodes.Accepted, "bid placed"))
        }
      } ~
        get {
          implicit val timeout: Timeout = 5.seconds

          val bids: Future[m.Bids] = (auction ? m.GetBids).mapTo[m.Bids]
          complete(bids)
        } ~
        post {
          parameter("bid".as[Int], "user") { (bid, user) =>
            auction ! m.Bid(user, bid)
            complete((StatusCodes.Accepted, "bid placed"))
          }
        }
    }

}
