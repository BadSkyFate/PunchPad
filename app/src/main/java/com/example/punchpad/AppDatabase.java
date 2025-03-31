package com.example.punchpad;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Note.class, Folder.class}, version = 4, exportSchema = false) // Incremented version to 4
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract NoteDao noteDao();

    public abstract FolderDao folderDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "punch_pad_database")
                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4) // Added migration to handle version 3 -> 4
                            .addCallback(roomCallback)
                            .fallbackToDestructiveMigration() // Ensure database reset if migration fails during debugging
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.d("AppDatabase", "Database created!");
        }

        @Override
        public void onOpen(SupportSQLiteDatabase db) {
            super.onOpen(db);
            Log.d("AppDatabase", "Database opened!");
        }
    };

    // Migration from version 1 to version 2
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            Log.d("AppDatabase", "Applying migration from version 1 to 2");

            // Add the folder_id column to the notes table
            database.execSQL("ALTER TABLE notes ADD COLUMN folder_id INTEGER NOT NULL DEFAULT 0");

            // Create the Folder table if it does not exist
            database.execSQL("CREATE TABLE IF NOT EXISTS Folder (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "folder_name TEXT, " +
                    "favorited INTEGER NOT NULL, " +
                    "created_at INTEGER NOT NULL)");
        }
    };

    // Migration from version 2 to version 3
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            Log.d("AppDatabase", "Applying migration from version 2 to 3");

            // Add a new column to the folders table
            database.execSQL("ALTER TABLE folders ADD COLUMN description TEXT DEFAULT NULL");
        }
    };

    // Migration from version 3 to version 4
    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            Log.d("AppDatabase", "Applying migration from version 3 to 4");

            // Example: Add a new column or modify schema further if needed
            // No schema changes in this example; replace or add actual schema modifications here
        }
    };
}
