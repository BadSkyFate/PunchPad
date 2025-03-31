package com.example.punchpad;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

public class FolderSelectionDialogFragment extends DialogFragment {

    public interface FolderSelectionListener {
        void onFolderSelected(int folderId);
    }

    private FolderSelectionListener listener;
    private NoteViewModel noteViewModel;
    private Note note;

    public FolderSelectionDialogFragment(Note note) {
        this.note = note;
    }

    public void setFolderSelectionListener(FolderSelectionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Select Folder");

        noteViewModel.getAllFolders().observe(this, folders -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_list_item_1,
                    convertFolderListToStringArray(folders));

            builder.setAdapter(adapter, (dialog, which) -> {
                Folder selectedFolder = folders.get(which);
                if (listener != null) {
                    listener.onFolderSelected(selectedFolder.getId());
                }
                note.setFolderId(selectedFolder.getId());
                noteViewModel.insert(note);
            });

            builder.setNegativeButton("Create New Folder", (dialog, which) -> {
                showCreateNewFolderDialog();
            });
        });

        return builder.create();
    }

    private void showCreateNewFolderDialog() {
        EditText input = new EditText(getContext());
        input.setHint("Folder Name");

        new AlertDialog.Builder(getContext())
                .setTitle("New Folder")
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

    private String[] convertFolderListToStringArray(List<Folder> folders) {
        String[] folderNames = new String[folders.size()];
        for (int i = 0; i < folders.size(); i++) {
            folderNames[i] = folders.get(i).getFolderName();
        }
        return folderNames;
    }
}
