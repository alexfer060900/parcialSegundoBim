import com.github.tototoshi.csv.CSVReader
import play.api.libs.json.Json
import com.cibo.evilplot._
import com.cibo.evilplot.plot.{BarChart, PieChart}
import com.cibo.evilplot.plot.aesthetics.DefaultTheme.{DefaultElements, DefaultTheme, defaultTheme}

import scala.util.{Failure, Success, Try}
import java.io.File

object Main extends App{
  val reader = CSVReader.open(new File("C:\\Users\\alexa\\Downloads\\data_parcial_2_OO.csv\\data_parcial_2_OO.csv"))
  val data = reader.allWithHeaders()
  reader.close()


  // ¿Cuantos torneos se jugaron en el 2022?
  val torneosjugados = data
    .map(row => row("tourney_id"))
    .size

  println("Se jugaron: " + torneosjugados + " en el 2022")

  // ¿Cuáles los nombres de los torneos que se jugaron el 2022?

  val nombretorneos = data
    .map(row => row("tourney_name"))
    .distinct
  println("Los nombres de los torneos son: " + nombretorneos)

  def max (s: List[Double]) =
    s.max

  def min (s: List[Double]) =
    s.min

  def average (s: List[Double]) =
    s.sum / s.size

  val playerInfo = data
    .map(row => row("players_info"))
    .map(text => Json.parse(text))

  val popularHand = playerInfo
    .flatMap(jsonData => jsonData \\ "hand")
    .map(jsValue => jsValue.as[String])
    .filter(x => x != "null")
    .groupBy(identity)
    .map { case (keyword, lista) => (keyword, lista.size) }
    .toList
    .sortBy(_._2)
    .reverse

  val handCount = popularHand.map(_._2).map(_.toDouble)
  val hand = popularHand.map(_._1)

  println(popularHand)


  BarChart(handCount)
    .title("Mano mas usada")
    .xAxis(hand)
    .yAxis()
    .frame()
    .yLabel("Popularidad")
    .bottomLegend()
    .render()
    .write(new File("C:\\Users\\alexa\\Desktop\\histogramaManos.png"))

  val height0 = playerInfo
    .flatMap(jsonData => jsonData \\ "height")
    .map(jsValue => Try {
      jsValue.as[Double]
    })
    .filter(_.isSuccess)
    .map(x => x.get)

  val popularHeight = playerInfo
    .flatMap(jsonData => jsonData \\ "height")
    .map(jsValue => Try{ jsValue.as[Double] })
    .filter(_.isSuccess)
    .map(x => x.get)
    .groupBy(identity)
    .map { case (keyword, lista) => (keyword, lista.size) }
    .toList
    .sortBy(_._2)
    .reverse


  val heightCount = popularHeight.map(_._2).map(_.toDouble)
  val height1 = popularHeight.map(_._1).map(_.toString)

  val masAlto = max(height0)

  val masBajo = min(height0)

  val alturasPromedio = average(height0)

  println("El jugador mas alto es: " + masAlto)
  println("El jugador mas bajo es: " + masBajo)
  println("La estatura promedio de todos los jugadores es: " + alturasPromedio)


  BarChart(heightCount)
    .title("Altura mas populares")
    .xAxis(height1)
    .yAxis()
    .frame()
    .yLabel("Popularidad")
    .bottomLegend()
    .render()
    .write(new File("C:\\Users\\alexa\\Desktop\\histogramaAlturas.png"))

  val matchinfo = data
    .flatMap(row => row.get("match_info"))
    .map(row => Json.parse(row))
    .flatMap(jsonData => jsonData \\ "best_of")
    .map(jsValue => jsValue.as[Int])

  val matchinfoDouble = data
    .flatMap(row => row.get("match_info"))
    .map(row => Json.parse(row))
    .flatMap(jsonData => jsonData \\ "best_of")
    .map(jsValue => jsValue.as[Int])
    .map(_.toDouble)

  val matchinfo2 = matchinfo
    .groupBy(identity)
    .map { case (keyword, lista) => (keyword, lista.size) }
    .toList
    .sortBy(_._2)
    .reverse

  val matchInfo = matchinfo2.map(_._2).map(_.toDouble)
  val matchInfo2 = matchinfo2.map(_._1).map(_.toString)

  BarChart(matchInfo)
    .title("Numero de sets jugados")
    .xAxis(matchInfo2)
    .yAxis()
    .frame()
    .yLabel("Popularidad")
    .bottomLegend()
    .render()
    .write(new File("C:\\Users\\alexa\\Desktop\\histogramaSets.png"))


  val bestalto = max(matchinfoDouble)

  val bestbajo = min(matchinfoDouble)

  val matchpromediobest = average(matchinfoDouble)

  println("El best_of mas alto es: " + bestalto)
  println("El best_of mas bajo es: " + bestbajo)
  println("El promedio es: " + matchpromediobest)



  val matchinfoMinutes = data
    .flatMap(row => row.get("match_info"))
    .map(row => Json.parse(row))
    .flatMap(jsonData => jsonData \\ "minutes")
    .map(jsValue => Try {
      jsValue.as[Double]
    })
    .filter(_.isSuccess)
    .map(x => x.get)

  val minutes = matchinfoMinutes
    .groupBy(identity)
    .map { case (keyword, lista) => (keyword, lista.size) }
    .toList
    .sortBy(_._2)
    .reverse

  val minutesInfo = minutes.map(_._2).map(_.toDouble)
  val minutesInfo2 = minutes.map(_._1).map(_.toString)


  BarChart(minutesInfo)
    .title("Numero de minutos jugados por partido")
    .xAxis(minutesInfo2)
    .yAxis()
    .frame()
    .yLabel("Popularidad")
    .bottomLegend()
    .render()
    .write(new File("C:\\Users\\alexa\\Desktop\\histogramaMinutos.png"))


  val minutestalto = max(matchinfoMinutes)

  val minutetbajo = min(matchinfoMinutes)

  val minutospromedio = average(matchinfoMinutes)

  println("El minuto mas alto es: " + minutestalto)
  println("El minuto mas bajo es: " + minutetbajo)
  println("El promedio minuto es: " + minutospromedio)




}
