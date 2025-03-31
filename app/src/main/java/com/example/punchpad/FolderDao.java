package com.example.punchpad;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FolderDao {

    // Insert a single folder
    @Insert
    long insert(Folder folder); // Return folder ID for newly created folders

    // Update an existing folder
    @Update
    void update(Folder folder);

    // Delete a folder
    @Delete
    void delete(Folder folder);

    // Retrieve all folders, ordered by creation date
    @Query("SELECT * FROM folders ORDER BY created_at ASC")
    LiveData<List<Folder>> getAllFolders(); // Changed to LiveData for reactivity

    // Retrieve all folder names
    @Query("SELECT folder_name FROM folders ORDER BY folder_name ASC")
    LiveData<List<String>> getAllFolderNames(); // Changed to LiveData for dynamic updates

    // Retrieve a folder by its ID
    @Query("SELECT * FROM folders WHERE id = :folderId")
    LiveData<Folder> getFolderById(int folderId); // Changed to LiveData

    // Retrieve the favorited folder (if exists)
    @Query("SELECT * FROM folders WHERE favorited = 1 LIMIT 1")
    LiveData<Folder> getFavoritedFolder(); // Changed to LiveData

    // Remove all favorite flags
    @Query("UPDATE folders SET favorited = 0")
    void clearFavorite();

    // Set a specific folder as favorite
    @Query("UPDATE folders SET favorited = 1 WHERE id = :folderId")
    void setFavorite(int folderId);

    // Retrieve folder names synchronously (added for flexibility)
    @Query("SELECT folder_name FROM folders ORDER BY folder_name ASC")
    List<String> getAllFolderNamesSync();
}
