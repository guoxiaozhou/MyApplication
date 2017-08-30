package example.xz.com.myapplication.NoHttp;

import com.google.gson.Gson;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.RestRequest;
import com.yanzhenjie.nohttp.rest.StringRequest;

/**
 * Created by Administrator on 2017/8/29.
 */

public class EntityRequest<T> extends RestRequest<T> {

    private Class<T> clazz;

    public EntityRequest(String url, Class<T> clazz) {
        super(url, RequestMethod.GET);
        this.clazz = clazz;
    }

    public EntityRequest(String url, RequestMethod requestMethod, Class<T> clazz) {
        super(url, requestMethod);
        this.clazz = clazz;
    }

    @Override
    public T parseResponse(Headers headers, byte[] body) throws Exception {
        String json = StringRequest.parseResponseString(headers, body);
        return new Gson().fromJson(json, clazz);
    }
}
