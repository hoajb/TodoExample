package hoa.nguyenminh.todo;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

/**
 * Created by Hoa Nguyen on Apr 09 2019.
 */
public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application) {
        NoteDatabase database = NoteDatabase.getInstance(application);
        noteDao = database.noteDao();

        allNotes = noteDao.getAllNotes();
    }

    public void insert(Note note) {
        new NoteAsyncTask(noteDao, NoteAsyncTask.TYPE_INSERT).execute(note);
    }

    public void update(Note note) {
        new NoteAsyncTask(noteDao, NoteAsyncTask.TYPE_UPDATE).execute(note);
    }

    public void delete(Note note) {
        new NoteAsyncTask(noteDao, NoteAsyncTask.TYPE_DELETE).execute(note);
    }

    public void deleteAllNotes() {
        new NoteAsyncTask(noteDao, NoteAsyncTask.TYPE_DELETE_ALL).execute();
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    private static class NoteAsyncTask extends AsyncTask<Note, Void, Void> {
        public static final int TYPE_INSERT = 0;
        public static final int TYPE_UPDATE = 1;
        public static final int TYPE_DELETE = 2;
        public static final int TYPE_DELETE_ALL = 3;
        private NoteDao mNoteDao;
        private int mType;

        private NoteAsyncTask(NoteDao noteDao, int type) {
            mNoteDao = noteDao;
            mType = type;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            switch (mType) {
                case TYPE_INSERT:
                    mNoteDao.insert(notes[0]);
                    break;

                case TYPE_UPDATE:
                    mNoteDao.update(notes[0]);
                    break;

                case TYPE_DELETE:
                    mNoteDao.delete(notes[0]);
                    break;

                case TYPE_DELETE_ALL:
                    mNoteDao.deleteAllNotes();
                    break;
            }

            return null;
        }
    }
}
