package com.mood.moodnews.data.posts

import android.content.Context
import com.mood.moodnews.data.interests.InterestsRepository
import com.mood.moodnews.data.interests.impl.FakeInterestsRepository
import com.mood.moodnews.data.posts.impl.FakePostsRepository

interface AppContainer {
    val postsRepository: PostsRepository
    val interestsRepository: InterestsRepository

}

/**
 * Implementation for the Dependency Injection container at the application level.
 *
 * Variables are initialized lazily and the same instance is shared across the whole app.
 */
class AppContainerImpl(private val applicationContext: Context) : AppContainer {

    override val postsRepository: PostsRepository by lazy {
        FakePostsRepository()
    }
    override val interestsRepository: InterestsRepository by lazy {
        FakeInterestsRepository()
    }


}