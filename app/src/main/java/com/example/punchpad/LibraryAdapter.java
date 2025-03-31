package com.example.punchpad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.LibraryViewHolder> {

    private List<Folder> folders;
    private Map<Integer, List<String>> folderNotesMap;
    private boolean isSelectionMode = false;

    private final FolderLongClickListener longClickListener;

    public LibraryAdapter(FolderLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public interface FolderLongClickListener {
        void onFolderLongClick();
    }

    @NonNull
    @Override
    public LibraryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item_folder, parent, false);
        return new LibraryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LibraryViewHolder holder, int position) {
        Folder folder = folders.get(position);
        List<String> notes = folderNotesMap != null ? folderNotesMap.get(folder.getId()) : null;
        holder.bind(folder, notes, isSelectionMode);
    }

    @Override
    public int getItemCount() {
        return folders != null ? folders.size() : 0;
    }

    public void setFolders(List<Folder> folders, Map<Integer, List<String>> folderNotesMap) {
        this.folders = folders;
        this.folderNotesMap = folderNotesMap;
        notifyDataSetChanged();
    }

    public void setFilteredFolders(List<Folder> filteredFolders) {
        this.folders = filteredFolders;
        notifyDataSetChanged();
    }

    public void setSelectionMode(boolean isSelectionMode) {
        this.isSelectionMode = isSelectionMode;
        notifyDataSetChanged();
    }

    public List<Folder> getSelectedFolders() {
        List<Folder> selectedFolders = new ArrayList<>();
        if (folders != null) {
            for (Folder folder : folders) {
                if (folder.isSelected()) { // Ensure Folder class has `isSelected` logic
                    selectedFolders.add(folder);
                }
            }
        }
        return selectedFolders;
    }

    class LibraryViewHolder extends RecyclerView.ViewHolder {
        private final TextView folderName;
        private final CheckBox selectionCheckBox;
        private final TextView firstNote, secondNote, thirdNote;

        public LibraryViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views
            folderName = itemView.findViewById(R.id.folderName);
            selectionCheckBox = itemView.findViewById(R.id.selectionCheckBox);
            firstNote = itemView.findViewById(R.id.firstNote);
            secondNote = itemView.findViewById(R.id.secondNote);
            thirdNote = itemView.findViewById(R.id.thirdNote);

            // Handle selection logic
            selectionCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    folders.get(position).setSelected(isChecked);
                }
            });

            // Handle long click to enable selection mode
            itemView.setOnLongClickListener(v -> {
                longClickListener.onFolderLongClick();
                return true;
            });
        }

        public void bind(Folder folder, List<String> notes, boolean isSelectionMode) {
            folderName.setText(folder.getFolderName());
            selectionCheckBox.setVisibility(isSelectionMode ? View.VISIBLE : View.INVISIBLE); // Maintain overlay visibility
            selectionCheckBox.setChecked(folder.isSelected());

            if (notes != null && !notes.isEmpty()) {
                firstNote.setVisibility(View.VISIBLE);
                firstNote.setText(notes.size() > 0 ? notes.get(0) : "");

                secondNote.setVisibility(notes.size() > 1 ? View.VISIBLE : View.GONE);
                secondNote.setText(notes.size() > 1 ? notes.get(1) : "");

                thirdNote.setVisibility(notes.size() > 2 ? View.VISIBLE : View.GONE);
                thirdNote.setText(notes.size() > 2 ? notes.get(2) : "");
            } else {
                firstNote.setVisibility(View.GONE);
                secondNote.setVisibility(View.GONE);
                thirdNote.setVisibility(View.GONE);
            }
        }
    }
}
