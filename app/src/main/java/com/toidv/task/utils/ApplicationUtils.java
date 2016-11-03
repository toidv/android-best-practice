package com.toidv.task.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.afollestad.materialdialogs.MaterialDialog;
import com.toidv.task.consts.Consts;
import com.toidv.task.data.model.ApiError;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.ConnectException;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by TOIDV on 4/5/2016.
 */
public class ApplicationUtils {

    public static MaterialDialog createProgress(Context context, String title) {
        return new MaterialDialog.Builder(context)
                .title(title)
                .content("Please wait")
                .progress(true, 0)
                .build();
    }

    public static MaterialDialog createAlertDialog(Context context, String title) {
        return new MaterialDialog.Builder(context)
                .title(title)
                .positiveText("OK")
                .build();
    }


    public static String getError(Throwable e, Retrofit retrofit) {
        String errorMessage = Consts.GENERIC_ERROR;
        if (e instanceof HttpException) {
            ResponseBody body = ((HttpException) e).response().errorBody();
            Converter<ResponseBody, ApiError> responseBodyObjectConverter = retrofit.responseBodyConverter(ApiError.class, new Annotation[0]);
            try {
                ApiError error = responseBodyObjectConverter.convert(body);
                if (error != null) {
                    errorMessage = error.getMessage();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        } else if (e instanceof ConnectException) {
            errorMessage = Consts.SERVER_ERROR;
        }
        return errorMessage;
    }

    public static boolean isConnectivityAvailable(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        return info != null && info.isAvailable() && info.isConnectedOrConnecting();
    }
}
