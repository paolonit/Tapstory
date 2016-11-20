package edu.osu.tapstory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ViewPlaces extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_places);
    }



    @Override
    public void onBackPressed() {
        Intent back = new Intent(this, CheckTaps.class);
        startActivity(back);
        finish();
    }
}
