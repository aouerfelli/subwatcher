package com.aouerfelli.subwatcher.broadcast

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LifecycleCoroutineScope
import com.aouerfelli.subwatcher.repository.SubredditName
import com.aouerfelli.subwatcher.repository.SubredditRepository
import com.aouerfelli.subwatcher.repository.asUrl
import com.aouerfelli.subwatcher.util.extensions.goAsync
import com.aouerfelli.subwatcher.util.extensions.launch
import dagger.android.DaggerBroadcastReceiver
import timber.log.Timber
import timber.log.warn
import javax.inject.Inject

class ViewSubredditBroadcastReceiver : DaggerBroadcastReceiver() {

  companion object {
    fun createIntent(context: Context, subredditName: SubredditName): Intent {
      return Intent(context, ViewSubredditBroadcastReceiver::class.java)
        // This is to make the intent unique, as extras aren't taken into account for PendingIntents
        // TODO: When min SDK 29 becomes viable, replace action with identifier
        .setAction(subredditName.name)
    }
  }

  @Inject
  lateinit var repository: SubredditRepository

  @Inject
  lateinit var processLifecycleScope: LifecycleCoroutineScope

  override fun onReceive(context: Context, intent: Intent) {
    super.onReceive(context, intent)
    goAsync(processLifecycleScope) {
      val subredditName = intent.action?.let(::SubredditName)
      if (subredditName == null) {
        Timber.warn { "No subreddit name was provided." }
        return@goAsync
      }

      subredditName.asUrl().launch(context, startNewTask = context !is Activity)

      val subreddit = repository.getSubreddit(subredditName)
      if (subreddit == null) {
        Timber.warn { "Subreddit ${subredditName.name} is not in database." }
        return@goAsync
      }
      repository.updateLastPosted(subreddit)
    }
  }
}
