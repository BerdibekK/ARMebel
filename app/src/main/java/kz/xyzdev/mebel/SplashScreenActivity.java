package kz.xyzdev.mebel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread logoTimer = new Thread()
        {
            public void run()
            {
                try
                {
                    int logoTimer = 0;
                    while(logoTimer < 2000)
                    {
                        sleep(100);
                        logoTimer = logoTimer +100;
                    };
                    startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));
                }
                catch (InterruptedException e)
                {
                    // TODO: автоматически сгенерированный блок catch.
                    e.printStackTrace();
                }
                finally
                {
                    finish();
                }
            }
        };
        logoTimer.start();
    }
}
