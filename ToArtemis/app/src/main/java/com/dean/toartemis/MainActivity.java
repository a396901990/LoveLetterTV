package com.dean.toartemis;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dean.toartemis.factory.ImageNameFactory;
import com.dean.toartemis.util.DeviceInfo;
import com.dean.toartemis.util.ImageUtils;
import com.dean.toartemis.view.bluesnow.FlowerView;
import com.dean.toartemis.view.heart.HeartLayout;
import com.dean.toartemis.view.typewriter.TypeTextView;
import com.dean.toartemis.view.whitesnow.SnowView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends Activity {

    private static final String LOVE = "心动是等你的留言，渴望是常和你见面，甜蜜是和你小路流连，温馨是看着你清澈的双眼，爱你的感觉真的妙不可言！";
    private static final int SNOW_BLOCK = 1;

    private FlowerView mBlueSnowView;//蓝色的雪花
    private Handler mHandler = new Handler() {
        public void dispatchMessage(Message msg) {
            MainActivity.this.mBlueSnowView.inva();
        }
    };
    private HeartLayout mHeartLayout;//垂直方向的漂浮的红心

    private Random mRandom = new Random();
    private Random mRandom2 = new Random();
    private TimerTask mTask = null;
    private TypeTextView mTypeTextView;//打字机
    private WebSettings mWebSettings;

    private SnowView mWhiteSnowView;//白色的雪花
    private Timer myTimer = null;
    private ImageView heartBtn = null;
    private MediaPlayer mediaPlayer = new MediaPlayer();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeviceInfo.getInstance().initializeScreenInfo(this);
        setContentView(R.layout.activity_main);
        initView();
        bindListener();
        initMediaPlayer();
        delayShowAll(3000L);
        showAllViews();
    }


    private FrameLayout mWebViwFrameLayout = null;
    private FrameLayout root_fragment_layout = null;
    private TextView textview = null;

    private void initMediaPlayer() {
        try {
            AssetManager assetManager = getAssets();
            AssetFileDescriptor fileDescriptor = assetManager.openFd("test.mp3");
            mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),fileDescriptor.getStartOffset(),
                    fileDescriptor.getStartOffset());
            mediaPlayer.setLooping(true);//设置为循环播放
            mediaPlayer.prepare();//初始化播放器MediaPlayer
//            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        Banner banner = (Banner) findViewById(R.id.banner);
        banner.setImageLoader(new GlideImageLoader());
        ArrayList<String> images = ImageUtils.getAssetsImageNamePathList(MainActivity.this.getApplicationContext(), "dongyu");
        banner.setImages(images);
        banner.setBannerAnimation(Transformer.ForegroundToBackground);
        banner.isAutoPlay(true);
        banner.setDelayTime(3000);
        banner.start();

        heartBtn= findViewById(R.id.imageBtn);

        root_fragment_layout = (FrameLayout) findViewById(R.id.root_fragment_layout);

        FrameLayout.LayoutParams fp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        fp.gravity = Gravity.CENTER;

        this.mHeartLayout = (HeartLayout) findViewById(R.id.heart_o_red_layout);
        this.mTypeTextView = (TypeTextView) findViewById(R.id.typeTextView);
        this.mWhiteSnowView = (SnowView) findViewById(R.id.whiteSnowView);

        this.mBlueSnowView = (FlowerView) findViewById(R.id.flowerview);
        this.mBlueSnowView.setWH(DeviceInfo.mScreenWidthForPortrait, DeviceInfo.mScreenHeightForPortrait, DeviceInfo.mDensity);
        this.mBlueSnowView.loadFlower();
        this.mBlueSnowView.addRect();
        this.myTimer = new Timer();
        this.mTask = new TimerTask() {
            public void run() {
                Message msg = new Message();
                msg.what = MainActivity.SNOW_BLOCK;
                MainActivity.this.mHandler.sendMessage(msg);
            }
        };
        this.myTimer.schedule(this.mTask, 3000, 10);
        clickEvent();
        this.mTypeTextView.setOnTypeViewListener(new TypeTextView.OnTypeViewListener() {
            public void onTypeStart() {
                MainActivity.this.mWhiteSnowView.setVisibility(View.GONE);
            }

            public void onTypeOver() {
                delayShowTheSnow();
            }
        });
        this.mTypeTextView.setText("");
    }

    private void bindListener() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
        mediaPlayer.stop();
        unBindDrawables(findViewById(R.id.root_fragment_layout));
        System.gc();
    }

    private void cancelTimer() {
        if (this.myTimer != null) {
            this.myTimer.cancel();
            this.myTimer = null;
        }
        if (this.mTask != null) {
            this.mTask.cancel();
            this.mTask = null;
        }
    }

    private void showAllViews() {
        this.mWhiteSnowView.setVisibility(View.VISIBLE);
        this.heartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delayDo();
            }
        });
    }

    private void clickEvent() {

    }

    private void gotoNext() {
//        THIS.MWEBVIEW.LOADURL(URL);

//        DELAYDO();
    }

    private void delayShow(long time) {
        Observable.timer(time, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    public void onCompleted() {
                        MainActivity.this.mTypeTextView.start(MainActivity.LOVE);
                    }

                    public void onError(Throwable e) {
                    }

                    public void onNext(Long aLong) {
                    }
                });
    }


    private void delayShowTheSnow() {
        Observable.timer(200, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    public void onCompleted() {
                        mBlueSnowView.setVisibility(View.VISIBLE);
                        MainActivity.this.showRedHeartLayout();
                    }

                    public void onError(Throwable e) {
                    }

                    public void onNext(Long aLong) {
                    }
                });

    }

    private void delayDo() {
        Observable.timer(0, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    public void onCompleted() {
                        MainActivity.this.mTypeTextView.setVisibility(View.VISIBLE);
                        MainActivity.this.delayShow(1000);//延时显示显示打印机
                    }

                    public void onError(Throwable e) {
                    }

                    public void onNext(Long aLong) {
                    }
                });
    }

    private void delayShowAll(long time) {
        Observable.timer(time, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    public void onCompleted() {
                        MainActivity.this.gotoNext();
                    }

                    public void onError(Throwable e) {
                    }

                    public void onNext(Long aLong) {
                    }
                });
    }

    private int randomColor() {
        return Color.rgb(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255));
    }

    private void showRedHeartLayout() {
        Observable.timer(400, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    public void onCompleted() {
                        mHeartLayout.setVisibility(View.VISIBLE);
                        MainActivity.this.delayDo2();
                    }

                    public void onError(Throwable e) {
                    }

                    public void onNext(Long aLong) {
                    }
                });
    }

    private void delayDo2() {
        Observable.timer((long) this.mRandom2.nextInt(200), TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    public void onCompleted() {
                        MainActivity.this.mHeartLayout.addHeart(MainActivity.this.randomColor());
                        MainActivity.this.delayDo2();
                    }

                    public void onError(Throwable e) {
                    }

                    public void onNext(Long aLong) {

                    }
                });
    }


    /**
     * remove View Drawables
     *
     * @param view
     */
    private void unBindDrawables(View view) {
        if (view != null) {
            try {
                Drawable drawable = view.getBackground();
                if (drawable != null) {
                    drawable.setCallback(null);
                } else {
                }
                if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
                    ViewGroup viewGroup = (ViewGroup) view;
                    int viewGroupChildCount = viewGroup.getChildCount();
                    for (int j = 0; j < viewGroupChildCount; j++) {
                        unBindDrawables(viewGroup.getChildAt(j));
                    }
                    viewGroup.removeAllViews();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
