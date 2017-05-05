package org.onoreak.timeSeries

import java.io.File
import java.util.{Locale, Properties}

import be.cetic.tsimulus.config.Configuration
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.joda.time.format.DateTimeFormat
import spray.json._

import scala.io.Source
import scala.util.control.Breaks.{break, breakable}

/**
  * Created by Chris on 5/1/17.
  */
object KafkaProducer
{
  def main(args: Array[String]): Unit =
  {
    val content = Source .fromFile(new File(args(0)))
      .getLines()
      .mkString("\n")

    val topic = args(1)
    val brokers = args(2)

    val dtf = DateTimeFormat.forPattern("YYYY-MM-dd'T'HH:mm:ss.SSS'Z'")

    val config = Configuration(content.parseJson)

    val freq = scala.collection.mutable.Map[String,Long]()

    val props = new Properties()
    props.put("bootstrap.servers", brokers)
    props.put("client.id", "ScalaProducerExample")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    val producer = new KafkaProducer[String, String](props);
    config.series foreach (e => freq.put(e.name, e.frequency.getMillis))

    println("Publishing to topic: " + topic);
    println("On broker: " + brokers)
    var i = 1
    var t0 = 0;

    val formatter = java.text.NumberFormat.getNumberInstance(Locale.US);
    formatter.setMaximumFractionDigits(3);

    while(true) {
      val value = Utils.getTimeValue(config.timeSeries)
      var j = 0
      for (d <- value) {
        breakable {
          val fre = freq.get(d._1).getOrElse(1000).asInstanceOf[Long]
          var rem = 0.asInstanceOf[Long]
          if (t0 != 0) rem = t0 % fre;
          if (rem != 0) break

          val msg = d._1 + ";" + d._2 + ";" + formatter.format(d._3.asInstanceOf[Some[Double]].getOrElse("****"))
          val data = new ProducerRecord[String, String](topic, d._1, msg)
          producer.send(data)

          j = j + 1
        }
      }
      Thread.sleep(100)
      t0 = t0 + 100
    }
  }
}
