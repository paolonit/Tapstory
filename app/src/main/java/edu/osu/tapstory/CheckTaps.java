package edu.osu.tapstory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
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
        TextView taps = (TextView) findViewById(R.id.totalTaps);
        taps.setText("3");
        TextView places = (TextView) findViewById(R.id.totalPlaces);
        places.setText("3");
    }



    @Override
    public void onBackPressed() {
        Intent back = new Intent(this, MainActivity.class);
        startActivity(back);
        finish();
    }



    public void toPlaces(View v) {
        Intent check = new Intent(this, ViewPlaces.class);
        startActivity(check);
        finish();
    }
}
