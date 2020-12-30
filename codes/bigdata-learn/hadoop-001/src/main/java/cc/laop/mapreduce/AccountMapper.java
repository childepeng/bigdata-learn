package cc.laop.mapreduce;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @Auther: pengpeng
 * @Date: create in 2020/5/13 10:27
 * @Description:
 */
public class AccountMapper extends Mapper<LongWritable, Text, Text, LongWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        if (StringUtils.isEmpty(line)) {
            return;
        }
        JSONObject job = JSON.parseObject(line);
        String code = job.getJSONObject("msg").getString("code");
        String mac = job.getJSONObject("msg").getString("mac");
        String kk = code + "+" + mac;
        long time = job.getJSONObject("msg").getLong("currentTime");
        try {
            context.write(new Text(kk), new LongWritable(time));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
