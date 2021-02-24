package cc.laop.flink.stream

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time

/**
 * @Auther: Administrator
 * @Date: create in 2021/1/22 17:27
 * @Description:
 */
object WaterMarkTest {

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.getConfig.setAutoWatermarkInterval(10000)

    val ds = env.addSource(new KafkaConsumerFactory().build("watermarkTest01"))

    ds
      .map(it => {
        val sarr = it.split(",")
        Access(sarr(0).toInt, sarr(1).toLong)
      })
      .assignAscendingTimestamps(_.time)
      .windowAll(TumblingProcessingTimeWindows.of(Time.seconds(10)))
      .apply[Access]((tw, its, col) => {
        for (elem <- its) {
          col.collect(elem)
        }
      })
      .print()

    env.execute()
  }

}

case class Access(id: Int, time: Long)
