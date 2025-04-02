package com.example.punchpad;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.Ignore;

@Entity(tableName = "folders")
public class Folder {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "folder_name")
    private String folderName;

    @ColumnInfo(name = "favorited")
    private boolean favorited;

    @ColumnInfo(name = "created_at")
    private long createdAt;

    private boolean isExpanded; // Field to track expanded state (not persisted in DB)

    private boolean isSelected; // Field to track selection state for deletion

    // Default Constructor (required by Room)
    public Folder() {
        this.isExpanded = false; // Default to collapsed
        this.isSelected = false; // Default to not selected
    }

    // Constructor with Parameters
    @Ignore
    public Folder(String folderName, boolean favorited, long createdAt) {
        this.folderName = folderName;
        this.favorited = favorited;
        this.createdAt = createdAt;
        this.isExpanded = false; // Default to collapsed
        this.isSelected = false; // Default to not selected
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public void toggleExpanded() {
        this.isExpanded = !this.isExpanded;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    public void toggleSelected() {
        this.isSelected = !this.isSelected;
    }

    // Utility method to toggle favorited status
    public void toggleFavorite() {
        this.favorited = !this.favorited;
    }

    // Utility method to check if the folder is new
    public boolean isNewFolder() {
        return this.createdAt == 0;
    }
}
