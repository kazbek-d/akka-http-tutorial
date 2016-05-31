package routs

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._


object hello {
  private val helloMessage = "<h1>Akka-http hello actor</h1>"

  val helloRoute = pathPrefix("hello") {
    path("me" / IntNumber) { id =>
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "me: " + id))
      }
    } ~
      path("you" / IntNumber) { id =>
        get {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "you: " + id))
        }
      }
  } ~
    path("hello" / "blah") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, helloMessage + "blah blah blah"))
      }
    }
}
