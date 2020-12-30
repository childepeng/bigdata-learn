package cc.laop.flume.intercept;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.List;

/**
 * @Auther: pengpeng
 * @Date: create in 2020/5/21 9:24
 * @Description:
 */
public class JsonArrSplitInterceptor implements Interceptor {

    private ObjectMapper mapper = null;

    @Override
    public void initialize() {
        mapper = new ObjectMapper();
    }

    @Override
    public Event intercept(Event event) {
        try {
            JsonNode node = mapper.readTree(new String(event.getBody()));
            if (node.isArray()) {
                StringBuffer sb = new StringBuffer();
                node.forEach(it -> sb.append(it.toString()).append(System.lineSeparator()));
                event.setBody(sb.toString().getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return event;
    }

    @Override
    public List<Event> intercept(List<Event> list) {
        if (list != null && list.size() > 0) {
            for (Event event : list) {
                intercept(event);
            }
        }
        return list;
    }

    @Override
    public void close() {

    }


    public static class Builder implements Interceptor.Builder {
        private Context context;

        @Override
        public Interceptor build() {
            return new JsonArrSplitInterceptor();
        }

        @Override
        public void configure(Context context) {
            this.context = context;
        }
    }
}
