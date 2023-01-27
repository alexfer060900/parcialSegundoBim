import kantan.csv._
import kantan.csv.ops._
import kantan.csv.generic._

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

import java.io.File

import java.time.LocalDate
import java.time.format.DateTimeFormatter


import play.api.libs.json._
import scala.util.{Failure, Success, Try}
import scala.util.matching.Regex

import requests.Response


import scalikejdbc._

object Main extends App {


  val rawData = "C:/Users/kevin/Desktop/ProyectoIntegrador/data_parcial_2_OO.csv"

  case class data(
                   tourney_id: String,
                   tourney_name: String,
                   surface: String,
                   draw_size: String,
                   tourney_level: String,
                   tourney_date: String,
                   match_num: String,
                   players_info: String,
                   match_info: String
                 )

  val dataSource = new File(rawData).readCsv[List, data](rfc.withHeader(true).withCellSeparator(','))
  val values = dataSource.collect({ case Right(data) => data })



  val datos = values
    .map(x => (x.tourney_name, x.players_info, x.match_info))
    .map(j1 => (j1._1, Try(Json.parse(j1._2)), Try(Json.parse(j1._3))))
    .filter(z => (z._2.isSuccess)).filter(z => z._3.isSuccess)
    .map(x => (x._1, x._2.get, x._3.get))
    .map(x => (x._1, x._2.as[JsArray].value.map(_.as[JsObject]), x._3.as[zJsObject]))
    .map(x => (x._1, x._2(0).as[JsObject], x._2(1).as[JsObject], x._3))
    .map(x => (x._1, x._2("id"), x._2("name").as[String], x._3("id"), x._3("name").as[String], x._4("winner_id"), x._4("minutes"), x._4("score")))
    .filter(x => (x._3 == "Felix Auger Aliassime" && x._2 == x._6) || (x._5 == "Felix Auger Aliassime" && x._5 == x._6))
    .map(x=>(x._1,x._3,x._5,x._7,x._8)).filter(x=>x._4.toString()!= "null").minBy(x=>x._4.as[Int])

  print(datos)


}




