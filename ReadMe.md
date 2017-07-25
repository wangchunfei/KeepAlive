# 1像素保活大法

### 1.新建一个Activity，作为1像素页面的主体，我姑且叫它HooliganActivity:

```
	 public class HooliganActivity extends Activity {
	    private static HooliganActivity instance;
	
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        instance = this;
	        Window window = getWindow();
	        window.setGravity(Gravity.LEFT | Gravity.TOP);
	        WindowManager.LayoutParams params = window.getAttributes();
	        params.x = 0;
	        params.y = 0;
	        params.height = 1;
	        params.width = 1;
	        window.setAttributes(params);
	    }
	
	    /**
	     * 开启保活页面
	     */
	    public static void startHooligan() {
	        Intent intent = new Intent(DWApplication.getAppContext(), HooliganActivity.class);
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        DWApplication.getAppContext().startActivity(intent);
	    }
	
	    @Override
	    protected void onDestroy() {
	        super.onDestroy();
	        instance = null;
	    }
	
	    /**
	     * 关闭保活页面
	     */
	    public static void killHooligan() {
	        if(instance != null) {
	            instance.finish();
	        }
	    }
	}
```

### 2.注册清单文件：
```
	<activity android:name=".activity.HooliganActivity"
    android:configChanges="keyboardHidden|orientation|screenSize|navigation|keyboard"
    android:excludeFromRecents="true"
    android:exported="false"
    android:finishOnTaskLaunch="false"
    android:launchMode="singleInstance"
    android:theme="@style/HooliganActivityStyle"/>
```
```
	<style name="HooliganActivityStyle">
        <item name="android:windowBackground">@color/transparent</item>
        <item name="android:windowContentOverlay">@null</item>
        <item name="android:windowIsTranslucent">true</item>
        <item name="android:windowNoDisplay">false</item>
        <item name="android:windowDisablePreview">true</item>
    </style>
```

### 3.监听锁屏和解锁通知，不能静态注册广播，只能动态注册：
```
	IntentFilter filter = new IntentFilter();
	filter.addAction(Intent.ACTION_SCREEN_ON);
	filter.addAction(Intent.ACTION_SCREEN_OFF);
	registerReceiver(new BootCompleteReceiver(),filter);
```

### 4.分别在解锁和锁屏时唤醒我的HooliganActivity：
```
	public class BootCompleteReceiver extends BroadcastReceiver {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
	            HooliganActivity. startHooligan();
	        } else if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
	            HooliganActivity. killHooligan();
	        }
	    }
	}
```

### 5.在最近使用的列表中隐藏应用
	
当按返回键退出应用时，不是
```
	Process.killProcess(Process.myPid());
```
和
```
	System.exit(0);
```
而是
```
	Intent intent = new Intent(Intent.ACTION_MAIN);
	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	intent.addCategory(Intent.CATEGORY_HOME);
	getAppContext().startActivity(intent);
```

并且在清单文件的Activity中加入了

```
	android:excludeFromRecents="true"
```

至此，整个的保活就结束了
这样你在后台每次锁屏，实际上都会吊起一个一像素的页面，假装app在前台，拥有最高进程优先级。


>技术无罪。
                               ——王欣