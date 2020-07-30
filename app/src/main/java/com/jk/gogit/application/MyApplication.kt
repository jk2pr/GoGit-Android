package com.jk.gogit.application


import android.app.Application
import android.content.Context
import com.jk.gogit.di.components.AppComponent
import com.jk.gogit.di.components.DaggerAppComponent
import com.jk.gogit.di.modules.AppModule
import com.jk.gogit.di.modules.CacheModule
import com.jk.gogit.di.modules.DBModule
import com.jk.gogit.di.modules.NetworkModule


/**
 * Created by Jitendra on 02/08/2017.
 */
class MyApplication : Application() {
    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MyApplication.appComponent = DaggerAppComponent.builder()
                .networkModule(NetworkModule())
                .appModule(AppModule(base))
                .dBModule(DBModule(base))
                .cacheModule(CacheModule(base))
                .build()

    }

    override fun onCreate() {
        super.onCreate()

        /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
             val shortcutManager = getSystemService(ShortcutManager::class.java)
             val webShortcut = ShortcutInfo.Builder(this, "shortcut_web")
                     .setShortLabel("catinean.com")
                     .setLongLabel("Open catinean.com web site")
                     .setIcon(Icon.createWithResource(this, R.drawable.ic_delete))
                     .setIntent(Intent(Intent.ACTION_VIEW, Uri.parse("https://catinean.com")))
                     .build()
             shortcutManager!!.dynamicShortcuts = Collections.singletonList(webShortcut)


         }*/
    }
}