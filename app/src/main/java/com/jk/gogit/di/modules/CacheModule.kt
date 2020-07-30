package com.jk.gogit.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import java.io.File
import javax.inject.Singleton

/**
 * Created by Jitendra on 08/11/2017.
 */

@Module
class CacheModule(val app: Context) {
    @Provides
    @Singleton
    fun getCacheDir(): Cache {
        val httpCacheDirectory = File(app.cacheDir, "responses")
        return Cache(httpCacheDirectory, 10 * 1024 * 1024)


    }
}
