package com.tuktuk

import io.gatling.app.Gatling
import io.gatling.core.Predef._
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.action.{Failable, Interruptable}
import io.gatling.core.config.GatlingPropertiesBuilder
import io.gatling.core.result.writer.StatsEngine
import io.gatling.core.session.Session
import io.gatling.core.structure.ScenarioContext
import io.gatling.core.validation.Validation
import io.gatling.http.Predef._
import io.gatling.http.config.HttpProtocolBuilder.toHttpProtocol
import io.gatling.http.request.builder.HttpRequestBuilder.toActionBuilder
import io.gatling.http.request.builder.ws.WsOpenRequestBuilder.toActionBuilder
import scala.concurrent.duration.DurationInt

import scala.collection.immutable.HashMap
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import akka.actor.ActorSystem
import com.tuktuk.TukTukLoadConstants._

class TukTukApiLoadTest extends Simulation  {
  
  
  def getRandomObj[T](list: List[T]): T = {
    val randCusIndex = new scala.util.Random
    val item = list(randCusIndex.nextInt(list.length))
    item
  }
  
  implicit val actorRefFactory: ActorSystem = ActorSystem("tuktuk-load")

  val feeder = new Feeder[String] {
    override val hasNext = true
    override def next: Map[String, String] = {
      val driverId = getRandomObj(driverIds)
      val latlong = getRandomObj(latLongTuples)

      Map(
        "driverId" -> driverId.toString(),
        "lat" -> latlong._1.toString(),
        "long" -> latlong._2.toString())
    }
  }
  
   val httpConf = http
    .baseURL(tutukUrl)
    .contentTypeHeader("application/json")
    .acceptHeader("application/json;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/35.0")
    
    
   /*API to publish messages to*/
  val publishLocation = {
    http("Driver location update")
      .put(publishLocationUrl)
      .body(bodyForLocationPublication).asJSON
      .check(status.is(200))
  } 

     /*API to publish messages to*/
  val getNearestNeighbors = {
    http("Nearest available drivers")
      .get(getDriversUrl)
      .check(status.is(200))
  } 

  
  val publishersScenario = {
    scenario("Publish driver location info").feed(feeder).repeat(100) {
      exec(publishLocation).pause(200 millis)
    }.inject(nothingFor(rampupDurationForUsers), atOnceUsers(noOfPublishers))
  }
  
  val nearestDriversScenario = {
    scenario("Get nearest drivers info").feed(feeder).repeat(100) {
      exec(getNearestNeighbors).pause(200 millis)
    }.inject(nothingFor(rampupDurationForUsers), atOnceUsers(noOfPublishers))
  }
  
  setUp(
    publishersScenario.protocols(httpConf),
    nearestDriversScenario.protocols(httpConf)
  )
}