package nsrdev.task;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        configStatusBar();
    }

    private void configStatusBar() {
        Window window = this.getWindow();

        window.setStatusBarColor(Color.WHITE);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    @OnClick(R.id.nsrdev_button)
    public void onViewClicked() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://nsrdev.github.io/"));
        startActivity(browserIntent);
    }
}
