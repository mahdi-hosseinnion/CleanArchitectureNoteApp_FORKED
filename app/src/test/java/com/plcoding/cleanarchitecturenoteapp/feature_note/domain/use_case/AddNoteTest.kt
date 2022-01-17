package com.plcoding.cleanarchitecturenoteapp.feature_note.domain.use_case

import com.plcoding.cleanarchitecturenoteapp.feature_note.data.repository.FakeNoteRepository
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model.InvalidNoteException
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model.Note
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.util.*

class AddNoteTest {

    private lateinit var addNote: AddNote
    private lateinit var fakeRepository: FakeNoteRepository

    @Before
    fun setup() {
        fakeRepository = FakeNoteRepository()
        addNote = AddNote(fakeRepository)
    }

    @Test
    fun `Add note to database, confirm note has inserted`() = runBlocking {
        val noteId = 123
        val note =
            Note(
                title = UUID.randomUUID().toString(),
                content = UUID.randomUUID().toString(),
                timestamp = 1234,
                color = 1,
                id = noteId
            )
        //confirm there is not any note in repository with note id (avoid false-possitive)
        assertNull(fakeRepository.getNoteById(noteId))
        //run use case
        addNote.invoke(note = note)
        //confirm note have actually inserted and is the same as 'note'
        assertEquals(note, fakeRepository.getNoteById(noteId))
    }

    @Test
    fun `Add note with empty title, throw InvalidNoteException`() = runBlocking {
        val expectedExceptionMessage = "The title of the note can't be empty."
        val noteId = 123
        val noteWithEmptyTitle =
            Note(
                title = "",
                content = UUID.randomUUID().toString(),
                timestamp = 1234,
                color = 1,
                id = noteId
            )
        //run use case and catch exception
        val exception = runCatching {
            addNote.invoke(note = noteWithEmptyTitle)
        }.exceptionOrNull()
        //assert exception
        assert(exception is InvalidNoteException)
        assertEquals(expectedExceptionMessage, exception!!.message)
        //confirm note did not inserted
        assertNull(fakeRepository.getNoteById(noteId))
    }

    @Test
    fun `Add note with empty content, throw InvalidNoteException`() = runBlocking {
        val expectedExceptionMessage = "The content of the note can't be empty."
        val noteId = 123
        val noteWithEmptyTitle =
            Note(
                title = UUID.randomUUID().toString(),
                content = "",
                timestamp = 1234,
                color = 1,
                id = noteId
            )
        //run use case and catch exception
        val exception = runCatching {
            addNote.invoke(note = noteWithEmptyTitle)
        }.exceptionOrNull()
        //assert exception
        assert(exception is InvalidNoteException)
        assertEquals(expectedExceptionMessage, exception!!.message)
        //confirm note did not inserted
        assertNull(fakeRepository.getNoteById(noteId))
    }


}