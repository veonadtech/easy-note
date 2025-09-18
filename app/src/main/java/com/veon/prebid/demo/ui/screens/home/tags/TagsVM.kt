package com.veon.prebid.demo.ui.screens.home.tags

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.veon.prebid.demo.R
import com.veon.prebid.demo.data.datasource.DummyNotesData
import com.veon.prebid.demo.data.repositories.TagsRepo
import com.veon.prebid.demo.data.utils.Result
import com.veon.prebid.demo.ui.utils.State
import com.veon.prebid.demo.ui.utils.UiText
import kotlinx.coroutines.launch

class TagsVM : ViewModel() {

    private val tagsRepo = TagsRepo

    var uiState by mutableStateOf(UiState(state = State.Idle()))
        private set

    init {

        onEvent(Events.Load)
    }

    private fun loadTags() {
        viewModelScope.launch {
            uiState = uiState.copy(
                state = State.Loading(),
                message = UiText.ResString(R.string.loading_msg, "Tags")
            )
            launch { tagsRepo.get() }

            tagsRepo.tags.collect { noteResult ->
                when (noteResult) {
                    is Result.Idle -> Unit

                    is Result.Failed -> {
                        uiState = uiState.copy(
                            state = State.Error(
                                reason = State.Error.Reason.UnClassified,
                                message = UiText.NetworkString(noteResult.message)
                            )
                        )
                    }

                    is Result.Success -> {
                        val tags = noteResult.data!!
                        uiState = when {
                            tags.isEmpty() -> {
                                uiState.copy(
                                    state = State.Error(
                                        reason = State.Error.Reason.EmptyData
                                    ),
                                    message = UiText.ResString(R.string.tag_create)
                                )
                            }

                            else -> {
                                uiState.copy(
                                    state = State.Success(null),
                                    tags = tags,
                                    message = UiText.ResString(R.string.tags_list_success_message)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun onEvent(event: Events) {
        viewModelScope.launch {
            uiState = uiState.copy(event = event)
            when (event) {
                is Events.Load -> loadTags()
            }
        }
    }

    sealed interface Events {
        object Load : Events

        class Delete(var tagId: String)
    }

    data class UiState(
        val state: State<Any?> = State.Idle(),
        val tags: List<DummyNotesData.Tag> = emptyList(),
        val message: UiText = UiText.None,
        val event: Events = Events.Load
    )


}
