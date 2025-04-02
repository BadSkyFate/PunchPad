package com.example.punchpad;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private NoteViewModel noteViewModel;
    private FolderAdapter folderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        EditText largeTextBox = findViewById(R.id.largeTextBox);
        Button addNewNoteButton = findViewById(R.id.addNewNoteButton);
        Button settingsButton = findViewById(R.id.settingsButton);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        // Setup RecyclerView for displaying folders
        folderAdapter = new FolderAdapter(this, new ArrayList<>());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(folderAdapter);
        recyclerView.setVerticalScrollBarEnabled(true); // Enable vertical scrollbar for RecyclerView

        // Initialize ViewModel and observe data
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        noteViewModel.getAllFolders().observe(this, folders -> {
            if (folders != null) {
                folderAdapter = new FolderAdapter(this, folders); // ✅ correct

                recyclerView.setAdapter(folderAdapter);
            }
        });

        // Add New Note Button
        addNewNoteButton.setOnClickListener(v -> {
            String content = largeTextBox.getText().toString().trim();
            Log.d(TAG, "Add New Note button clicked with content: " + content);

            if (!content.isEmpty()) {
                // Create a new note and open folder selection dialog
                Note newNote = new Note(content, System.currentTimeMillis(), 0); // Default folderId = 0
                showFolderSelectionDialog(newNote);
                largeTextBox.setText(""); // Clear the textbox after adding note
            } else {
                // Navigate to Library when textbox is empty
                Log.d(TAG, "Textbox is empty. Navigating to LibraryActivity.");
                openLibrary();
            }
        });

        // Settings Button
        settingsButton.setOnClickListener(v -> {
            Log.d(TAG, "Settings button clicked.");
        });
    }

    // Show dialog for folder selection or creation
    private void showFolderSelectionDialog(Note note) {
        SaveNoteDialogFragment dialogFragment = new SaveNoteDialogFragment(note);
        dialogFragment.show(getSupportFragmentManager(), "SaveNoteDialog");
    }

    // Navigate to LibraryActivity
    private void openLibrary() {
        Intent intent = new Intent(this, LibraryActivity.class);
        startActivity(intent);
        Log.d(TAG, "LibraryActivity started.");
    }
}
