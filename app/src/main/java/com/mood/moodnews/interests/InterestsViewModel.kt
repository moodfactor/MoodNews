package com.mood.moodnews.interests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mood.moodnews.data.interests.InterestsRepository
import com.mood.moodnews.data.interests.InterestsSection
import com.mood.moodnews.data.interests.TopicSelection
import com.mood.moodnews.data.successOr
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * UI state for teh Interests screen
 */
data class InterestsUiState(
    val topics: List<InterestsSection> = emptyList(),
    val people: List<String> = emptyList(),
    val publications: List<String> = emptyList(),
    val loading: Boolean = false,
)

class InterestsViewModel(
    private val interestsRepository: InterestsRepository
) : ViewModel() {


    // UI state exposed to UI
    private val _uiState = MutableStateFlow(InterestsUiState(loading = true))
    val uiState: StateFlow<InterestsUiState>
        get() = _uiState.asStateFlow()

    val selectedTopics = interestsRepository.observeTopicsSelected().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptySet()
    )

    val selectedPeople = interestsRepository.observePeopleSelected().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptySet()
    )

    val selectedPublications = interestsRepository.observePublicationSelected().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptySet()
    )

    init {
        refreshAll()
    }

    fun toggleTopicSelection(topic: TopicSelection) {
        viewModelScope.launch {
            interestsRepository.toggleTopicSelection(topic)
        }
    }

    fun togglePersonSelected(person: String) {
        viewModelScope.launch {
            interestsRepository.togglePersonSelected(person)
        }
    }

    fun togglePublicationSelected(publication: String) {
        viewModelScope.launch {
            interestsRepository.togglePublicationSelected(publication)
        }
    }

    private fun refreshAll() {
        _uiState.update {
            it.copy(loading = true)
        }

        viewModelScope.launch {
            // Trigger repository requests in parallel
            val topicsDeferred = async {
                interestsRepository.getTopics()
            }
            val peopleDeferred = async {
                interestsRepository.getPeople()
            }
            val publicationsDeferred = async {
                interestsRepository.getPublications()
            }

            // Wait for all requests to finish
            val topics = topicsDeferred.await().successOr(emptyList())
            val people = peopleDeferred.await().successOr(emptyList())
            val publications = publicationsDeferred.await().successOr(emptyList())

            _uiState.update {
                it.copy(
                    loading = false,
                    topics = topics,
                    people = people,
                    publications = publications
                )
            }
        }

    }

    /**
     * Factory for [InterestsViewModel] that takes PostsRepository as a dependency
     */
    companion object {
        fun provideFactory(
            interestsRepository: InterestsRepository,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return InterestsViewModel(interestsRepository) as T
            }

        }
    }

}