package com.example.punchpad;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FolderDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;
    private NoteViewModel noteViewModel;
    private int folderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_details);

        // Get folder ID from intent
        folderId = getIntent().getIntExtra("folderId", -1);

        // Initialize UI elements
        SearchView searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);
        Button backButton = findViewById(R.id.backButton);

        // Back button functionality
        backButton.setOnClickListener(v -> finish()); // Close this activity and return to the previous one

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        notesAdapter = new NotesAdapter();
        recyclerView.setAdapter(notesAdapter);

        // Initialize ViewModel
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        // Load notes in the folder
        loadNotes();

        // Search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterNotes(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterNotes(newText);
                return false;
            }
        });
    }

    private void loadNotes() {
        noteViewModel.getNotesByFolderId(folderId).observe(this, notes -> {
            if (notes != null) {
                notesAdapter.setNotes(notes);
            }
        });
    }

    private void filterNotes(String query) {
        notesAdapter.filter(query);
    }
}
