package di.modules

import android.app.Application
import android.content.Context
import com.jk.daggerrxkotlin.application.MyApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
public class AppModule(val app: MyApplication) {

    @Provides
    @Singleton
    fun provideContext(): Context = app

    @Provides
    @Singleton
    fun provideApplication(): Application = app

}