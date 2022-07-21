package com.mood.moodnews.data.posts.impl

import com.mood.moodnews.data.Result
import com.mood.moodnews.data.posts.PostsRepository
import com.mood.moodnews.data.posts.posts
import com.mood.moodnews.model.Post
import com.mood.moodnews.model.PostsFeed
import com.mood.moodnews.utils.addOrRemove
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class FakePostsRepository : PostsRepository {

    // for now, store these in memory
    private val favorites = MutableStateFlow<Set<String>>(setOf())

    //Used to make suspend functions that read and update state safe to call from any thread.
    private val mutex = Mutex()

    override suspend fun getPost(postId: String?): Result<Post> {
        return withContext(Dispatchers.IO) {
            val post = posts.allPosts.find { it.id == postId }
            if (post == null) {
                Result.Error(IllegalArgumentException("Post not found"))
            } else {
                Result.Success(post)
            }
        }
    }

    override suspend fun getPostsFeed(): Result<PostsFeed> {
        return withContext(Dispatchers.IO) {
            delay(800) // pretend we're on a slow network
            if (shouldRandomlyFail()) {
                Result.Error(IllegalStateException())
            } else {
                Result.Success(posts)
            }
        }
    }

    override fun observeFavorites(): Flow<Set<String>> = favorites

    override suspend fun toggleFavorite(postId: String) {
        mutex.withLock {
            val set = favorites.value.toMutableSet()
            set.addOrRemove(postId)
            favorites.value = set.toSet()
        }
    }

    /**
     * Used to drive "random" failure in a predictable pattern.
     *
     * Making the first request always succeed
     */

    private var requestCount = 0

    /**
     * Randomly fail some loads to simulate a real network.
     *
     * This will fail deterministically every 5 requests.
     */
    private fun shouldRandomlyFail(): Boolean = ++requestCount % 5 == 0
}