package com.example.punchpad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FolderViewHolder extends RecyclerView.ViewHolder {

    private final TextView folderNameTextView;
    private final RecyclerView notesRecyclerView;
    private final TextView firstNote, secondNote, thirdNote;

    public FolderViewHolder(@NonNull View itemView) {
        super(itemView);
        folderNameTextView = itemView.findViewById(R.id.folderName);
        notesRecyclerView = itemView.findViewById(R.id.notes_recycler_view);
        firstNote = itemView.findViewById(R.id.firstNote);
        secondNote = itemView.findViewById(R.id.secondNote);
        thirdNote = itemView.findViewById(R.id.thirdNote);
    }

    public void bind(Folder folder, boolean isExpanded, NotesAdapter notesAdapter) {
        folderNameTextView.setText(folder.getName());

        List<String> notes = folder.getNotes();

        firstNote.setText(notes.size() > 0 ? notes.get(0) : "");
        secondNote.setText(notes.size() > 1 ? notes.get(1) : "");
        thirdNote.setText(notes.size() > 2 ? notes.get(2) : "");

        if (isExpanded) {
            notesRecyclerView.setVisibility(View.VISIBLE);

            List<Note> convertedNotes = new ArrayList<>();
            for (String content : notes) {
                Note note = new Note(content, System.currentTimeMillis(), -1);
                convertedNotes.add(note);
            }

            notesAdapter.setAllNotes(convertedNotes);
            notesRecyclerView.setAdapter(notesAdapter);
        } else {
            notesRecyclerView.setVisibility(View.GONE);
        }
    }

    public static FolderViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item_folder, parent, false);
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
