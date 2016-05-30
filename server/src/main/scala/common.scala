package common

import java.net.InetAddress

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer


object implicits {
  private val actorSystemName: String = "my-system"

  implicit val system = ActorSystem(actorSystemName)
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
}
