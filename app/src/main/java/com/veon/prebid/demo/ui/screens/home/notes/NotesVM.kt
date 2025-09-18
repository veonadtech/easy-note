package com.veon.prebid.demo.ui.screens.home.notes

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.veon.prebid.demo.R
import com.veon.prebid.demo.data.datasource.DummyNotesData
import com.veon.prebid.demo.data.repositories.NotesRepo
import com.veon.prebid.demo.data.utils.Result
import com.veon.prebid.demo.ui.utils.State
import com.veon.prebid.demo.ui.utils.UiText
import kotlinx.coroutines.launch

class NotesVM : ViewModel() {

    companion object {
        private const val TAG = "NOTES_VM"
    }

    private val notesRepo = NotesRepo

    var uiState by mutableStateOf(UiState(state = State.Idle()))
        private set

    init {
        uiState = uiState.copy(
            greeting = UiText.ResString(R.string.user_greeting, R.string.user_greeting_noon)
        )

        viewModelScope.launch {
            notesRepo.notes.collect { noteResult ->
                when (noteResult) {
                    is Result.Idle -> Unit

                    is Result.Failed -> {

                        Log.e(TAG, "loadNotes: M -> ${noteResult.message}")
                        uiState = uiState.copy(
                            state = State.Error(
                                reason = State.Error.Reason.UnClassified,
                                message = UiText.NetworkString(noteResult.message)
                            )
                        )
                    }

                    is Result.Success -> {
                        val notes = noteResult.data!!
                        uiState = when {
                            notes.isEmpty() -> {
                                uiState.copy(
                                    state = State.Error(
                                        reason = State.Error.Reason.EmptyData
                                    ),
                                    message = UiText.ResString(R.string.note_create)
                                )
                            }

                            else -> {
                                uiState.copy(
                                    state = State.Success(null),
                                    notes = notes,
                                    message = UiText.ResString(if (notes.size > 1) R.string.notes_list_success_message else R.string.note_list_success_message)
                                )
                            }
                        }
                    }
                }
            }
        }

        loadNotes()

    }

    private fun loadNotes() {
        viewModelScope.launch {
            Log.e(TAG, "loadNotes: Requested")
            uiState = uiState.copy(
                state = State.Loading(),
                message = UiText.ResString(R.string.loading_msg, "Notes")
            )
            launch { notesRepo.get() }
        }
    }

    data class UiState(
        val greeting: UiText = UiText.None,
        val state: State<Any?> = State.Idle(),
        val notes: List<DummyNotesData.Note> = listOf(),
        val message: UiText = UiText.None,
    )

    override fun onCleared() {
        super.onCleared()
        Log.e(TAG, "onCleared: ")
    }

}
