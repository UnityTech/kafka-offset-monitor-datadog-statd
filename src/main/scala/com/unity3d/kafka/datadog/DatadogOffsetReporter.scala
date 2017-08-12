package com.unity3d.kafka.datadog

import com.quantifind.kafka.OffsetGetter.OffsetInfo
import com.timgroup.statsd.NonBlockingStatsDClient
import org.slf4j.LoggerFactory


final class DatadogOffsetReporter(pluginsArgs: String) extends com.quantifind.kafka.offsetapp.OffsetInfoReporter {

  private val logger = LoggerFactory.getLogger(classOf[DatadogOffsetReporter])
  private val arguments = new DatadogOffsetReporterArgumentParser(pluginsArgs)
  logger.info("arguments=" + arguments.toString)

  private val metrics = new NonBlockingStatsDClient(arguments.prefix, arguments.statsDHost, 8125)

  override def report(info: IndexedSeq[OffsetInfo]): Unit = {
    info.par.foreach(info => {
      metrics.gauge(
        "partition_lag",
        info.lag,
        s"consumer:${info.group}",
        s"partition:${info.partition}",
        s"topic:${info.topic}"
      )

      metrics.gauge(
        "offsets",
        info.offset,
        s"consumer:${info.group}",
        s"partition:${info.partition}",
        s"topic:${info.topic}"
      )

      metrics.time(
        "last_seen",
        info.modified.inMillis,
        s"consumer:${info.group}",
        s"partition:${info.partition}",
        s"topic:${info.topic}"
      )
    })
  }
}
