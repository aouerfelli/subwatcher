package io.github.aouerfelli.subwatcher

import android.content.Context
import androidx.work.Configuration
import androidx.work.WorkManager
import coil.ImageLoaderBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.github.aouerfelli.subwatcher.ui.MainModule
import io.github.aouerfelli.subwatcher.work.SubwatcherWorkerFactory
import io.github.aouerfelli.subwatcher.work.WorkersModule
import javax.inject.Singleton

@Module(
  includes = [
    MainModule::class,
    WorkersModule::class
  ]
)
abstract class ApplicationModule {

  @Binds
  abstract fun bindContext(application: SubwatcherApplication): Context

  companion object {

    @Provides
    @Singleton
    fun provideWorkManager(context: Context) = WorkManager.getInstance(context)

    @Provides
    fun provideWorkConfiguration(workerFactory: SubwatcherWorkerFactory): Configuration {
      return Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .build()
    }

    @Provides
    @Singleton
    fun provideImageLoader(context: Context) = ImageLoaderBuilder(context).build()
  }
}
