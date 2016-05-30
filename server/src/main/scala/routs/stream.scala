package routs

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.stream.scaladsl.Source
import akka.util.ByteString

import scala.util.Random

object stream {
  private val numbers = Source.fromIterator(() => Iterator.continually(Random.nextInt()))
  val randomPath = path("random") {
    get {
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, numbers.map(n => ByteString(s"$n\n"))))
    }
  }
}
