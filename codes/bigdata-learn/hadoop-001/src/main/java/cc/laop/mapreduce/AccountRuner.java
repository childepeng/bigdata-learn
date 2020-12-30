package cc.laop.mapreduce;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.Date;

/**
 * @Auther: pengpeng
 * @Date: create in 2020/5/14 11:19
 * @Description:
 */
public class AccountRuner {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        job.setJarByClass(AccountRuner.class);

        // 设置Mapper
        job.setMapperClass(AccountMapper.class);

        // 设置Reduce
        job.setReducerClass(AccountReduce.class);

        // 设置Reduce输出key/value类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        // 设置Mapper输出key/value类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        // 设置输入数据路径
        FileInputFormat.setInputPaths(job, new Path("/iptv/access-20-05-14"), new Path("/iptv/access-20-05-15"));
        FileInputFormat.setInputPathFilter(job, TmpPathFilter.class);

        // 设置输出数据路径
        String date = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
        FileOutputFormat.setOutputPath(job, new Path("/iptv/out" + date));

        job.waitForCompletion(true);
    }


    public static class TmpPathFilter implements PathFilter {

        @Override
        public boolean accept(Path path) {
            return !path.toString().endsWith(".tmp");
        }
    }
}
