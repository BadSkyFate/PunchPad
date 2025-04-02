package com.example.punchpad;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Note.class, Folder.class}, version = 4, exportSchema = false) // Incremented version to 4
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase instance;

    public abstract NoteDao noteDao();
    public abstract FolderDao folderDao();

    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            NoteDatabase.class, "note_database")
                    .addMigrations(MIGRATION_3_4) // Apply the latest migration
                    .build();
        }
        return instance;
    }

    // Migration from version 3 to version 4
    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Example: Add a new column `isExpanded` to the `folders` table
            database.execSQL("ALTER TABLE folders ADD COLUMN isExpanded INTEGER NOT NULL DEFAULT 0");
        }
    };
}
