package com.example.punchpad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class NoteLibraryAdapter extends RecyclerView.Adapter<NoteLibraryAdapter.ViewHolder> {

    private final List<Folder> folderList;
    private final Map<Integer, List<String>> folderNotesMap; // Map to store notes for each folder

    public NoteLibraryAdapter(List<Folder> folderList, Map<Integer, List<String>> folderNotesMap) {
        this.folderList = folderList;
        this.folderNotesMap = folderNotesMap;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_folder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Folder folder = folderList.get(position);
        holder.folderName.setText(folder.getFolderName());

        // Toggle expanded state on click
        holder.folderContainer.setOnClickListener(view -> {
            boolean isExpanded = folder.isExpanded();
            folder.setExpanded(!isExpanded);
            notifyItemChanged(position);
        });

        // Show or hide the note container based on expanded state
        holder.noteContainer.setVisibility(folder.isExpanded() ? View.VISIBLE : View.GONE);

        // Fetch notes dynamically from the map
        List<String> notes = folderNotesMap.get(folder.getId());

        // Bind notes to TextViews
        holder.firstNote.setText((notes != null && notes.size() > 0) ? notes.get(0) : "");
        holder.secondNote.setText((notes != null && notes.size() > 1) ? notes.get(1) : "");
        holder.thirdNote.setText((notes != null && notes.size() > 2) ? notes.get(2) + "..." : "");
    }

    @Override
    public int getItemCount() {
        return folderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView folderName, firstNote, secondNote, thirdNote;
        public View noteContainer, folderContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            folderName = itemView.findViewById(R.id.folderName);
            firstNote = itemView.findViewById(R.id.firstNote);
            secondNote = itemView.findViewById(R.id.secondNote);
            thirdNote = itemView.findViewById(R.id.thirdNote);
            folderContainer = itemView.findViewById(R.id.folderContainer);
            noteContainer = itemView.findViewById(R.id.noteContainer);

        }
    }
}
