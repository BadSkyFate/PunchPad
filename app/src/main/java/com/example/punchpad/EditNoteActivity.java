package com.example.punchpad;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditNoteActivity extends AppCompatActivity {

    private static final String TAG = "EditNoteActivity";

    private EditText editTextNoteContent;
    private Button saveButton, cancelButton;

    private int noteId;
    private String originalContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        editTextNoteContent = findViewById(R.id.editTextNoteContent);
        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);

        // Get note details from Intent
        noteId = getIntent().getIntExtra("noteId", -1);
        originalContent = getIntent().getStringExtra("noteContent");

        if (noteId == -1 || originalContent == null) {
            Toast.makeText(this, "Error loading note", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Display the note content
        editTextNoteContent.setText(originalContent);

        // Save button listener
        saveButton.setOnClickListener(v -> {
            String updatedContent = editTextNoteContent.getText().toString().trim();
            if (!updatedContent.isEmpty() && !updatedContent.equals(originalContent)) {
                updateNoteInDatabase(noteId, updatedContent);
            } else {
                Toast.makeText(this, "No changes made", Toast.LENGTH_SHORT).show();
            }
        });

        // Cancel button listener
        cancelButton.setOnClickListener(v -> finish());
    }

    private void updateNoteInDatabase(int noteId, String updatedContent) {
        NoteDatabase database = NoteDatabase.getInstance(this);
        NoteDao noteDao = database.noteDao();

        new Thread(() -> {
            Note note = noteDao.getNoteByIdSync(noteId); // Use the newly added synchronous method
            if (note != null) {
                note.setContent(updatedContent);
                noteDao.update(note);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Note updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                });
            } else {
                runOnUiThread(() -> Toast.makeText(this, "Error updating note", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
