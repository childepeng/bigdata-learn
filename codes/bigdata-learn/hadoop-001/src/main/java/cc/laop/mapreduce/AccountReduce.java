package cc.laop.mapreduce;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @Auther: pengpeng
 * @Date: create in 2020/5/14 11:15
 * @Description:
 */
public class AccountReduce extends Reducer<Text, LongWritable, Text, LongWritable> {

    @Override
    protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException,
            InterruptedException {
        int count = 0;
        for (LongWritable value : values) {
            count++;
        }
        context.write(key, new LongWritable(count));
    }
}
