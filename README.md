epsAndroidPlayer Android SDK使用说明

epsAndroidPlayer SDK是Android 平台上使用的软件开发工具包(SDK), 负责播放视频直播和点播内容。

一. 功能特点
•	  音频编码：AAC
•	  视频编码：H.264
•	  播放流协议：RTMP, HLS, HTTP

二. 运行环境
•	最低支持版本为Android 4.2 (API level 17)
•	支持的cpu架构：armv5,armv7a,arm64v8a,x86,x86_64

三. 快速集成
本章节提供一个快速集成推流SDK基础功能的示例。
具体可以参考app demo工程中的相应文件。

3.1 下载工程
3.1.1 github下载 从github下载SDK及demo工程

3.2 工程目录结构
•	appdemo: 示例工程，演示本SDK主要接口功能的使用
•	doc: SDK说明文档
•	libs: 集成SDK需要的所有库文件()
o	libs/EIL_nativeplayersdk-debug.aar: Android Studio aar包
o	libs/EIL_nativeplayersdk-release.aar: Android Studio aar包

3.3 配置项目
引入目标库, 将libs目录下的库文件引入到目标工程中并添加依赖。
可参考下述配置方式（以android studio为例）：
•	将libs目录copy到目标工程app目录下；
- 在AndroidManifest.xml文件中申请相应权限
````xml
<!-- 使用权限 -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />

3.4 简单播放示例
具体可参考demo工程中的com.ej.demop类
•	在布局文件中加入预览View
•	<com.example.ejplayer.myplaysdk.widget.media.IjkVideoView
    android:id="@+id/video_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
</com.example.ejplayer.myplaysdk.widget.media.IjkVideoView
•	
//创建播放管理类EILPlayerManager
player = new EILPlayerManager(this,R.id.video_view);

// 设置默认全屏模式，设置播放事件监听，返回播放信息。
player.setFullScreenOnly(true);
player.setScaleType(EILPlayerManager.SCALETYPE_FITXY);
player.playInFullScreen(true);

player.setPlayerStateListener(this);
//监听按钮响应事件 播放输入的url
public void clickTogglePlay(@SuppressWarnings("unused") View unused) {
    if(mRecordingEnabled) {
        player.stop();
        mRecordingEnabled=!mRecordingEnabled;
    }
    if(!mRecordingEnabled)
    {
        player.setFullScreenOnly(true);
        player.setScaleType(EILPlayerManager.SCALETYPE_FITXY);
        player.playInFullScreen(true);
        player.setPlayerStateListener(this);
        EditText et = (EditText)findViewById(R.id.editText);
        String s = et.getText().toString();
        player.play(s);
        mRecordingEnabled=!mRecordingEnabled;
    }
}
四. 接口说明

EILPlayerManager(final Activity activity, View view) 创建播放器实例

void play(String url) 开始播放

void pause() 暂停播放

void onResume() 恢复播放

void stop() 停止播放

void setVolume(float percent)设置音量 音量参数范围为0.0-1.0

boolean isPlaying() 检查是否正在播放

void seekto(int msec) 点播时跳转到第msec（毫秒）处播放

int getCurrentPosition()获取当前播放位置

int getDuration() 获取媒体文件总时间长度、

void setFullScreenOnly(boolean fullScreenOnly)是否以全屏模式播放

void setScaleType(String scaleType)设定画面比例和缩放方式

boolean onTouch(View v, MotionEvent event)屏幕触控控制音量，亮度的高级功能




