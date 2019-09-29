package nsrdev.task;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import java.util.Calendar;
import java.util.Objects;

@Table(database = TaskDatabase.class)
class Task extends BaseModel {

    @PrimaryKey(autoincrement = true)
    private long id;
    @Column
    private String name;
    @Column
    private Calendar date;
    @Column
    private Calendar time;
    @Column
    private int category;
    @Column
    private String note;
    @Column
    private int color;
    @Column
    private boolean completed;
    @Column
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Task() {
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getNote() {
        return note;
    }

    public String getName() {
        return name;
    }

    public Calendar getDate() {
        return date;
    }

    public Calendar getTime() {
        return time;
    }

    public long getId() {
        return id;
    }

    public int getCategory() {
        return category;
    }

    public int getColor() { return color; }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setColor(int color) { this.color = color; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
