package lab.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;


/**
 * Json格式工具，还未完成
 */
public class JsonUtil {

    private final static GsonBuilder gsonBuilder = new GsonBuilder();

    private final static Gson gson = gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    /**
     * 以前写的JsonUtil效率太低,优化一些json处理
     *
     * @param object
     * @throws IOException
     */
    public static void toJson(Object object) throws IOException {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json; charset=utf-8");
        response.setHeader("cache-control", "no-cache");
        PrintWriter out = response.getWriter();
        out.print(gson.toJson(object));
        out.flush();
        out.close();
    }


    public static void writeJson(Object o) throws IOException {

        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json; charset=utf-8");
        response.setHeader("cache-control", "no-cache");

        PrintWriter out = response.getWriter();
        out.print(o);
        out.flush();
        out.close();
    }

    /**
     * 把json字符串变成List
     * params: new TypeToken<List<yourbean>>(){}.getType(),
     * new TypeToken<HashMap<String,Object>>() {}.getType()
     * 传入这个参数,可以变成map
     *
     * @param json
     * @param type new TypeToken<List<yourbean>>(){}.getType()
     * @return
     */
    public static <T> T fromJson(String json, Type type) {
        return gson.fromJson(json, type);
    }
}
