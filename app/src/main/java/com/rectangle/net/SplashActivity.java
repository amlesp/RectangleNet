package com.rectangle.net;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.rectangle.net.data.api.GetColorsTask;
import com.rectangle.net.data.api.IApiCallback;
import com.rectangle.net.data.db.AppDatabase;
import com.rectangle.net.data.db.AppExecutors;
import com.rectangle.net.data.models.ColorModel;
import com.rectangle.net.utils.NetworkUtils;

import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private AppDatabase mDb;
    private List<ColorModel> colorModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mDb = AppDatabase.getInstance(this);

        colorModels = mDb.dataDao().getColorModelList();

        if (!NetworkUtils.checkConnectivity(this)) {
            if (colorModels != null && colorModels.size() > 0) {
                navigateToMainActivity();
            } else {
                Toast.makeText(this, R.string.check_internet, Toast.LENGTH_LONG).show();
                finish();
            }

        } else {
            new GetColorsTask(SplashActivity.this, new IApiCallback() {
                @Override
                public void onSuccess() {
                    navigateToMainActivity();

                }

                @Override
                public void onFail(String error) {
                    Toast.makeText(SplashActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
