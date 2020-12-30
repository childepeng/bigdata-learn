package cc.laop.flink.java.stream;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @Auther: Administrator
 * @Date: create in 2020/12/29 16:37
 * @Description:
 */
public class WordCount {

    public static void main(String[] args) throws Exception {

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<String> dss = env.socketTextStream("192.168.1.30", 2345);

        dss
                .flatMap((FlatMapFunction<String, Tuple2<String, Integer>>) (s, c) -> {
                    for (String s1 : s.split("\\W+")) {
                        c.collect(new Tuple2<>(s1, 1));
                    }
                })
                .returns(Types.TUPLE(Types.STRING, Types.INT))
                .keyBy((KeySelector<Tuple2<String, Integer>, String>) value -> value.f0)
                .sum(1)
                .print();

        env.execute("socket word count");
    }

}
