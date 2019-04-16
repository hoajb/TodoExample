package hoa.nguyenminh.todo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import hoa.nguyenminh.todo.binding.DataBindingNoteAdapter;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_ADD_NOTE = 500;
    public static final int REQUEST_EDIT_NOTE = 501;
    private NoteViewModel mNoteViewModel;
    private DataBindingNoteAdapter mNoteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        mNoteAdapter = new DataBindingNoteAdapter();
        recyclerView.setAdapter(mNoteAdapter);
        mNoteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        mNoteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //update View
                mNoteAdapter.submitList(notes);

            }
        });

        mNoteAdapter.setOnItemClickListener(new DataBindingNoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(MainActivity.this, AddNoteEditActivity.class);
                intent.putExtra(AddNoteEditActivity.EXTRA_TITLE, note.getTitle());
                intent.putExtra(AddNoteEditActivity.EXTRA_DESCRIPTION, note.getDescription());
                intent.putExtra(AddNoteEditActivity.EXTRA_PRIORITY, note.getTitle());
                intent.putExtra(AddNoteEditActivity.EXTRA_ID, note.getId());

                startActivityForResult(intent, REQUEST_EDIT_NOTE);
            }
        });

        FloatingActionButton buttonAddNote = findViewById(R.id.bt_add_note);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddNoteEditActivity.class);
                startActivityForResult(intent, REQUEST_ADD_NOTE);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mNoteViewModel.delete(mNoteAdapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notes:
                mNoteViewModel.deleteAllNotes();
                Toast.makeText(MainActivity.this, "All Notes deleted", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ADD_NOTE && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddNoteEditActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddNoteEditActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddNoteEditActivity.EXTRA_PRIORITY, 1);

            Note note = new Note(title, description, priority);
            mNoteViewModel.insert(note);

            Toast.makeText(MainActivity.this, "Note saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == REQUEST_EDIT_NOTE && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddNoteEditActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddNoteEditActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddNoteEditActivity.EXTRA_PRIORITY, 1);
            int id = data.getIntExtra(AddNoteEditActivity.EXTRA_ID, -1);

            if (id != -1) {
                Note note = new Note(title, description, priority);
                note.setId(id);
                mNoteViewModel.update(note);
                Toast.makeText(MainActivity.this, "Note saved", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(MainActivity.this, "Note can't be updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Note not saved", Toast.LENGTH_SHORT).show();
        }
    }
}
