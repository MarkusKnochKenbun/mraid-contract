package de.kenbun.contract_library.di

import android.app.Application
import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.kenbun.contract_library.intent.IntentManager

@Module
@InstallIn(SingletonComponent::class)
object IntentManagerProvider {

    @Provides
    fun provideIntentManager(app: Application): IntentManager {

        Log.e("IntentManagerProvider", "provideIntentManager")
        return IntentManager(
            context = app.applicationContext,
        )
    }

}