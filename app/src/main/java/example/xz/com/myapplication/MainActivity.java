package example.xz.com.myapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.jaeger.library.StatusBarUtil;
import com.umeng.analytics.MobclickAgent;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import example.xz.com.myapplication.Adapter.RecyclerViewAdapter;
import example.xz.com.myapplication.Creator.ViewModelCreator;
import example.xz.com.myapplication.Data.BasicBean;
import example.xz.com.myapplication.Data.Story;
import example.xz.com.myapplication.Data.StoryViewModel;
import example.xz.com.myapplication.Data.TopStory;
import example.xz.com.myapplication.Data.TopStoryViewModel;
import example.xz.com.myapplication.NoHttp.BasicRequest;
import example.xz.com.myapplication.NoHttp.CallServer;
import example.xz.com.myapplication.NoHttp.ResponseCallback;
import example.xz.com.myapplication.Utils.NetworkUtil;
import example.xz.com.myapplication.Utils.ToastUtils;

public class MainActivity extends BaseActivity implements ResponseCallback<BasicBean> {


    private ViewModelCreator viewModelCreator;
    private List<TopStoryViewModel> topStories;
    private List<StoryViewModel> stories;
    private LRecyclerView recyler_view;
    private RecyclerViewAdapter recyclerViewAdapter;
    private LRecyclerViewAdapter adapter;
    private LinearLayout ll_loading;
    private LinearLayout ll_data_show;
    private String todayDate;
    private int date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        viewModelCreator = ViewModelCreator.getInstance(MyApplication.getMyApplication());

        initData();
        Log.i("MobclickAgent",getDeviceInfo(MainActivity.this));
    }
    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    private void initData() {
        if (!NetworkUtil.isNetworkConnected(MyApplication.getMyApplication())) {
            stories = viewModelCreator.getStoryModel();
            topStories = viewModelCreator.getTopstoryModels();
            if (stories != null)
                setRecyclerAdapter();
            ToastUtils.showLong("网络出小差了");
        } else {
            requestData();
        }
    }


    private void requestData() {
        String url = "http://news-at.zhihu.com/api/4/news/latest";
        Request request = new BasicRequest(url,BasicBean.class);
        CallServer.getInstance().request(0, request, this);
    }

    private void requestBeforeData(String date) {
        if (!NetworkUtil.isNetworkConnected(MyApplication.getMyApplication())) {
            ToastUtils.showLong("网络出小差了");
        } else {
            String beforeurl = "http://news.at.zhihu.com/api/4/news/before/" + date;
            Log.i("Date", beforeurl);
            Request request = new BasicRequest(beforeurl,BasicBean.class);
            int what = Integer.parseInt(date);
            CallServer.getInstance().request(what, request, this);
        }
    }


    private void setRecyclerAdapter() {
        recyclerViewAdapter.setDataList(stories);
        if (adapter == null) {
            adapter = new LRecyclerViewAdapter(recyclerViewAdapter);
            recyler_view.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(stories.get(position).getType()==0){
                    Intent i=new Intent(MainActivity.this,ItemActivity.class);
                    i.putExtra("storyId",stories.get(position).getId());
                    startActivity(i);
                }

            }
        });

        ll_data_show.setVisibility(View.VISIBLE);
        ll_loading.setVisibility(View.GONE);
    }

    @Override
    public void onSucceed(int what, Response<BasicBean> response) {
        BasicBean basicBean = response.get();
        if (basicBean != null) {
            if (what == 0) {
                Toast.makeText(MainActivity.this, basicBean.getDate(), Toast.LENGTH_LONG).show();
                todayDate = basicBean.getDate();
                //网络获取的列表
                List<TopStory> topStorylist = basicBean.getTopStory();
                List<Story> storyList = basicBean.getStories();
                //设置给本地数据库

                addDateitem(storyList);

                List<TopStoryViewModel> topStoryViewModelList = viewModelCreator.setTopstoryModels(topStorylist);
                List<StoryViewModel> storyViewModelList = viewModelCreator.setStoryModels(storyList);
                if (topStoryViewModelList != null) {
                    topStories = topStoryViewModelList;
                }
                if (storyViewModelList != null) {
                    stories = storyViewModelList;
                }



                if (stories != null)
                    setRecyclerAdapter();

                recyler_view.refreshComplete(0);
                recyclerViewAdapter.notifyDataSetChanged();

                recyler_view.setLoadMoreEnabled(true);


            } else {
                List<Story> storyList = basicBean.getStories();

                todayDate = basicBean.getDate();

                addDateitem(storyList);

                List<StoryViewModel> storyViewModelList = viewModelCreator.setStoryModels(storyList);
                if (storyViewModelList != null) {
                    stories.addAll(stories.size(), storyViewModelList);
                }



                setRecyclerAdapter();

                recyler_view.refreshComplete(0);

            }
        }
    }

    private void addDateitem(List<Story> storyList) {
        Story story = new Story();
        story.setTitle(todayDate);
        story.setstoryid(Integer.parseInt(todayDate));
        story.setType(1);
        story.setGa_prefix("");
        List<String> stringList = new ArrayList<String>();
        stringList.add("a");
        story.setImages(stringList);
        storyList.add(story);
    }

    @Override
    public void onFailed(int what, Object tag, Exception exception, long networkMills) {
        if (exception != null) {
            if (!TextUtils.isEmpty(exception.getMessage())) {
                ToastUtils.showShort(exception.getMessage());
            }
        }
    }

    private void initView() {

        StatusBarUtil.setColor(this,getResources().getColor(R.color.colorPrimary),60);

        recyler_view = (LRecyclerView) findViewById(R.id.recyler_view);
        recyler_view.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new RecyclerViewAdapter(this);

        DividerDecoration divider = new DividerDecoration.Builder(this)
                .setHeight(1.0f)
                .setPadding(1.0f)
                .setColorResource(R.color.itemDecoration)
                .build();
        recyler_view.addItemDecoration(divider);

        ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
        ll_data_show = (LinearLayout) findViewById(R.id.ll_data_show);
        ll_loading.setVisibility(View.VISIBLE);
        ll_data_show.setVisibility(View.GONE);

        recyler_view.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!NetworkUtil.isNetworkConnected(MyApplication.getMyApplication())) {
                    ToastUtils.showLong("网络出小差了");
                }else {
                    requestData();
                }

            }
        });


        recyler_view.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                if (null != todayDate) {
                    date = Integer.parseInt(todayDate);

                    Log.i("Date", "修改之前的日期" + date + "");
                    requestBeforeData(date + "");
                    date = date - 1;
                }
            }
        });
        recyler_view.setFooterViewColor(R.color.colorPrimary,R.color.colorPrimary,R.color.background);
        recyler_view.setFooterViewHint("拼命加载中..","加载完成","网络不给力，再试一次吧");
        recyler_view.setRefreshProgressStyle(ProgressStyle.Pacman);


    }



    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            String mac = getMac(context);

            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMac(Context context) {
        String mac = "";
        if (context == null) {
            return mac;
        }
        if (Build.VERSION.SDK_INT < 23) {
            mac = getMacBySystemInterface(context);
        } else {
            mac = getMacByJavaAPI();
            if (TextUtils.isEmpty(mac)){
                mac = getMacBySystemInterface(context);
            }
        }
        return mac;

    }

    @TargetApi(9)
    private static String getMacByJavaAPI() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface netInterface = interfaces.nextElement();
                if ("wlan0".equals(netInterface.getName()) || "eth0".equals(netInterface.getName())) {
                    byte[] addr = netInterface.getHardwareAddress();
                    if (addr == null || addr.length == 0) {
                        return null;
                    }
                    StringBuilder buf = new StringBuilder();
                    for (byte b : addr) {
                        buf.append(String.format("%02X:", b));
                    }
                    if (buf.length() > 0) {
                        buf.deleteCharAt(buf.length() - 1);
                    }
                    return buf.toString().toLowerCase(Locale.getDefault());
                }
            }
        } catch (Throwable e) {
        }
        return null;
    }

    private static String getMacBySystemInterface(Context context) {
        if (context == null) {
            return "";
        }
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (checkPermission(context, Manifest.permission.ACCESS_WIFI_STATE)) {
                WifiInfo info = wifi.getConnectionInfo();
                return info.getMacAddress();
            } else {
                return "";
            }
        } catch (Throwable e) {
            return "";
        }
    }

    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (context == null) {
            return result;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Throwable e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

}
