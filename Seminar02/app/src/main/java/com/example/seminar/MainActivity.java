package com.example.seminar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.seminar.adapter.NoteAdapter;
import com.example.seminar.bottomSheetFragment.CreateNoteBottomSheetFragment;
import com.example.seminar.database.DatabaseClient;
import com.example.seminar.model.Note;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements CreateNoteBottomSheetFragment.setRefreshListener {

    @BindView(R.id.noteRecycler)
    RecyclerView noteRecycler;
    @BindView(R.id.addNote)
    TextView addNote;
    @BindView(R.id.noDataImage)
    ImageView noDataImage;
    NoteAdapter noteAdapter;
    List<Note> notes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noteRecycler = findViewById(R.id.noteRecycler);
        addNote = findViewById(R.id.addNote);
        ButterKnife.bind(this);
        setUpAdapter();

        addNote.setOnClickListener(view -> {
            CreateNoteBottomSheetFragment createNoteBottomSheetFragment = new CreateNoteBottomSheetFragment();
            createNoteBottomSheetFragment.setNoteId(0, false, this, MainActivity.this);
            createNoteBottomSheetFragment.show(getSupportFragmentManager(), createNoteBottomSheetFragment.getTag());
        });

        getSaveNotes();
    }

    public void setUpAdapter() {
        noteAdapter = new NoteAdapter(this, notes, this);
        noteRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        noteRecycler.setAdapter(noteAdapter);
    }

    private void getSaveNotes() {
        class GetSaveNotes extends AsyncTask<Void, Void, List<Note>> {
            @Override
            protected List<Note> doInBackground(Void... voids) {
                notes = DatabaseClient.getInstance(getApplicationContext()).getAppDataBase()
                        .noteDAO()
                        .getAllNotesList();
                return notes;
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
                noDataImage.setVisibility(notes.isEmpty() ? View.VISIBLE : View.GONE);
                setUpAdapter();
            }
        }
        GetSaveNotes saveNotes = new GetSaveNotes();
        saveNotes.execute();
    }

    @Override
    public void refresh() {
        getSaveNotes();
    }
}