package com.example.punchpad;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private final NoteRepository noteRepository;
    private final FolderRepository folderRepository;
    private final LiveData<List<Note>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        noteRepository = new NoteRepository(application);
        folderRepository = new FolderRepository(application);
        allNotes = noteRepository.getAllNotes();
    }

    // Fetch all notes (Reactive)
    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    // Fetch notes by folder ID (Reactive)
    public LiveData<List<Note>> getNotesByFolderId(int folderId) {
        return noteRepository.getNotesByFolderId(folderId);
    }

    // Insert a new note
    public void insert(Note note) {
        noteRepository.insert(note);
    }

    // Fetch all folders (Reactive)
    public LiveData<List<Folder>> getAllFolders() {
        return folderRepository.getAllFolders();
    }

    // Fetch folders with latest 3-note previews (Synchronous)
    public List<FolderViewHolder.Folder> getFoldersWithPreviews() {
        return folderRepository.getFoldersWithPreviews();
    }

    // Insert a new folder
    public long insertFolder(Folder folder) {
        return folderRepository.insert(folder);
    }
}
