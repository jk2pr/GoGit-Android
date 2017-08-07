package di.components

import android.app.Activity
import com.jk.daggerrxkotlin.DataFragment
import dagger.Component
import di.modules.AppModule
import di.modules.NetworkModule
import javax.inject.Singleton

/**
 * Created by M2353204 on 07/08/2017.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class,NetworkModule::class))
 interface AppComponent {
    fun inject(app: Activity)
    fun inject(dataFragment: DataFragment)
}