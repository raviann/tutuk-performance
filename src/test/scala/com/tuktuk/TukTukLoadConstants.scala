package com.tuktuk

import java.io.{InputStream, FileInputStream, InputStreamReader}
import java.util.UUID

import akka.actor.{ActorSystem, ActorRef}
import io.gatling.core.Predef.StringBody
import io.gatling.core.config.Resource

import scala.collection.immutable.HashMap

object TukTukLoadConstants {

  val tutukUrl = s"http://localhost:8080/drivers"
  val publishLocationUrl = tutukUrl + "/${driverId}/location"
  val getDriversUrl = tutukUrl + "?latitude=${lat}&longitude=${long}&radius=500&limit=5"
  
  val driverIds:List[Int]= List(234,567,100,222,4000, 345, 900)
  val latLongTuples:List[(Double,Double)] = List((12.97161923, 108.59463452), 
                                                (12.97161913, 108.59463442),
                                                (12.97171913, 108.59453442),
                                                (12.97171, 108.5945))
                                                
  
  val bodyForLocationPublication = StringBody(
    """
      |{
      |  "latitude": ${lat},
      |  "longitude": ${long},
      |  "accuracy": 0.8
      |}
    """.stripMargin)
    
    val noOfPublishers = 20
    val rampupDurationForUsers = 10
}