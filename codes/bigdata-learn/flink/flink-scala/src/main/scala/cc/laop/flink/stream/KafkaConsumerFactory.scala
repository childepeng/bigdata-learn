package cc.laop.flink.stream

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

/**
 * @Auther: Administrator
 * @Date: create in 2021/1/22 17:28
 * @Description:
 */
class KafkaConsumerFactory {

  def build(topic: String): FlinkKafkaConsumer[String] = {
    var prop = new Properties()
    prop.put("bootstrap.servers", "192.168.1.30:9092")
    prop.put("group.id", "test004")
    new FlinkKafkaConsumer[String](topic, new SimpleStringSchema(), prop)
  }

}
