package de.kenbun.contract_library.di

import android.app.Application
import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.kenbun.contract_library.resolver.DataContentResolver

@Module
@InstallIn(SingletonComponent::class)
object ContentResolverProvider {

    @Provides
    fun provideDataContentResolver(app: Application): DataContentResolver {

        Log.e("ContentResolverProvider", "provideDataContentResolver")
        return DataContentResolver(
            contentResolver = app.applicationContext.contentResolver,
        )
    }

}