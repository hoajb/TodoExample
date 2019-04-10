package hoa.nguyenminh.todo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;

public class AddNoteEditActivity extends AppCompatActivity {
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "EXTRA_PRIORITY";
    public static final String EXTRA_ID = "EXTRA_ID";

    private EditText edTitle;
    private EditText edDescription;
    private NumberPicker numberPickerPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        edTitle = findViewById(R.id.title);
        edDescription = findViewById(R.id.description);
        numberPickerPriority = findViewById(R.id.number_picker_priority);

        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(10);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);


        Intent intent = getIntent();

        int id = intent.getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            String title = intent.getStringExtra(EXTRA_TITLE);
            String description = intent.getStringExtra(EXTRA_DESCRIPTION);
            int priority = intent.getIntExtra(EXTRA_PRIORITY, 1);

            edTitle.setText(title);
            edDescription.setText(description);
            numberPickerPriority.setValue(priority);
            setTitle("Update Note");
        } else {
            setTitle("Add Note");
        }
    }

    private void saveNote() {
        String title = edTitle.getText().toString();
        String description = edDescription.getText().toString();
        int priority = numberPickerPriority.getValue();

        if (TextUtils.isEmpty(title)) {
            ((TextInputLayout) edTitle.getParent()).setError("Input Title!");
            return;
        }

        if (TextUtils.isEmpty(title)) {
            ((TextInputLayout) edDescription.getParent()).setError("Input Description!");
            return;
        }

        Intent intentResult = new Intent();
        intentResult.putExtra(EXTRA_TITLE, title);
        intentResult.putExtra(EXTRA_DESCRIPTION, description);
        intentResult.putExtra(EXTRA_PRIORITY, priority);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            intentResult.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, intentResult);

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


}
