package eu.nanooq.otkd.di.modules

import dagger.Module
import dagger.Provides
import eu.nanooq.otkd.apiService.IOtkdService
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.Retrofit
import okhttp3.OkHttpClient
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import javax.inject.Named
import java.util.concurrent.TimeUnit


/**
 *
 * Created by rd on 26/07/2017.
 */
@Module
class RetrofitModule {

    companion object {
        const val RETROFIT_MOSHI_SERVICE = "retrofit"
        const val RETROFIT_MOSHI_NORMAL_TIMEOUT = "retrofit_otkd_service"
        const val CLIENT_NORMAL_TIMEOUT = "okhttp_client"
        const val BASE_URL = "http://www.odtatierkdunaju.sk/"
        const val TIMEOUT_NORMAL = 10L
    }

    @Provides
    @Named(RETROFIT_MOSHI_SERVICE)
    fun provideOtkdServiceWithMoshi(
            @Named(RETROFIT_MOSHI_NORMAL_TIMEOUT) retrofit: Retrofit
    ): IOtkdService {
        return retrofit.create<IOtkdService>(IOtkdService::class.java)
    }
    @Provides
    @Named(RETROFIT_MOSHI_NORMAL_TIMEOUT)
    fun provideRetrofitMoshiNormal(
            @Named(CLIENT_NORMAL_TIMEOUT) client: OkHttpClient

    ): Retrofit {
        return createRetrofitWithMoshiConverter(client, BASE_URL)
    }


    /**
     * Creates [Retrofit] with specified client, base URL.
     * and MoshiConverter
     * @param client OkHttp client.
     * *
     * @param baseUrl Server base URL.
     * *
     * @return Instance of [Retrofit].
     */
    private fun createRetrofitWithMoshiConverter(client: OkHttpClient,
                                                 baseUrl: String): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
    }

    @Provides
    @Singleton
    @Named(CLIENT_NORMAL_TIMEOUT)
    fun provideClientNormal(): OkHttpClient {
        return createClient( TIMEOUT_NORMAL)
    }

    /**
     * Creates [OkHttpClient]

     * @return Instance of [OkHttpClient].
     */
    private fun createClient(timeout: Long): OkHttpClient {

        return OkHttpClient.Builder()
//                .connectTimeout(timeout, TimeUnit.MILLISECONDS)
//                .readTimeout(timeout, TimeUnit.MILLISECONDS)
//                .writeTimeout(timeout, TimeUnit.MILLISECONDS)
                .build()
    }
}