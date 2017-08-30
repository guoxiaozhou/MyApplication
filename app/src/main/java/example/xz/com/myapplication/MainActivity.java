package example.xz.com.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.ArrayList;
import java.util.List;

import example.xz.com.myapplication.Creator.ViewModelCreator;
import example.xz.com.myapplication.Data.BasicBean;
import example.xz.com.myapplication.Data.ExtraNews;
import example.xz.com.myapplication.Data.Story;
import example.xz.com.myapplication.Data.TopStory;
import example.xz.com.myapplication.Data.TopStoryViewModel;
import example.xz.com.myapplication.NoHttp.BasicRequest;
import example.xz.com.myapplication.NoHttp.CallServer;
import example.xz.com.myapplication.NoHttp.EntityRequest;
import example.xz.com.myapplication.NoHttp.ResponseCallback;
import example.xz.com.myapplication.Utils.NetworkUtil;
import example.xz.com.myapplication.Utils.ToastUtils;

public class MainActivity extends AppCompatActivity implements ResponseCallback<BasicBean>, View.OnClickListener {

    /**
     * Button
     */
    private Button button1;
    /**
     * Button
     */
    private Button button2;
    final int Menu_1 = Menu.FIRST;
    /**
     * Hello World!
     */
    private TextView tv_hello;
    private PopupWindow popupWindow;
    private ViewModelCreator viewModelCreator;
    private List<TopStoryViewModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        String url = "http://news-at.zhihu.com/api/4/news/latest";


        viewModelCreator=ViewModelCreator.getInstance(MyApplication.getMyApplication());



//        View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog,null);
//        Dialog dialog=new AlertDialog.Builder(MainActivity.this).setView(view).show();
//        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        if (NetworkUtil.isNetworkConnected(MyApplication.getMyApplication()) ||
                NetworkUtil.isWifiConnected(this) ||
                NetworkUtil.isMobileConnected(this)) {
            Log.i("Network状态：", NetworkUtil.isNetworkConnected(MyApplication.getMyApplication()) + "");
            Log.i("Wifi状态：", NetworkUtil.isWifiConnected(MyApplication.getMyApplication()) + "");
            Log.i("Mobile状态：", NetworkUtil.isMobileConnected(MyApplication.getMyApplication()) + "");

            Request request = new BasicRequest(url);
            CallServer.getInstance().request(0, request, this);
        }else {

            list = viewModelCreator.getTopstoryModels();
            for (int i = 0; i < list.size(); i++) {
                Log.i("topstory", "id:" + list.get(i).getId());
                Log.i("topstory", "title:" + list.get(i).getTitle());
                Log.i("topstory", "type:" + list.get(i).getType());
                Log.i("topstory", "ga_prefix:" + list.get(i).getGa_prefix());
                Log.i("topstory", "imageurl:" + list.get(i).getImage());
            }
        }



    }


    @Override
    public void onSucceed(int what, Response<BasicBean> response) {
        BasicBean basicBean = response.get();
        if (basicBean != null) {
            if (what == 0) {
                Toast.makeText(MainActivity.this, basicBean.getDate(), Toast.LENGTH_LONG).show();
                Log.i("数据", basicBean.getDate() + "," + basicBean.getStories().toString() + "," + basicBean.getTopStory().toString());
             //   List<Story> storyList = basicBean.setStoriesList(basicBean.getStories());
                List<TopStory> topStoriesList = basicBean.setTopStoriesList(basicBean.getTopStory());
                List<TopStoryViewModel> topStoryViewModels=viewModelCreator.setTopstoryModels(topStoriesList);
                list=viewModelCreator.getTopstoryModels();

            }
        }
    }

    @Override
    public void onFailed(int what, Object tag, Exception exception, long networkMills) {
        Log.i("错误", tag.toString() + exception.toString());
    }

    private void initView() {
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);
        tv_hello = (TextView) findViewById(R.id.tv_hello);
        registerForContextMenu(tv_hello);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                ToastUtils.showShort("短时间Toast");
                showPopupWindow();
                break;
            case R.id.button2:
                ToastUtils.showLong("长时间Toast");
                //  Toast.makeText(this,"长时间Toast",)
                break;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, Menu_1, 0, "复制文字");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == 1)
            ToastUtils.showShort("复制成功");
        return super.onContextItemSelected(item);
    }

    public void showPopupWindow() {
        View contentView = LayoutInflater.from(MyApplication.getMyApplication()).inflate(R.layout.popupwindow_copycard, null);
        popupWindow = new PopupWindow(contentView);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        int[] location=new int[2];
        tv_hello.getLocationOnScreen(location);
        popupWindow.showAtLocation(tv_hello,Gravity.NO_GRAVITY, location[0]+tv_hello.getWidth(), location[1]);
    }
}
