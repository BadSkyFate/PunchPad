package com.example.punchpad;
import com.example.punchpad.NotesAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteHolder> {

    private List<Note> notes;

    public NotesAdapter(List<Note> notes) {
        this.notes = notes;
    }
    public NotesAdapter() {
        this.notes = new java.util.ArrayList<>();
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note currentNote = notes.get(position);

        holder.textViewContent.setText(currentNote.getContent());

        String formattedTimestamp = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.getDefault())
                .format(new Date(currentNote.getTimestamp()));
        holder.textViewTimestamp.setText(formattedTimestamp);
    }

    @Override
    public int getItemCount() {
        return notes != null ? notes.size() : 0;
    }

    public void updateNotes(List<Note> newNotes) {
        this.notes = newNotes;
        notifyDataSetChanged();
    }

    static class NoteHolder extends RecyclerView.ViewHolder {
        TextView textViewContent;
        TextView textViewTimestamp;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            textViewContent = itemView.findViewById(R.id.textViewContent);
            textViewTimestamp = itemView.findViewById(R.id.textViewTimestamp);
        }
    }
    private List<Note> allNotes;

    public void setAllNotes(List<Note> allNotes) {
        this.allNotes = new ArrayList<>(allNotes);
        this.notes = new ArrayList<>(allNotes);
        notifyDataSetChanged();
    }

    public void filter(String query) {
        if (query == null || query.trim().isEmpty()) {
            notes = new ArrayList<>(allNotes);
        } else {
            List<Note> filteredList = new ArrayList<>();
            for (Note note : allNotes) {
                if (note.getContent().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(note);
                }
            }
            notes = filteredList;
        }
        notifyDataSetChanged();
    }
}
