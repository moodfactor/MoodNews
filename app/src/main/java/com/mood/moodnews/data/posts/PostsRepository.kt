package com.mood.moodnews.data.posts

import com.mood.moodnews.data.Result
import com.mood.moodnews.model.Post
import com.mood.moodnews.model.PostsFeed
import kotlinx.coroutines.flow.Flow

interface PostsRepository {


    /**
     * Get a specific MoodNews post.
     */
    suspend fun getPost(postId: String?): com.mood.moodnews.data.Result<Post>

    /**
     * Get MoodNews posts.
     */
    suspend fun getPostsFeed(): Result<PostsFeed>

    /**
     * Observe the current favorites.
     */
    fun observeFavorites(): Flow<Set<String>>

    /**
     * Toggle a postId to be a favorite or not.
     */
    suspend fun toggleFavorite(postId: String)
}