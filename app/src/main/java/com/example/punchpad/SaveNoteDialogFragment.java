package com.example.punchpad;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

public class SaveNoteDialogFragment extends DialogFragment {

    private Note note;
    private NoteViewModel noteViewModel;

    public SaveNoteDialogFragment(Note note) {
        this.note = note;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Save Note");

        String[] options = {"Select Existing Folder", "Create New Folder"};
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                showExistingFoldersDialog();
            } else if (which == 1) {
                showCreateNewFolderDialog();
            }
        });

        return builder.create();
    }

    private void showExistingFoldersDialog() {
        noteViewModel.getAllFolders().observe(this, folders -> {
            String[] folderNames = new String[folders.size()];
            for (int i = 0; i < folders.size(); i++) {
                folderNames[i] = folders.get(i).getFolderName();
            }

            new AlertDialog.Builder(requireContext())
                    .setTitle("Select Folder")
                    .setItems(folderNames, (dialog, which) -> {
                        Folder selectedFolder = folders.get(which);
                        note.setFolderId(selectedFolder.getId());
                        noteViewModel.insert(note);
                    })
                    .show();
        });
    }

    private void showCreateNewFolderDialog() {
        EditText input = new EditText(getContext());
        input.setHint("Folder Name");

        new AlertDialog.Builder(getContext())
                .setTitle("Create New Folder")
                .setView(input)
                .setPositiveButton("Save", (dialog, which) -> {
                    String folderName = input.getText().toString().trim();
                    if (!folderName.isEmpty()) {
                        Folder newFolder = new Folder(folderName, false, System.currentTimeMillis());
                        int folderId = (int) noteViewModel.insertFolder(newFolder);
                        note.setFolderId(folderId);
                        noteViewModel.insert(note);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
