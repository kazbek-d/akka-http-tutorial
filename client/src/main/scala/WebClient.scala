import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, HttpResponse}
import akka.stream.ActorMaterializer

import scala.concurrent.Future
import scala.io.StdIn

object WebClient {
  def main(args: Array[String]): Unit = {
    println("Client Start")

    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()

    val responseFuture: Future[HttpResponse] =
      Http().singleRequest(HttpRequest(GET, uri = "http://localhost:8081/auction"))

    println("press any key for exit")
    StdIn.readLine()
    println("Client End")
  }
}
