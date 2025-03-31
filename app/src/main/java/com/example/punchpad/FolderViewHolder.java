package com.example.punchpad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FolderViewHolder extends RecyclerView.ViewHolder {

    private final TextView folderNameTextView;
    private final RecyclerView notesRecyclerView;

    public FolderViewHolder(@NonNull View itemView) {
        super(itemView);
        folderNameTextView = itemView.findViewById(R.id.folder_name);
        notesRecyclerView = itemView.findViewById(R.id.notes_recycler_view);
    }

    public void bind(Folder folder, boolean isExpanded, NotesAdapter notesAdapter) {
        folderNameTextView.setText(folder.getName());

        if (isExpanded) {
            notesRecyclerView.setVisibility(View.VISIBLE);
            notesAdapter.updateNotes(folder.getNotes());
            notesRecyclerView.setAdapter(notesAdapter);
        } else {
            notesRecyclerView.setVisibility(View.GONE);
        }
    }

    public static FolderViewHolder create(ViewGroup parent) {
        // Corrected layout reference
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_folder, parent, false);
        return new FolderViewHolder(view);
    }

    public static class Folder {
        private final String name;
        private final List<String> notes;

        public Folder(String name, List<String> notes) {
            this.name = name;
            this.notes = notes;
        }

        public String getName() {
            return name;
        }

        public List<String> getNotes() {
            return notes;
        }
    }
}
