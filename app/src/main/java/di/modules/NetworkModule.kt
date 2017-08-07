package di.modules

import com.jk.daggerrxkotlin.api.IApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

/**
 * Created by M2353204 on 07/08/2017.
 */

@Module
public class NetworkModule(){
    @Provides
    @Singleton
    fun getRetrofit(): IApi {
        return Retrofit.Builder()
                //http://ip.jsontest.com/?mime=6
                .baseUrl("http://ip.jsontest.com/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(IApi::class.java)

    }
}