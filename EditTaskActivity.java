package nsrdev.task;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.material.textfield.TextInputEditText;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.thebluealliance.spectrum.SpectrumPalette;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    @BindView(R.id.nameEdit)
    TextInputEditText nameEdit;
    @BindView(R.id.list_view_edit_categories)
    Spinner spinnerCategories;
    @BindView(R.id.dateEdit)
    EditText dateEdit;
    @BindView(R.id.placeEdit)
    TextInputEditText placeEdit;
    @BindView(R.id.noteEdit)
    TextInputEditText noteEdit;
    @BindView(R.id.timeEdit)
    EditText timeEdit;
    @BindView(R.id.edit_color_palette)
    SpectrumPalette colorPalette;
    @BindView(R.id.ll_edit_task)
    LinearLayout llEditTask;

    private Task task;
    private long id;
    private Calendar calendarDate, calendarTime;
    private SimpleDateFormat sdf;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private static final int PLACE_PICKER_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        ButterKnife.bind(this);

        id = getIntent().getLongExtra("id", 0);

        task = SQLite
                .select()
                .from(Task.class)
                .where(Task_Table.id.is(id))
                .querySingle();

        calendarDate = task.getDate();
        calendarTime = task.getTime();

        sdf = new SimpleDateFormat("kk:mm");

        nameEdit.setText(task.getName());
        System.out.println(task.getCategory());
        dateEdit.setText(DateFormat.getDateInstance().format(calendarDate.getTime()));
        timeEdit.setText(sdf.format(calendarTime.getTime()));
        placeEdit.setText(task.getLocation());
        noteEdit.setText(task.getNote());
        colorPalette.setSelectedColor(task.getColor());
        placeEdit.setText(task.getLocation());


        llEditTask.setBackgroundColor(task.getColor());

        colorPalette.setOnColorSelectedListener(new SpectrumPalette.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                task.setColor(color);
                llEditTask.setBackgroundColor(color);
                configStatusBar();
            }
        });

        configStatusBar();
        configCategories();
    }

    private void configStatusBar() {
        Window window = this.getWindow();

        window.setStatusBarColor(task.getColor());
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }
    private void configCategories() {
        ArrayAdapter<CharSequence> aa = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_list_item_1);
        spinnerCategories.setAdapter(aa);

        spinnerCategories.setSelection(task.getCategory());
    }


    @OnClick(R.id.dateEdit)
    public void onEditDateClicked() {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "date picker");
    }

    @OnClick(R.id.timeEdit)
    public void onEditTimeClicked() {
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "hour picker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendarDate = Calendar.getInstance();
        calendarDate.set(Calendar.YEAR, year);
        calendarDate.set(Calendar.MONTH, month);
        calendarDate.set(calendarDate.DAY_OF_MONTH, dayOfMonth);

        String dateString = DateFormat.getDateInstance().format(calendarDate.getTime());
        dateEdit.setText(dateString);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        timePicker.setIs24HourView(true);

        calendarTime = Calendar.getInstance();
        calendarTime.set(Calendar.HOUR_OF_DAY, hour);
        calendarTime.set(Calendar.MINUTE, minute);

        timeEdit.setText(sdf.format(calendarTime.getTime()));
    }


    @OnClick({R.id.cancelEditedTask, R.id.saveEditedTask})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancelEditedTask:
                finish();
                break;
            case R.id.saveEditedTask:
                task.setName(nameEdit.getText().toString().trim());
                task.setCategory(spinnerCategories.getSelectedItemPosition());
                task.setNote(noteEdit.getText().toString().trim());
                task.setDate(calendarDate);
                task.setTime(calendarTime);
                task.setLocation(placeEdit.getText().toString().trim());

                task.save();
                setResult(RESULT_OK);
                finish();
                break;
        }
    }
}