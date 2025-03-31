package com.example.punchpad;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoteRepository {

    private final NoteDao noteDao;
    private final ExecutorService executorService;

    public NoteRepository(Application application) {
        NoteDatabase database = NoteDatabase.getInstance(application);
        noteDao = database.noteDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    // Fetch notes by folder ID (Reactive)
    public LiveData<List<Note>> getNotesByFolderId(int folderId) {
        return noteDao.getNotesByFolderId(folderId);
    }

    // Fetch all notes (Reactive)
    public LiveData<List<Note>> getAllNotes() {
        return noteDao.getAllNotes();
    }

    // Insert a note
    public void insert(Note note) {
        executorService.execute(() -> noteDao.insert(note));
    }
}
