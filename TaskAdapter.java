package nsrdev.task;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<Task> listTask;
    private Context context;

    public TaskAdapter(List<Task> listTask) {
        this.listTask = listTask;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview, viewGroup, false);

        this.context = viewGroup.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Task task = listTask.get(i);
        Calendar calendar = task.getDate();

        viewHolder.taskName.setText(task.getName());
        viewHolder.taskDate.setText(DateFormat.getDateInstance().format(calendar.getTime()));
        viewHolder.cardview.setCardBackgroundColor(task.getColor());
        viewHolder.taskCheckBox.setChecked(task.isCompleted());

        if(task.isCompleted()) {
            viewHolder.taskName.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            viewHolder.taskName.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
        }

        viewHolder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("id", task.getId());
                context.startActivity(intent);
            }
        });

        viewHolder.taskCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!task.isCompleted()) {
                    task.setCompleted(true);
                    viewHolder.taskName.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    task.save();
                } else {
                    task.setCompleted(false);
                    viewHolder.taskName.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
                    task.save();
                }
            }
        });

    }

    private List<Task> getTasksFromDB() {
        return SQLite
                .select()
                .from(Task.class)
                .orderBy(Task_Table.date, true)
                .queryList();

    }

    @Override
    public int getItemCount() {
        return this.listTask.size();
    }

    public void setList(List<Task> tasks) {
        listTask = tasks;
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.taskName)
        TextView taskName;
        @BindView(R.id.taskDate)
        TextView taskDate;
        @BindView(R.id.ll_cardview)
        LinearLayout ll;
        @BindView(R.id.cardview)
        CardView cardview;
        @BindView(R.id.task_check_box)
        AppCompatCheckBox taskCheckBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}


