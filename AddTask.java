package nsrdev.task;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.thebluealliance.spectrum.SpectrumPalette;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddTask extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    @BindView(R.id.taskTitle)
    TextInputEditText taskTitle;
    @BindView(R.id.taskDate)
    EditText taskDate;
    @BindView(R.id.taskNote)
    TextInputEditText taskNote;
    @BindView(R.id.taskTime)
    EditText taskTime;
    @BindView(R.id.color_palette)
    SpectrumPalette colorPalette;
    @BindView(R.id.cl_add_task)
    ConstraintLayout clAddTask;
    @BindView(R.id.list_view_categories)
    Spinner spinnerCategories;
    @BindView(R.id.taskLocation)
    TextInputEditText taskLocation;

    private Calendar calendarDate, calendarTime;
    private String dateString, timeString;
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        ButterKnife.bind(this);

        configTask();
        configStatusBar();
        configCategories();

        colorPalette.setOnColorSelectedListener(new SpectrumPalette.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                task.setColor(color);
                clAddTask.setBackgroundColor(color);
                configStatusBar();
            }
        });
    }

    private void configCategories() {
        ArrayAdapter<CharSequence> aa = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_list_item_1);
        spinnerCategories.setAdapter(aa);
    }

    private void configStatusBar() {
        Window window = this.getWindow();

        window.setStatusBarColor(task.getColor());
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }


    private void configTask() {
        task = new Task();
        task.setDate(Calendar.getInstance());
        task.setColor(Color.WHITE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void saveTask() {
        String nameAux, placeAux, noteAux;

        nameAux = taskTitle.getText().toString().trim();
        noteAux = taskNote.getText().toString().trim();
        placeAux = taskLocation.getText().toString().trim();

        if (nameAux.length() == 0) {
            taskTitle.setError("Nombre incompleto");
        } else if (calendarDate == null) {
            taskDate.setError("Fecha incompleta");
        } else if (calendarTime == null) {
            taskTime.setError("Hora incompleta");
        } else {
            task.setName(nameAux);
            task.setDate(calendarDate);
            task.setTime(calendarTime);
            task.setCategory(spinnerCategories.getSelectedItemPosition());
            task.setNote(noteAux);
            task.setCompleted(false);
            task.setLocation(placeAux);

            try {
                Long id = task.insert();
                task.setId(id);
                task.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
            finish();
        }
    }

    private void cancelTask() {
        finish();
    }

    @OnClick(R.id.taskDate)
    public void onSelectDate() {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "date picker");
    }

    @OnClick(R.id.taskTime)
    public void onSelectTime() {
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "hour picker");
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendarDate = Calendar.getInstance();
        calendarDate.set(Calendar.YEAR, year);
        calendarDate.set(Calendar.MONTH, month);
        calendarDate.set(calendarDate.DAY_OF_MONTH, dayOfMonth);

        dateString = DateFormat.getDateInstance().format(calendarDate.getTime());
        taskDate.setText(dateString);

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        timePicker.setIs24HourView(true);
        calendarTime = Calendar.getInstance();
        calendarTime.set(Calendar.HOUR_OF_DAY, hour);
        calendarTime.set(Calendar.MINUTE, minute);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        timeString = sdf.format(calendarTime.getTime());
        taskTime.setText(timeString);
    }

    @OnClick({R.id.cancelNewTask, R.id.saveNewTask})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancelNewTask:
                cancelTask();
                break;
            case R.id.saveNewTask:
                saveTask();
                break;
        }
    }
}
