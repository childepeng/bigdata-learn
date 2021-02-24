package cc.laop.flink.java.stream;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.time.Duration;
import java.util.Properties;

/**
 * @Auther: Administrator
 * @Date: create in 2021/1/26 16:42
 * @Description:
 */
public class WatermarkTest {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // env.getConfig().setAutoWatermarkInterval(10000);
        env.setParallelism(1);

        Properties prop = new Properties();
        prop.put("bootstrap.servers", "192.168.1.30:9092");
        prop.put("group.id", "test004");

        DataStreamSource<String> ds = env.addSource(new FlinkKafkaConsumer<>("watermarkTest01",
                new SimpleStringSchema(), prop));

        WatermarkStrategy<Tuple3<Integer, Long, Integer>> ws =
                WatermarkStrategy
                        .<Tuple3<Integer, Long, Integer>>forBoundedOutOfOrderness(Duration.ofSeconds(10))
                        .withTimestampAssigner((element, recordTimestamp) -> element.f1);

        ds
                .map(it -> {
                    Integer i = Integer.valueOf(it.substring(0, it.indexOf(",")));
                    Long time = Long.valueOf(it.substring(it.indexOf(",") + 1));
                    return new Tuple3<>(i, time, 1);
                })
                .returns(Types.TUPLE(Types.INT, Types.LONG, Types.INT))
                .assignTimestampsAndWatermarks(ws)
                .keyBy(it -> it.f0)
                .window(TumblingEventTimeWindows.of(Time.seconds(10)))
                // .window(SlidingEventTimeWindows.of(Time.seconds(10), Time.seconds(8)))
                .reduce((t1, t2) -> {
                    System.out.println("t1: " + t1);
                    System.out.println("t2: " + t2);
                    return new Tuple3<>(t2.f0, t2.f1, t2.f2 + t1.f2);
                })
                .print()
        ;

        env.execute();

    }


}
