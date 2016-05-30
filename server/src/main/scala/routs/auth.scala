package routs

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.model.headers.BasicHttpCredentials
import akka.http.scaladsl.server.Directives._

object auth {

  val authPath = path("auth") {
    extractCredentials { httpCredentials =>
      get {
        httpCredentials match {
          case Some(BasicHttpCredentials(user, pass)) =>
            complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"<h1>Hello $user</h1>"))
          case _ => complete(HttpResponse(401, entity = "Unauthorized"))
        }
      }
    }
  }

}
