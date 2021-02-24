package cc.laop.flink.stream

import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

/**
 * @Auther: Administrator
 * @Date: create in 2020/12/29 19:22
 * @Description:
 */
object SocketWordCount {

  def main(args: Array[String]): Unit = {
    // 创建执行环境
    var env = StreamExecutionEnvironment.getExecutionEnvironment
    // 设置并行度
    env.setParallelism(1)
    // 设置输入源
    var dss = env.socketTextStream("192.168.1.30", 2345)

    // 压平
    dss.flatMap(_.split("\\W+"))
      // 映射为二元组
      .map((_, 1))
      .keyBy(_._1)
      .sum(1)
      .print()
    
    env.execute("Scala Socket WordCount")
  }

}
