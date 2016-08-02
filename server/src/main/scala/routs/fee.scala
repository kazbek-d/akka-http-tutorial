package routs

import java.time.format.DateTimeFormatter
import java.time.{ZoneId, ZonedDateTime}

import akka.actor.Props
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout
import model.{Responce, Tariff, TariffRejected, TariffSaved}
import spray.json.DefaultJsonProtocol._
import spray.json._
import scala.concurrent.duration._

object fee {

  object ZonedDateTimeProtocol extends RootJsonFormat[ZonedDateTime] {

    val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.systemDefault)

    def write(obj: ZonedDateTime): JsValue = {
      JsString(formatter.format(obj))
    }

    def read(json: JsValue): ZonedDateTime = json match {
      case JsString(s) => try {
        ZonedDateTime.parse(s, formatter)
      } catch {
        case t: Throwable => error(s)
      }
      case _ =>
        error(json.toString())
    }

    def error(v: Any): ZonedDateTime = {
      val example = formatter.format(ZonedDateTime.now())
      deserializationError(f"'$v' is not a valid date value. Dates must be in compact ISO-8601 format, e.g. '$example'")
    }
  }


  implicit val dateTimeFormat = ZonedDateTimeProtocol
  implicit val tariffFormat = jsonFormat4(Tariff)

  import common.implicits._

  private val feesActor = system.actorOf(Props[actors.FeesActor], "fees")
  val feesRoute =
    path("tariff") {
      post {
        entity(as[Tariff]) { tarrif =>
          if (tarrif.activeStarting.isBefore(ZonedDateTime.now()))
            complete((StatusCodes.BadRequest, s"${tarrif.activeStarting} have to be > ${ZonedDateTime.now()}"))
          else {

            implicit val timeout: Timeout = 5.seconds

            val response = (feesActor ? tarrif).mapTo[Responce]

            complete(response.map {
              case TariffSaved(message) => (StatusCodes.Accepted, "tarrif is placed")
              case TariffRejected(message) => (StatusCodes.BadRequest, "tarrif is rekected")
            }
            )
          }

        }
      }

    }

}
