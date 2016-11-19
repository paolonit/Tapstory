package edu.osu.tapstory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class CheckTaps extends AppCompatActivity {
    private long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_taps);
    }

    @Override
    public void onBackPressed() {
        long t = System.currentTimeMillis();
        if (t - backPressedTime > 2000) {
            backPressedTime = t;
            Toast.makeText(this, "Press back again to exit.",
                    Toast.LENGTH_SHORT).show();
        } else {

            super.onBackPressed();
        }
    }

    public void toMain(View v) {
        Intent check = new Intent(this, MainActivity.class);
        startActivity(check);
        finish();
    }
}
