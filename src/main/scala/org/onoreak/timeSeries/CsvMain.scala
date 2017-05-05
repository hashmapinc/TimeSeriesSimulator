package org.onoreak.timeSeries

import java.io.{File, _}
import java.util.Locale
import be.cetic.tsimulus.config.Configuration
import spray.json._
import scala.io.Source
import scala.util.control.Breaks.{break, breakable}

/**
  * Created by Chris on 5/1/17.
  */
object CsvMain
{
  def main(args: Array[String]): Unit =
  {
    val content = Source .fromFile(new File(args(0)))
      .getLines()
      .mkString("\n")

    val config = Configuration(content.parseJson)

    val freq = scala.collection.mutable.Map[String,Long]()

    config.series foreach (e => freq.put(e.name, e.frequency.getMillis))

    var i = 1
    var reset = true
    var fileIter = 0;
    var file = new File("output - " + fileIter +  " .csv")
    var bw = new BufferedWriter(new FileWriter(file))
    val formatter = java.text.NumberFormat.getNumberInstance(Locale.US);
    formatter.setMaximumFractionDigits(3);
    val t0 = 0;

    while( i < 10000000) {
      val value = Utils.getTimeValue(config.timeSeries)

      var dataRow = ""

      var j = 0
      for (d <- value) {
        breakable {
          if (reset){
            if (j == 0) dataRow = dataRow + "Time, " + d._1
            else dataRow = dataRow + d._1
            if (j != value.size - 1) dataRow = dataRow + ","
            else dataRow = dataRow + (util.Properties.lineSeparator)
          }
          else {
            val fre = freq.get(d._1).getOrElse(1000).asInstanceOf[Long]
            var rem = 0.asInstanceOf[Long]
            if (t0 != 0) rem = t0 % fre;
            if (rem != 0) break

            if (j == 0) dataRow = dataRow + d._2 + ","
            dataRow = dataRow + formatter.format(d._3.asInstanceOf[Option[Double]].getOrElse("****"))
            if (j != value.size - 1) dataRow = dataRow + ","
            else dataRow = dataRow + (util.Properties.lineSeparator)
          }
          j = j + 1
        }
      }
      bw.write(dataRow)
      reset = false
      i=i+1
      if (i % 100000 == 0) {
        fileIter = fileIter + 1;
        file = new File("output - " + fileIter + " .csv")
        bw = new BufferedWriter(new FileWriter(file))
        reset = true
      }

      if (i % 5 == 0) bw.flush()
    }

    bw.close()
    println("Done!")
  }
}