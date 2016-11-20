package edu.osu.tapstory;

import android.app.ProgressDialog;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import edu.osu.tapstory.app.AppConfig;
import edu.osu.tapstory.app.AppController;
import edu.osu.tapstory.helper.DBHandler;
import edu.osu.tapstory.helper.SessionManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class NewTap extends AppCompatActivity {
    private long backPressedTime = 0;
    private ProgressDialog pDialog;
    private static final String TAG = RegisterActivity.class.getSimpleName();

    private ArrayList<String> messagesReceivedArray = new ArrayList<>();

    //Text boxes to add and display our messages
    private TextView txtReceivedMessages;
    private NfcAdapter mNfcAdapter;
    private DBHandler db;

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    private void checkLogin(final String id) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOC, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {

                        // Now store the user in SQLite

                        JSONObject user = jObj.getJSONObject("location");
                        String id = user.getString("id");
                        String name = user.getString("name");
                        String description = user.getString("description");

                        // Inserting row in users table
                        messagesReceivedArray.add(description);
                        updateTextViews();
                        //db.addUser(name, email, uid, created_at);

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);

                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private  void updateTextViews() {

        txtReceivedMessages.setText("Messages Received:\n");
        //Populate our list of messages we have received
        if (messagesReceivedArray.size() > 0) {
            for (int i = 0; i < messagesReceivedArray.size(); i++) {
                txtReceivedMessages.append(messagesReceivedArray.get(i));
                txtReceivedMessages.append("\n");
            }
        }
    }

    //Save our Array Lists of Messages for if the user navigates away
    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putStringArrayList("lastMessagesReceived",messagesReceivedArray);
    }

    //Load our Array Lists of Messages for when the user navigates back
    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        messagesReceivedArray = savedInstanceState.getStringArrayList("lastMessagesReceived");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tap);
        db = new DBHandler(getApplicationContext());
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        //R.layout.activity_new_tap
        txtReceivedMessages = (TextView) findViewById(R.id.txtMessagesReceived);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(mNfcAdapter != null) {
            //Handle some NFC initialization here
        }
        else {
            Toast.makeText(this, "NFC not available on this device",
                    Toast.LENGTH_SHORT).show();
        }
        updateTextViews();
    }


    @Override
    public void onBackPressed() {
        Intent back = new Intent(this, MainActivity.class);
        startActivity(back);
        finish();
    }

    public void toMain(View v) {
        Intent check = new Intent(this, MainActivity.class);
        startActivity(check);
        finish();
    }
    private void handleNfcIntent(Intent NfcIntent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(NfcIntent.getAction())) {
            Parcelable[] receivedArray =
                    NfcIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if(receivedArray != null) {
                messagesReceivedArray.clear();
                NdefMessage receivedMessage = (NdefMessage) receivedArray[0];
                NdefRecord[] attachedRecords = receivedMessage.getRecords();

                for (NdefRecord record:attachedRecords) {
                    String string = new String(record.getPayload());
                    //Make sure we don't pass along our AAR (Android Application Record)
                    if (string.equals(getPackageName())) { continue; }
                    checkLogin(string.substring((3)));
                    //messagesReceivedArray.add(string);
                }
                Toast.makeText(this, "Received " + messagesReceivedArray.size() +
                        " Messages", Toast.LENGTH_LONG).show();
                updateTextViews();
            }
            else {
                Toast.makeText(this, "Received Blank Parcel", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        updateTextViews();
        handleNfcIntent(getIntent());
    }

    @Override
    public void onNewIntent(Intent intent) {
        handleNfcIntent(intent);
    }
}
