package com.rectangle.net.data.api;

import android.content.Context;
import android.util.Log;

import com.rectangle.net.data.db.AppDatabase;
import com.rectangle.net.data.models.ColorModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetColorsTask {

    private static final String TAG = GetColorsTask.class.getSimpleName();

    private Context context;
    private IApiCallback mListener;

    public GetColorsTask(Context context, IApiCallback mListener) {
        this.context = context;
        this.mListener = mListener;
        call();
    }

    private void call() {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        appDatabase.dataDao().deleteColorTable();

        IApiEndpoints getColorsService = RetrofitClient.getClient(context).create(IApiEndpoints.class);
        Call<List<ColorModel>> call = getColorsService.getColorList();
        call.enqueue(new Callback<List<ColorModel>>() {
            @Override
            public void onResponse(Call<List<ColorModel>> call, Response<List<ColorModel>> response) {
                Log.d(TAG, "onResponseS: " + String.valueOf(response.body().size()));
                AppDatabase appDatabase = AppDatabase.getInstance(context);
                List<ColorModel> colorModels = response.body();
                Log.d(TAG, "onResponse: " + String.valueOf(colorModels.size()));
                for (ColorModel color : colorModels) {
                    appDatabase.dataDao().insertColor(color);
                }
                mListener.onSuccess();
            }

            @Override
            public void onFailure(Call<List<ColorModel>> call, Throwable t) {
                Log.d(TAG, "onResponseF: " + t.toString());
                mListener.onFail(t.toString());

            }
        });
    }
}
