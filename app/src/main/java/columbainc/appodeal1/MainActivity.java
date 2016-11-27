package columbainc.appodeal1;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Chronometer;
import android.widget.Toast;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.BannerCallbacks;
import com.appodeal.ads.InterstitialCallbacks;
import com.appodeal.ads.SkippableVideoCallbacks;

public class MainActivity extends AppCompatActivity {
    boolean pressbtn = false;
    boolean pause = false;
    Chronometer chronometr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

       Thread chron = new Thread(new Runnable() {
            @Override
            public void run() {
                chronometr.start();
                while (!pressbtn) {
                    Appodeal.initialize(main, appKey, Appodeal.REWARDED_VIDEO);
                    Appodeal.initialize(main,appKey,Appodeal.INTERSTITIAL);
                    pause = false;

                    try {
                        Thread.sleep(30000);
                        if (pressbtn) {
                            chronometr.stop();
                            break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                      }
                        chronometr.stop();
                        if (pressbtn) break;
                        if (Appodeal.isLoaded(Appodeal.REWARDED_VIDEO)){

                            Appodeal.show(main, Appodeal.REWARDED_VIDEO);

                        } else {
                            Appodeal.show(main, Appodeal.INTERSTITIAL);
                        }
                        while (!pause) {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
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
                pause=true;
                chronometr.setBase(SystemClock.elapsedRealtime());
                chronometr.start();
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
                pause=true;
                chronometr.setBase(SystemClock.elapsedRealtime());
                chronometr.start();
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

        Appodeal.onResume(this, Appodeal.BANNER);

    }
    }

