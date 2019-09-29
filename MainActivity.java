package nsrdev.task;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.searchField)
    TextInputEditText searchField;
    @BindView(R.id.recycler_view_design_button)
    AppCompatImageButton recyclerViewDesignButton;

    private TaskAdapter adapter;
    private List<Task> listTask = new ArrayList<>();
    private SharedPreferences app_configuration;
    private SharedPreferences.Editor app_configuration_editor;
    private Set<String> categoriesSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddTask.class);
                startActivity(intent);
            }
        });

        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String name = searchField.getText().toString().trim();
                if (name.length() == 0) {
                    adapter.setList(getTasksFromDB());
                } else {
                    adapter.setList(getTasksFromDBByName(name));
                }
            }
        });

        configAppConfiguration();
        configAdapter();
        configRecyclerView();
        configStatusBar();
    }

    private void configAppConfiguration() {
        app_configuration = getSharedPreferences("preferences", 0);
        app_configuration_editor = app_configuration.edit();
    }

    private void configStatusBar() {
        Window window = this.getWindow();

        window.setStatusBarColor(Color.WHITE);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }


    private void configRecyclerView() {
        set_recycler_view_layout();
        recyclerView.setAdapter(adapter);
    }


    private void configAdapter() {
        adapter = new TaskAdapter(listTask);
    }


    private List<Task> getTasksFromDBByName(String name) {
        return SQLite
                .select()
                .from(Task.class)
                .where(Task_Table.name.like("%" + name + "%"))
                .queryList();
    }


    private List<Task> getTasksFromDB() {

        return SQLite
                .select()
                .from(Task.class)
                .orderBy(Task_Table.date, true)
                .queryList();
    }


    @Override
    protected void onResume() {         // Cada vez que la vista pasa a primer plano se ejecuta
        super.onResume();
        adapter.setList(getTasksFromDB());
    }

    @OnClick(R.id.menu_button)
    public void onViewClicked() {
        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(intent);
    }



    public void set_recycler_view_layout() {
        Boolean status = app_configuration.getBoolean("grid_layout_status", true);
        if (status) {
            recyclerViewDesignButton.setImageResource(R.drawable.linear);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerViewDesignButton.setImageResource(R.drawable.grid);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    @OnClick(R.id.recycler_view_design_button)
    public void recycler_view_design_button_clicked() {
        Boolean status = app_configuration.getBoolean("grid_layout_status", true);
        if (status) {
            recyclerViewDesignButton.setImageResource(R.drawable.grid);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            recyclerViewDesignButton.setImageResource(R.drawable.linear);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }
        app_configuration_editor.putBoolean("grid_layout_status", !status).commit();
    }
}
