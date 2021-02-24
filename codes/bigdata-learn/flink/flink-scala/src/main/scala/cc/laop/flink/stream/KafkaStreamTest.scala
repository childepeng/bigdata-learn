package cc.laop.flink.stream

import com.alibaba.fastjson.JSON
import org.apache.flink.streaming.api.scala._


/**
 * @Auther: Administrator
 * @Date: create in 2021/1/18 17:57
 * @Description:
 */
object KafkaStreamTest {

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val dataSource = env.addSource(new KafkaConsumerFactory().build("watermarkTest"))

    dataSource
      // global 上游数据分发给下游第一个分区
      //.global
      // shuffle 上游数据随机分发给下游分区
      //.shuffle
      // rebalance 上游每个分区采用轮询算法分发给下游每个分区
      //.rebalance
      // rescale 上游分区将数据就近发给下游分区
      .rescale
      // broadcast 广播，下游每个分区都会收到全量数据
      //.broadcast
      // forward 需要上下游分区一致，否则会抛异常
      //.forward
      .map(JSON.parseObject(_))
      .setParallelism(2)
      .map(it => {
        Access(it.getIntValue("id"), it.getLongValue("time"))
      })
      .setParallelism(2)
      .print()

    //dataSource.print()

    env.execute()
  }

}

case class Access(id: Int, time: Long) {}
