package example.xz.com.myapplication.NoHttp;

import android.util.Log;

import com.google.gson.Gson;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.RestRequest;
import com.yanzhenjie.nohttp.rest.StringRequest;

import example.xz.com.myapplication.Data.BasicBean;

/**
 * Created by Administrator on 2017/8/23.
 */

public class BasicRequest extends RestRequest {
    public BasicRequest(String url) {
        super(url, RequestMethod.GET);
    }

    @Override
    public BasicBean parseResponse(Headers responseHeaders, byte[] responseBody) throws Exception {
        String response= StringRequest.parseResponseString(responseHeaders,responseBody);
        try {
            Log.i("response:",response);
            return new Gson().fromJson(response,BasicBean.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new BasicBean();

    }
}
