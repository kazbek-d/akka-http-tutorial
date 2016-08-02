import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._

import scala.io.StdIn


object WebServer {

  def main(args: Array[String]): Unit = {
    println("Server Start 127.0.0.1:9009")


    val route = {
      import routs._
      hello.helloRoute ~ auction.actorRoute ~ stream.streamRoute ~ auth.authRoute ~ fee.feesRoute
    }

    import common.implicits._

    val bindingFuture = Http().bindAndHandle(route, "localhost", 9009)

    println("press any key for exit")
    StdIn.readLine()
    bindingFuture.flatMap(_ .unbind()).onComplete(_ => system.terminate())

    println("Server End")
  }

}
