package edu.osu.tapstory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ViewPlaces extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_places);
        TextView places = (TextView) findViewById(R.id.listPlaces);
        places.setText("Ohio Union");
    }



    @Override
    public void onBackPressed() {
        Intent back = new Intent(this, CheckTaps.class);
        startActivity(back);
        finish();
    }
}
