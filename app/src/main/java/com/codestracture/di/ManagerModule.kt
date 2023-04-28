package com.codestracture.di

import com.codestracture.data.manager.location.LocationManager
import com.codestracture.data.manager.location.LocationManagerImpl
import com.codestracture.data.manager.preference.PreferenceManager
import com.codestracture.data.manager.preference.PreferenceManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ManagerModule {

    @Binds
    @Singleton
    abstract fun provideLocationManager(locationManagerImpl: LocationManagerImpl): LocationManager

    @Binds
    @Singleton
    abstract fun providePreferenceManager(preferenceManagerImpl: PreferenceManagerImpl): PreferenceManager
}