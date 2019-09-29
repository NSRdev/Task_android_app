package nsrdev.task;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = TaskDatabase.NAME, version = TaskDatabase.VERSION)
public class TaskDatabase {
    public static final int VERSION = 2;
    public static final String NAME = "TaskDatabase";
}
