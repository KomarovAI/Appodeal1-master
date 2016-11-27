package columbainc.appodeal1;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.Toast;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.BannerCallbacks;
import com.appodeal.ads.InterstitialCallbacks;
import com.appodeal.ads.SkippableVideoCallbacks;

public class MainActivity extends Activity {
    boolean pressbtn = false;
    Chronometer chronometr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Appodeal.disableLocationPermissionCheck();
        Appodeal.setTesting(false);
        final String appKey = "3c3b9b3c818087b5f373d491d44638cb4daddfddb7126024";
        Appodeal.initialize(this, appKey, Appodeal.INTERSTITIAL | Appodeal.NON_SKIPPABLE_VIDEO | Appodeal.BANNER | Appodeal.NATIVE);
        chronometr  = (Chronometer) findViewById(R.id.chronometer4);
        chronometr.setBase(SystemClock.elapsedRealtime());
        final Activity main = this;

        if (savedInstanceState == null) {
            Appodeal.show(main, Appodeal.BANNER_TOP);
        }



        final Thread baneerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Appodeal.hide(main, Appodeal.BANNER);
            }
        });

       final Thread chron = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!pressbtn) {
                    Appodeal.initialize(main, appKey, Appodeal.REWARDED_VIDEO);
                    Appodeal.initialize(main, appKey, Appodeal.INTERSTITIAL);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        //
                    }
                    long timeElapsed = SystemClock.elapsedRealtime() - chronometr.getBase();
                            int seconds = (int) (timeElapsed) / 1000;
                    System.out.println(seconds);
                    if (pressbtn) break;
                    if (seconds == 30) {
                        if (pressbtn) break;
                        if (Appodeal.isLoaded(Appodeal.REWARDED_VIDEO)) {

                            Appodeal.show(main, Appodeal.REWARDED_VIDEO);
                            try {
                                Thread.sleep(10000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Appodeal.show(main, Appodeal.INTERSTITIAL);
                            try {
                                Thread.sleep(10000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
        chron.setPriority(2);
        chron.start();

        Appodeal.setBannerCallbacks(new BannerCallbacks() {
            private Toast mToast;

            @Override
            public void onBannerLoaded(int height, boolean isPrecache) {

            }

            @Override
            public void onBannerFailedToLoad() {}

            @Override
            public void onBannerShown() {
                baneerThread.setPriority(1);
                baneerThread.start();
            }

            @Override
            public void onBannerClicked() {
            }

            void showToast(final String text) {
                if (mToast == null) {
                    mToast = Toast.makeText(main, text, Toast.LENGTH_SHORT);
                }
                mToast.setText(text);
                mToast.setDuration(Toast.LENGTH_SHORT);
                mToast.show();
            }
        });

        Appodeal.setInterstitialCallbacks(new InterstitialCallbacks() {
            private Toast mToast;

            @Override
            public void onInterstitialLoaded(boolean isPrecache) {
            }

            @Override
            public void onInterstitialFailedToLoad() {

            }

            @Override
            public void onInterstitialShown() {

            }

            @Override
            public void onInterstitialClicked() {

            }

            @Override
            public void onInterstitialClosed() {
                chronometr.setBase(SystemClock.elapsedRealtime());
            }

            void showToast(final String text) {
                if (mToast == null) {
                    mToast = Toast.makeText(main, text, Toast.LENGTH_SHORT);
                }
                mToast.setText(text);
                mToast.setDuration(Toast.LENGTH_SHORT);
                mToast.show();
            }
        });

        Appodeal.setSkippableVideoCallbacks(new SkippableVideoCallbacks() {
            private Toast mToast;

            @Override
            public void onSkippableVideoLoaded() {
            }

            @Override
            public void onSkippableVideoFailedToLoad() {
            }

            @Override
            public void onSkippableVideoShown() {
            }

            @Override
            public void onSkippableVideoFinished() {
            }

            @Override
            public void onSkippableVideoClosed(boolean finished) {
                chronometr.setBase(SystemClock.elapsedRealtime());
            }

            void showToast(final String text) {
                if (mToast == null) {
                    mToast = Toast.makeText(main, text, Toast.LENGTH_SHORT);
                }
                mToast.setText(text);
                mToast.setDuration(Toast.LENGTH_SHORT);
                mToast.show();
            }
        });
    }

    public void onMyButtonClick(View view)
    {
        pressbtn = true;
        Toast toast = Toast.makeText(getApplicationContext(),
                "Ахтунг, рекламма закончилась!!!1", Toast.LENGTH_SHORT);
        toast.show();
        chronometr.stop();
        chronometr.setBase(SystemClock.elapsedRealtime());
        chronometr.setVisibility(View.INVISIBLE);


    }

    protected void onResume() {
        super.onResume();
        chronometr.start();
        Appodeal.onResume(this, Appodeal.BANNER);
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onPause() {
        super.onPause();
        chronometr.stop();
    }


    }

