package com.mood.moodnews.data.posts

import android.content.Context
import com.mood.moodnews.data.posts.impl.FakePostsRepository

interface AppContainer {
    val postsRepository: PostsRepository

}

/**
 * Implementation for the Dependency Injection container at the application level.
 *
 * Variables are initialized lazily and the same instance is shared across the whole app.
 */
class AppContainerImp(private val applicationContext: Context) : AppContainer {

    override val postsRepository: PostsRepository by lazy {
        FakePostsRepository()
    }


}