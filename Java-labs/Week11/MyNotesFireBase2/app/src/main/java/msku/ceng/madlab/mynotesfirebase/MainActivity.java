package msku.ceng.madlab.mynotesfirebase;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements NoteFragment.OnNoteListInteractionsListener{
    boolean displayingEditor = false;
    Note editingNote;
    ArrayList<Note> notes;
    ListenerRegistration listenerRegistration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (!displayingEditor) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.container, NoteFragment.newInstance(), "list_note");
            ft.commit();
        } else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, EditNoteFragment.newInstance(editingNote.getContent()));
            ft.addToBackStack(null);
            ft.commit();
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        listenerRegistration = db.collection("notes").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("Firebase", "error retrieveing notes");
                    return;
                }

                notes.clear();
                for (QueryDocumentSnapshot doc : value) {
                    notes.add(doc.toObject(Note.class));
                }

                NoteFragment listFragment = (NoteFragment) getSupportFragmentManager().findFragmentByTag("list_note");
                listFragment.updateNotes(notes);
            }

            ;
        });
    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected (@NonNull MenuItem item){
        displayingEditor = !displayingEditor;
        invalidateOptionsMenu();    // menuyu refresh etmesini sağlar
        int id = item.getItemId();
        if (id == R.id.action_new) {
            editingNote = createNote();
            notes.add(editingNote);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, EditNoteFragment.newInstance(""), "edit_note");
            ft.addToBackStack(null);
            ft.commit();
            return true;
        } else if (id == R.id.action_close) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu){
        Log.d("onPrepareOptionsMenu new visible",
                menu.findItem(R.id.action_new).isVisible() + "");
        menu.findItem(R.id.action_new).setVisible(!displayingEditor);
        menu.findItem(R.id.action_close).setVisible(displayingEditor);

        return super.onPrepareOptionsMenu(menu);
    }
    getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            invalidateOptionsMenu();
            EditNoteFragment editNoteFragment = (EditNoteFragment) getSupportFragmentManager().findFragmentByTag("edit_note");
            String content = null;
            if (editNoteFragment != null) {
                content = editNoteFragment.getContent();

            }
            if (content != null) {
                saveContent(editingNote, content);
            }
            setEnable(false);
            getOnBackPressedDispatcher().onBackPressed();

        }

        private void setEnable(boolean b) {
        }

        private void saveContent(Note editingNote, String content) {
            if (editingNote == null || !editingNote.getContent().equals(content) {
                editingNote.setContent(content);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                editingNote.setDate(new Timestamp(new Date()));
                db.collection("notes").document(editingNote.getId()).set(editingNote);

            };
        }
    });

    @Override
    public void onNoteSelected (Note note){
        editingNote = note;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.container, EditNoteFragment.newInstance(editingNote.getContent()), "edit note");
        ft.addToBackStack(null);
        ft.commit();
        displayingEditor = !displayingEditor;
        invalidateOptionsMenu();
    }

    private Note createNote () {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Note note = new Note();
        String id = FirebaseFirestore.getInstance().collection("notes").document().getId();
        note.setId(id);
        db.collection("notes").document(id).set(note);
        return note;
    }

    @Override
    protected void onStop() {
        super.onStop();
        listenerRegistration.remove();
    }
}






