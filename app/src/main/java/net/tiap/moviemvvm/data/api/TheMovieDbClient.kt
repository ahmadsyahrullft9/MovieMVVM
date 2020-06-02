package net.tiap.moviemvvm.data.api

import net.tiap.moviemvvm.BuildConfig
import net.tiap.moviemvvm.data.Conf
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object TheMovieDbClient {

    fun getClien(): TheMovieDbInterface {

        val requestInterceptor = Interceptor { chain ->
            val url = chain.request()
                .url()
                .newBuilder()
                .addQueryParameter("api_key", Conf.API_KEY)
                .build()
            val request = chain.request()
                .newBuilder()
                .url(url)
                .build()
            return@Interceptor chain.proceed(request)
        }

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val spec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .tlsVersions(TlsVersion.TLS_1_2)
            .cipherSuites(
                CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
            )
            .build()
        val specs = listOf(spec)

        var okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .connectionSpecs(specs)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
        if (BuildConfig.DEBUG) {
            okHttpClient = OkHttpClient.Builder()
                .connectionSpecs(specs)
                .addInterceptor(requestInterceptor)
                .addInterceptor(logging)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build()
        }

        return Retrofit.Builder()
            .baseUrl(Conf.BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TheMovieDbInterface::class.java)
    }
}