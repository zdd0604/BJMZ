package com.hjnerp.business.BusinessJsonCallBack;

import com.google.gson.Gson;
import com.hjnerp.model.BusinessBillsMessages;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.request.BaseRequest;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;

/**
 * Created by zdd on 2017/1/9.
 */

public abstract class BJsonCallBack<T> extends AbsCallback<T> {

    @Override
    public void onBefore(BaseRequest request) {
        super.onBefore(request);
    }

    @Override
    public T convertSuccess(Response response) throws Exception {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        Type type = params[0];
        Gson gson = new Gson();
        String string = response.body().string();
        BusinessBillsMessages result = gson.fromJson(string, type);
        if (result.getFlag().equals("Y")) {
            return (T) result;
        } else {
            throw new IllegalArgumentException(result.getMessage());
        }
    }
}
