package example.xz.com.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.litepal.crud.DataSupport;

import java.util.List;

import example.xz.com.myapplication.Creator.ViewModelCreator;
import example.xz.com.myapplication.Data.BasicBean;
import example.xz.com.myapplication.Data.Story;
import example.xz.com.myapplication.Data.StoryModel;
import example.xz.com.myapplication.Data.StoryViewModel;
import example.xz.com.myapplication.Data.TopStory;
import example.xz.com.myapplication.Data.TopStoryModel;
import example.xz.com.myapplication.Data.TopStoryViewModel;
import example.xz.com.myapplication.NoHttp.BasicRequest;
import example.xz.com.myapplication.NoHttp.CallServer;
import example.xz.com.myapplication.NoHttp.ResponseCallback;
import example.xz.com.myapplication.Utils.NetworkUtil;
import example.xz.com.myapplication.Utils.ToastUtils;

public class MainActivity extends AppCompatActivity implements ResponseCallback<BasicBean>{


    private TextView tv_hello;
    private ViewModelCreator viewModelCreator;
    private List<TopStoryViewModel> topStories;
    private List<StoryViewModel> stories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        String url = "http://news-at.zhihu.com/api/4/news/latest";


        viewModelCreator = ViewModelCreator.getInstance(MyApplication.getMyApplication());
//        DataSupport.deleteAll(TopStoryModel.class);
//        ToastUtils.showLong("删除成功");
        if (!NetworkUtil.isNetworkConnected(MyApplication.getMyApplication())) {
            stories = viewModelCreator.getStoryModel();
            topStories = viewModelCreator.getTopstoryModels();
            tv_hello.setText(topStories.get(0).getTitle() + "  " + stories.get(0).getImages());
        }
//        if (NetworkUtil.isNetworkConnected(MyApplication.getMyApplication()) ||
//                NetworkUtil.isWifiConnected(this) ||
//                NetworkUtil.isMobileConnected(this)) {
//            Log.i("Network状态：", NetworkUtil.isNetworkConnected(MyApplication.getMyApplication()) + "");
//            Log.i("Wifi状态：", NetworkUtil.isWifiConnected(MyApplication.getMyApplication()) + "");
//            Log.i("Mobile状态：", NetworkUtil.isMobileConnected(MyApplication.getMyApplication()) + "");
//
        Request request = new BasicRequest(url);
        CallServer.getInstance().request(0, request, this);
    }


    @Override
    public void onSucceed(int what, Response<BasicBean> response) {
        BasicBean basicBean = response.get();
        if (basicBean != null) {
            if (what == 0) {
                Toast.makeText(MainActivity.this, basicBean.getDate(), Toast.LENGTH_LONG).show();
                //网络获取的列表
                List<TopStory> topStorylist = basicBean.getTopStory();
                List<Story> storyList = basicBean.getStories();
                //设置给本地数据库
                List<TopStoryViewModel> topStoryViewModelList = viewModelCreator.setTopstoryModels(topStorylist);
                List<StoryViewModel> storyViewModelList = viewModelCreator.setStoryModels(storyList);
                if (topStoryViewModelList != null) {
                    topStories = topStoryViewModelList;
                }
                if (storyViewModelList != null) {
                    stories = storyViewModelList;
                }


                tv_hello.setText(topStories.get(0).getTitle() + "  " + stories.get(0).getImages());

                for (StoryViewModel storyViewModel : stories) {
                    Log.i("id", storyViewModel.getId() + "");
                    Log.i("getGa_prefix", storyViewModel.getGa_prefix() + "");
                    Log.i("Images", storyViewModel.getImages() + "");
                    Log.i("title", storyViewModel.getTitle() + "");
                    Log.i("type", storyViewModel.getType() + "");
                }
            }
        }
    }

    @Override
    public void onFailed(int what, Object tag, Exception exception, long networkMills) {
          if(exception!=null){
              if(!TextUtils.isEmpty(exception.getMessage())){
                  ToastUtils.showShort(exception.getMessage());
              }
          }
    }

    private void initView() {
        tv_hello = (TextView) findViewById(R.id.tv_hello);
    }



}
