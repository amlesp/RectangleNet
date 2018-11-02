package com.rectangle.net.data.api;

import com.rectangle.net.data.models.ColorModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IApiEndpoints {

    @GET("/gEhgzs")
    Call<List<ColorModel>> getColorList();
}
