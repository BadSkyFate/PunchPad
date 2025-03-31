package com.example.punchpad;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class FolderViewModel extends AndroidViewModel {

    private final FolderRepository folderRepository;

    public FolderViewModel(@NonNull Application application) {
        super(application);
        folderRepository = new FolderRepository(application);
    }

    // Fetch all folders (Reactive)
    public LiveData<List<Folder>> getAllFolders() {
        return folderRepository.getAllFolders();
    }

    // Fetch latest notes for a folder
    public List<String> getLatestNotesByFolder(int folderId, int limit) {
        return folderRepository.getLatestNotesByFolder(folderId, limit);
    }

    // Insert a new folder
    public long insertFolder(Folder folder) {
        return folderRepository.insert(folder);
    }

    // Delete a folder
    public void deleteFolder(Folder folder) {
        folderRepository.delete(folder);
    }
}
