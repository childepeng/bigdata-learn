package cc.laop.hdfs;

import com.alibaba.fastjson.JSONArray;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @Auther: pengpeng
 * @Date: create in 2020/5/13 10:46
 * @Description:
 */
public class HdfsStarter {

    private String hdfs_path = "hdfs://192.168.1.16:9000";
    private String hdfs_user = "root";
    private FileSystem fileSystem;

    @Before
    public void init() throws URISyntaxException, IOException, InterruptedException {
        Configuration config = new Configuration();
        config.set("dfs.replication", "1");
        fileSystem = FileSystem.get(new URI(hdfs_path), config, hdfs_user);
    }


    @Test
    public void mkdir() throws IOException {
        //fileSystem.mkdirs(new Path("/test"));

        fileSystem.delete(new Path("/iptv/out2020-05-15"), true);
    }

    @Test
    public void write() throws IOException {
        FSDataOutputStream out = fileSystem.create(new Path("/test/test.txt"));
        out.write("abcd".getBytes());
        out.write("abcdasdf".getBytes());
        out.write("a234".getBytes());
        out.flush();
        out.close();
    }

    @Test
    public void delete() throws IOException {
        fileSystem.delete(new Path("/flume"), true);
    }

    @Test
    public void read() throws IOException {
        FSDataInputStream input = fileSystem.open(new Path("/iptv/access-20-05-13/access.1589362668850"));
        List<String> list = IOUtils.readLines(input);
        list.forEach(it -> {
            //JSONObject json = JSON.parseObject(it);
            JSONArray jarr = JSONArray.parseArray(it);
            jarr.forEach(it2 -> System.out.println(it2.toString()));
        });
    }


    @Test
    public void filelist() throws IOException {
        RemoteIterator<LocatedFileStatus> ri = fileSystem.listFiles(new Path("/test"), true);
        while (ri.hasNext()) {
            LocatedFileStatus lfs = ri.next();
            FSDataInputStream in = fileSystem.open(lfs.getPath());
            System.out.println(lfs);
            List<String> lines = IOUtils.readLines(in);
            lines.forEach(it -> System.out.println(it));
        }
    }


}
