package nsrdev.task;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class GenerateNotification {

    private final static String CHANNEL_ID = "NOTIFICATION";
    private final static int NOTIFICATION_ID = 0;

    @SuppressLint("ResourceAsColor")
    public GenerateNotification(Context c) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence charSequence = "Notification";
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                    charSequence, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(c, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_time);
        builder.setContentTitle("Task");
        builder.setContentText("Tienes una tarea mañana");
        builder.setColor(R.color.card_blue);
        builder.setVibrate(new long[] {1000, 1000, 1000, 1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(c);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
    }
}
