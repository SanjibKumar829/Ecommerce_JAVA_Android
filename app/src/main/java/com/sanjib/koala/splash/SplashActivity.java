package com.sanjib.koala.splash;

import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.sanjib.koala.BaseActivity;
import com.sanjib.koala.Db.AppDatabase;
import com.sanjib.koala.Db.AppPref;
import com.sanjib.koala.main.MainActivity;
import com.sanjib.koala.R;
import com.sanjib.koala.Ui.LoginActivity;
import com.sanjib.koala.networking.AppConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sanjib.koala.R.color.colorAccent;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        setContentView(R.layout.activity_splash2);
        changeStatusBarColor(ContextCompat.getColor(this,colorAccent));
        hideToolbar();

        if (TextUtils.isEmpty(AppPref.getInstance().getAuthToken())) {
            // user token is not present, take him to login screen
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }
        fetchAppConfig();
    }

    private void fetchAppConfig() {
        Call<AppConfig> call = getApi().getAppConfig();
        call.enqueue(new Callback<AppConfig>() {
            @Override
            public void onResponse(Call<AppConfig> call, Response<AppConfig> response) {
                if (!response.isSuccessful()) {
                    handleError(response.errorBody());
                    return;
                }

                // save app config to db
                AppDatabase.saveAppConfig(response.body());

                // fetch products
                fetchProducts();
            }

            @Override
            public void onFailure(Call<AppConfig> call, Throwable t) {
                handleError(t);
            }
        });
    }

    private void fetchProducts() {

        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash2;
    }
}