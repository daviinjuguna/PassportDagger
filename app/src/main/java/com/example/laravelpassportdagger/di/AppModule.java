package com.example.laravelpassportdagger.di;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.laravelpassportdagger.BuildConfig;
import com.example.laravelpassportdagger.R;
import com.example.laravelpassportdagger.baseviews.BaseApplication;
import com.example.laravelpassportdagger.utils.Tls12SocketFactory;
import com.example.laravelpassportdagger.utils.TokenManager;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.Cache;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;
import javax.net.ssl.SSLContext;

import dagger.Module;
import dagger.Provides;
import okhttp3.CacheControl;
import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    private static final String TAG = "AppModule";
    private static final String HEADER_PRAGMA = "Pragma";
    private static final String HEADER_CACHE_CONTROL = "Cache-Control" ;
    private static final long cacheSize = 10*1024*1024;  //10mb
    private static final String BASE_URL = "https://592bd338d0bb.ngrok.io/api/";

    @Singleton
    @Provides
    static RequestOptions provideRequestOption(){
        return RequestOptions
                .placeholderOf(R.drawable.white_background)
                .error(R.drawable.white_background);
    }

    @Singleton
    @Provides
    static RequestManager provideGlideInstance(Application application, RequestOptions requestOptions){
        return Glide.with(application)
                .setDefaultRequestOptions(requestOptions);
    }

    @Singleton
    @Provides
    static Drawable provideAppDrawable(Application application){
        return ContextCompat.getDrawable(application, R.drawable.user);
    }

    @Singleton
    @Provides
    Retrofit provideRetrofitInstance(){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    @Singleton
    @Provides
    Picasso providePicasso(Application application, OkHttpClient client){
        return new Picasso.Builder(application)
                .build();
    }

    //hii method nimeshindwa kuitoa
    private static OkHttpClient.Builder enableTls12OnPreLollipop(OkHttpClient.Builder client) {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 22) {
            try {
                SSLContext sc = SSLContext.getInstance("TLSv1.2");
                sc.init(null, null, null);
                client.sslSocketFactory(new Tls12SocketFactory(sc.getSocketFactory()));

                ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TlsVersion.TLS_1_2)
                        .build();

                List<ConnectionSpec> specs = new ArrayList<>();
                specs.add(cs);
                specs.add(ConnectionSpec.COMPATIBLE_TLS);
                specs.add(ConnectionSpec.CLEARTEXT);

                client.connectionSpecs(specs);
            } catch (Exception exc) {
                Log.d(TAG, "enableTls12OnPreLollipop: ");
            }
        }

        return client;
    }


    @Singleton
    @Provides
    OkHttpClient getNewHttpClient() {

        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .followRedirects(true)
                .followSslRedirects(true)
                .retryOnConnectionFailure(true)
                .cache(cache())
                .addInterceptor(chain -> {
                    Request request = chain.request();

                    Request.Builder builder = request.newBuilder()
                            .addHeader("Accept", "application/json")
                            .addHeader("Connection", "close");

                    request = builder.build();

                    return chain.proceed(request);
                })
                .addInterceptor(httpLoggingInterceptor())//used if network is off or on
                .addNetworkInterceptor(networkInterceptor()) //only used if network is on
                .addInterceptor(offlineInterceptor()) //only used if network is off
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(300, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG){

            client.addNetworkInterceptor(new StethoInterceptor());

        }

        return enableTls12OnPreLollipop(client).build();
    }

    private static okhttp3.Cache cache(){

        return new Cache(new File(BaseApplication.getInstance().getCacheDir(), "AppMobileCache"), cacheSize);

    }

    private static HttpLoggingInterceptor httpLoggingInterceptor(){


        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        return logging;
    }

    private static Interceptor offlineInterceptor(){

        return chain -> {

            Request request = chain.request();

            //prevents caching when network is on.

            if (!BaseApplication.hasNetwork()) {

                CacheControl cacheControl = new CacheControl.Builder()
                        .maxStale(7, TimeUnit.DAYS)
                        .build();

                request = request.newBuilder()
                        .removeHeader(HEADER_PRAGMA)
                        .removeHeader(HEADER_CACHE_CONTROL)
                        .cacheControl(cacheControl)
                        .build();

            }

            return chain.proceed(request);

        };

    }

    private static Interceptor networkInterceptor(){

        return (chain) -> {

            Response response = chain.proceed(chain.request());

            CacheControl cacheControl = new CacheControl.Builder()
                    .maxAge(10, TimeUnit.SECONDS)
                    .build();


            return response.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                    .build();

        };

    }

    @Singleton
    @Provides
    public static TokenManager getTokenManager(Application application){

        return BaseApplication.getInstance().tokenManager();

    }

    private Gson myGson(){

        return new GsonBuilder()
                .enableComplexMapKeySerialization()
                .serializeNulls()
                .setDateFormat(DateFormat.LONG)
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setPrettyPrinting()
                .setVersion(1.0)
                .create();

    }
}
