package com.example.punchpad;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FolderRepository {

    private final FolderDao folderDao;
    private final NoteDao noteDao;
    private final ExecutorService executorService;

    public FolderRepository(Application application) {
        NoteDatabase database = NoteDatabase.getInstance(application);
        folderDao = database.folderDao();
        noteDao = database.noteDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    // Get all folders (Reactive)
    public LiveData<List<Folder>> getAllFolders() {
        return folderDao.getAllFolders();
    }

    // Fetch latest notes for a folder
    public List<String> getLatestNotesByFolder(int folderId, int limit) {
        try {
            return executorService.submit(() -> noteDao.getLatestNotesByFolder(folderId, limit)).get();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Insert a new folder
    public long insert(Folder folder) {
        try {
            return executorService.submit(() -> folderDao.insert(folder)).get();
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Return -1 if an error occurs
        }
    }

    // Update an existing folder
    public void update(Folder folder) {
        executorService.execute(() -> folderDao.update(folder));
    }

    // Delete a folder
    public void delete(Folder folder) {
        executorService.execute(() -> folderDao.delete(folder));
    }

    // Get favorited folder (Reactive)
    public LiveData<Folder> getFavoritedFolder() {
        return folderDao.getFavoritedFolder();
    }

    // Set a folder as favorite
    public void setFavorite(int folderId) {
        executorService.execute(() -> {
            folderDao.clearFavorite(); // Remove existing favorites
            folderDao.setFavorite(folderId); // Set new favorite
        });
    }
}
