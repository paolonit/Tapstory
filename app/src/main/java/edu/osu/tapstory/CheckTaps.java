package edu.osu.tapstory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import edu.osu.tapstory.app.AppConfig;
import edu.osu.tapstory.app.AppController;
import edu.osu.tapstory.helper.SQLiteHandler;
import edu.osu.tapstory.helper.SessionManager;

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
