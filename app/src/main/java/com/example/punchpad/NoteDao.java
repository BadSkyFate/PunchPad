package com.example.punchpad;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {

    // Insert a single note
    @Insert
    long insert(Note note);

    // Insert multiple notes (optional, for batch operations)
    @Insert
    List<Long> insertAll(List<Note> notes);

    // Update an existing note
    @Update
    int update(Note note);

    // Update multiple notes (optional)
    @Update
    int updateAll(List<Note> notes);

    // Delete a single note
    @Delete
    int delete(Note note);

    // Fetch all notes, ordered by timestamp (DESC)
    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    LiveData<List<Note>> getAllNotes();

    // Fetch notes by folder ID
    @Query("SELECT * FROM notes WHERE folder_id = :folderId ORDER BY timestamp DESC")
    LiveData<List<Note>> getNotesByFolderId(int folderId);

    // Fetch the latest notes for a folder
    @Query("SELECT content FROM notes WHERE folder_id = :folderId ORDER BY timestamp DESC LIMIT :limit")
    List<String> getLatestNotesByFolder(int folderId, int limit);

    // Fetch a single note by its ID (LiveData for reactivity)
    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    LiveData<Note> getNoteById(int id);

    // Fetch a single note by its ID (Synchronous)
    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    Note getNoteByIdSync(int id);

    // Search notes by content
    @Query("SELECT * FROM notes WHERE content LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    LiveData<List<Note>> searchNotes(String query);

    // Delete all notes
    @Query("DELETE FROM notes")
    void deleteAllNotes();

    // Delete all notes by folder ID
    @Query("DELETE FROM notes WHERE folder_id = :folderId")
    int deleteNotesByFolderId(int folderId);
}
