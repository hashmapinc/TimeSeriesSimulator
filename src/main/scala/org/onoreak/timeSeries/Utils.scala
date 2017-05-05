package org.onoreak.timeSeries

import be.cetic.tsimulus.config.Configuration
import be.cetic.tsimulus.timeseries._
import com.github.nscala_time.time.Imports._

/**
  * Created by Chris on 5/5/17.
  */
object Utils
{
  /**
    * Creates a sequence of times, from a start time to a given limit.
    *
    * @param start the start time.
    * @param end the end time, no retrieved time can be set after this time.
    * @param duration the time space between two consecutive times.
    * @return a sequence of regularly spaced times, starting by start.
    */
  def sampling(start: LocalDateTime,
               end: LocalDateTime,
               duration: Duration): Stream[LocalDateTime] = if(start isAfter end) Stream.empty
  else start #:: sampling(start plus duration, end, duration)

  /**
    * Creates a sequence of times, from a start time to a given limit.
    *
    * @param start the start time.
    * @param end the end time, no retrieved time can be set after this time.
    * @param nbTimes the number of times that must be retrieved
    * @return a sequence of regularly spaced times, starting by start.
    */
  def sampling(start: LocalDateTime,
               end: LocalDateTime,
               nbTimes: Int): Stream[LocalDateTime] =
  {
    val duration = new Duration(start.toDateTime(DateTimeZone.UTC), end.toDateTime(DateTimeZone.UTC))
    sampling(start, end, new Duration(duration.getMillis / (nbTimes-1)))
  }

  def config2Results(config: Configuration): Map[String, Stream[(LocalDateTime, Any)]] =
    timeSeries2Results(config.timeSeries, config.from, config.to)

  def eval(config: Configuration, date: LocalDateTime) =
  {
    config.timeSeries.map{case (name, x) => (name, x._1.compute(date))}
  }

  def timeSeries2Results(ts: Map[String, (TimeSeries[Any], _root_.com.github.nscala_time.time.Imports.Duration)],
                         from: LocalDateTime,
                         to: LocalDateTime): Map[String, Stream[(LocalDateTime, Any)]] =
  {
    ts.map(series =>
    {
      val name = series._1
      val frequency = series._2._2
      val values = series._2._1
      val times = Utils.sampling(from, to, frequency)

      values.compute(LocalDateTime.now())

      name -> values.compute(times).filter(e => e._2.isDefined).map(e => (e._1, e._2.get))
    })

  }

  def getTimeValue(ts: Map[String, (TimeSeries[Any], _root_.com.github.nscala_time.time.Imports.Duration)]) : Iterable[(String,LocalDateTime,Any)] =
  {
    ts.map(series =>
    {
      val values = series._2._1

      val time = LocalDateTime.now();

      val data = values.compute(time);

      new Tuple3[String,LocalDateTime,Any](series._1, time, data)
    })

  }

  def generate(series: Map[String, Stream[(LocalDateTime, Any)]]): Stream[(LocalDateTime, String, Any)] =
  {
    val cleanedMap = series.filterNot(_._2.isEmpty)

    if (cleanedMap.isEmpty) Stream.Empty
    else
    {
      val selected = cleanedMap.minBy(e => e._2.head._1)

      val head = selected._2.head
      val tail = selected._2.tail
      val next = series.updated(selected._1, tail)

      (head._1, selected._1, head._2) #:: generate(next)
    }
  }
}
