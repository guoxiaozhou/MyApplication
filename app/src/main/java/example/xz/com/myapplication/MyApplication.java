package example.xz.com.myapplication;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.yanzhenjie.nohttp.NoHttp;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackManager;
import example.xz.com.myapplication.Utils.DBUtil;

/**
 * Created by Administrator on 2017/8/23.
 */

public class MyApplication extends LitePalApplication {


    private static MyApplication myApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Application","MyApplication");
        NoHttp.initialize(this);
        myApplication=this;

        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE,"");
        //友盟设置场景模式
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        UMConfigure.setLogEnabled(true);
        MobclickAgent.setDebugMode(true);
        MobclickAgent.setSessionContinueMillis(3*1000);


        BGASwipeBackManager.getInstance().init(this);

        LitePal.initialize(this);
        DBUtil.getInstance();
    }

    public static MyApplication getMyApplication() {
        return myApplication;
    }
}
