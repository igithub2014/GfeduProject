package jc.cici.android.atom.http;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 定义retrofit网络请求管理
 * Created by atom on 2017/4/18.
 */

public class RetrofitOKManager {

    /**
     * 创建单利模式
     **/
    private volatile static RetrofitOKManager INSTANCE;

    private RetrofitOKManager() {

    }

    public static RetrofitOKManager getinstance() {
        if (null == INSTANCE) {
            synchronized (RetrofitOKManager.class) {
                if (null == INSTANCE) {
                    INSTANCE = new RetrofitOKManager();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * htttp处理
     */
    public Retrofit doBaseRetrofit(String baseUrl) {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(
                new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.i("RetrofitLog","retrofitBack = "+message);
                    }
                });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // 手动创建http请求
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor);
        builder.connectTimeout(5, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
        return  retrofit;
    }
}
