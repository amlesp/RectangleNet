package com.rectangle.net.data.api;


public interface IApiCallback {

    void onSuccess();

    void onFail(String error);
}
