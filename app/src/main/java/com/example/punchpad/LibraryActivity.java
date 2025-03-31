package com.example.punchpad;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibraryActivity extends AppCompatActivity {

    private static final String TAG = "LibraryActivity";

    private RecyclerView recyclerView;
    private LibraryAdapter libraryAdapter;
    private FolderViewModel folderViewModel;
    private TextView noFoldersMessage;
    private Button cancelButton, deleteButton, backButton;
    private boolean isSelectionMode = false;
    private List<Folder> allFolders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");
        setContentView(R.layout.activity_library);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        SearchView searchView = findViewById(R.id.searchView);
        noFoldersMessage = findViewById(R.id.noNotesMessage);
        recyclerView = findViewById(R.id.recyclerView);
        cancelButton = findViewById(R.id.cancelButton);
        deleteButton = findViewById(R.id.deleteButton);
        backButton = findViewById(R.id.backButton);
        View selectionButtons = findViewById(R.id.selectionButtons);

        selectionButtons.setVisibility(View.GONE);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        libraryAdapter = new LibraryAdapter(this::onFolderLongClick);
        recyclerView.setAdapter(libraryAdapter);

        folderViewModel = new ViewModelProvider(this).get(FolderViewModel.class);
        loadFoldersWithNotes();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterFolders(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterFolders(newText);
                return false;
            }
        });

        cancelButton.setOnClickListener(v -> exitSelectionMode(selectionButtons));
        deleteButton.setOnClickListener(v -> deleteSelectedFolders(selectionButtons));

        backButton.setOnClickListener(v -> {
            Log.d(TAG, "Bottom back button clicked. Navigating back.");
            finish();
        });
    }

    private void loadFoldersWithNotes() {
        folderViewModel.getAllFolders().observe(this, folders -> {
            if (folders == null || folders.isEmpty()) {
                showNoFoldersMessage();
            } else {
                allFolders = folders;
                Map<Integer, List<String>> folderNotesMap = new HashMap<>();
                for (Folder folder : folders) {
                    List<String> latestNotes = folderViewModel.getLatestNotesByFolder(folder.getId(), 3);
                    folderNotesMap.put(folder.getId(), latestNotes != null ? latestNotes : new ArrayList<>());
                }
                showFolders(folders, folderNotesMap);
            }
        });
    }

    private void showNoFoldersMessage() {
        noFoldersMessage.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void showFolders(List<Folder> folders, Map<Integer, List<String>> folderNotesMap) {
        noFoldersMessage.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        libraryAdapter.setFolders(folders, folderNotesMap);
    }

    private void filterFolders(String query) {
        if (allFolders == null || allFolders.isEmpty()) {
            return;
        }

        List<Folder> filteredFolders = new ArrayList<>();
        for (Folder folder : allFolders) {
            if (folder.getFolderName().toLowerCase().contains(query.toLowerCase())) {
                filteredFolders.add(folder);
            }
        }
        libraryAdapter.setFilteredFolders(filteredFolders);
    }

    private void onFolderLongClick() {
        isSelectionMode = true;
        libraryAdapter.setSelectionMode(true);
        findViewById(R.id.selectionButtons).setVisibility(View.VISIBLE);
        Log.d(TAG, "Entered selection mode.");
    }

    private void exitSelectionMode(View selectionButtons) {
        isSelectionMode = false;
        libraryAdapter.setSelectionMode(false);
        selectionButtons.setVisibility(View.GONE);
        Log.d(TAG, "Exited selection mode.");
    }

    private void deleteSelectedFolders(View selectionButtons) {
        List<Folder> selectedFolders = libraryAdapter.getSelectedFolders();
        if (selectedFolders == null || selectedFolders.isEmpty()) {
            Toast.makeText(this, "No folders selected to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            for (Folder folder : selectedFolders) {
                folderViewModel.deleteFolder(folder);
            }
            Toast.makeText(this, "Selected folders deleted", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Error deleting folders: ", e);
            Toast.makeText(this, "Error deleting folders. Please try again.", Toast.LENGTH_SHORT).show();
        } finally {
            exitSelectionMode(selectionButtons);
            loadFoldersWithNotes();
        }
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed triggered. isSelectionMode=" + isSelectionMode);
        if (isSelectionMode) {
            exitSelectionMode(findViewById(R.id.selectionButtons));
        } else {
            Log.d(TAG, "Navigating back to previous screen.");
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Log.d(TAG, "Toolbar back button pressed.");
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
