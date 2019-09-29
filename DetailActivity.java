package nsrdev.task;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.taskNoteEdit)
    TextView taskNoteEdit;
    @BindView(R.id.taskNameEdit)
    TextView taskNameEdit;
    @BindView(R.id.taskDateEdit)
    TextView taskDateEdit;
    @BindView(R.id.taskTimeEdit)
    TextView taskTimeEdit;
    @BindView(R.id.layout_detail)
    LinearLayout layoutDetail;
    @BindView(R.id.ll_category)
    LinearLayout llCategory;
    @BindView(R.id.ll_location)
    LinearLayout llLocation;
    @BindView(R.id.ll_note)
    LinearLayout llNote;
    @BindView(R.id.taskCategoryEdit)
    TextView taskCategoryEdit;
    @BindView(R.id.completed_task_ic)
    AppCompatImageView completedTaskIc;
    @BindView(R.id.editTask)
    AppCompatImageButton editTask;
    @BindView(R.id.taskLocationEdit)
    TextView taskLocationEdit;

    private Task task;
    private long id;
    private SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        configDetails();
        configStatusBar();
    }

    private void configStatusBar() {
        Window window = this.getWindow();

        window.setStatusBarColor(task.getColor());
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    @OnClick({R.id.editTask, R.id.deleteTask, R.id.backDetail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.editTask:
                editTask();
                break;
            case R.id.deleteTask:
                deleteTask();
                break;
            case R.id.backDetail:
                finish();
                break;
        }
    }

    private void editTask() {
        Intent intent = new Intent(DetailActivity.this, EditTaskActivity.class);
        intent.putExtra("id", id);
        startActivityForResult(intent, 1);
    }

    private void deleteTask() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle(R.string.delete_confirm)
                .setMessage(R.string.delete_confirmation)
                .setPositiveButton(R.string.delete_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        task.delete();
                        finish();
                    }
                })
                .setNegativeButton(R.string.delete_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        configDetails();
        configStatusBar();
    }

    private void configDetails() {
        getTask();

        layoutDetail.setBackgroundColor(task.getColor());

        sdf = new SimpleDateFormat("HH:mm");

        taskNameEdit.setText(task.getName());
        taskDateEdit.setText(DateFormat.getDateInstance().format(task.getDate().getTime()));
        taskTimeEdit.setText(sdf.format(task.getTime().getTime()));
        taskCategoryEdit.setText(getCategoryString(task.getCategory()));
        taskLocationEdit.setText(task.getLocation());

        if (task.getLocation().length() == 0) {
            llLocation.setVisibility(LinearLayout.GONE);
        } else {
            llLocation.setVisibility(LinearLayout.VISIBLE);
        }

        if (task.getNote().length() == 0) {
            llNote.setVisibility(LinearLayout.GONE);
        } else {
            llNote.setVisibility(LinearLayout.VISIBLE);
            taskNoteEdit.setText(task.getNote());
        }

        if (task.getCategory() == 0) {
            llCategory.setVisibility(LinearLayout.GONE);
        } else {
            llCategory.setVisibility(LinearLayout.VISIBLE);
            taskCategoryEdit.setText(getCategoryString(task.getCategory()));
        }

        if (task.isCompleted()) {
            completedTaskIc.setVisibility(View.VISIBLE);
            editTask.setVisibility(View.GONE);
        } else {
            completedTaskIc.setVisibility(View.GONE);
            editTask.setVisibility(View.VISIBLE);
        }
    }

    private String getCategoryString(int categoryIndex) {
        switch (categoryIndex) {
            case 1:
                return "Estudios";
            case 2:
                return "Trabajo";
            case 3:
                return "Viajes";
            case 4:
                return "Ocio";
            case 5:
                return "Hogar";
            case 6:
                return "Alimentación";
            case 7:
                return "Familia";
            case 8:
                return "Burocracia";
            default:
                return "";
        }
    }

    private void getTask() {
        id = getIntent().getLongExtra("id", 0);
        task = SQLite
                .select()
                .from(Task.class)
                .where(Task_Table.id.is(id))
                .querySingle();
    }
}