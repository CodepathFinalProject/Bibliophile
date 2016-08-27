package com.codepath.bibliophile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.codepath.bibliophile.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codepath.bibliophile.utils.SharedPreferenceUtility.getAddressLine1;
import static com.codepath.bibliophile.utils.SharedPreferenceUtility.getAddressLine2;
import static com.codepath.bibliophile.utils.SharedPreferenceUtility.getCity;
import static com.codepath.bibliophile.utils.SharedPreferenceUtility.getState;
import static com.codepath.bibliophile.utils.SharedPreferenceUtility.getZipcode;

public class AddressActivity extends AppCompatActivity {

    String addre1 = "", addre2 = "", city = "", state = "", zipcode = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
    }

    public void onAddressSave(View view) {

        EditText addr1 = (EditText)findViewById(R.id.etAddress1);
        if(addr1.getText().toString().isEmpty()){
            addr1.setError("Address cannot be empty");
            return;
        }


        EditText addr2 = (EditText)findViewById(R.id.etAddress2);

        EditText etCity = (EditText)findViewById(R.id.etCity);
        if(etCity.getText().toString().isEmpty()){
            etCity.setError("Please enter the city");
            return;
        }


        EditText etState = (EditText)findViewById(R.id.state);
        if(etState.getText().toString().isEmpty()){
            etState.setError("Please enter the state");
            return;
        }


        EditText etZipcode = (EditText)findViewById(R.id.etZipCode);
        String regex = "^[0-9]{5}(?:-[0-9]{4})?$" ;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(etZipcode.getText().toString());
        if(etZipcode.getText().toString().isEmpty() || !(matcher.matches())){
            etZipcode.setError("Please enter a valid numeric zipcode either (xxxxxx) or (xxxxx)-(xxxx)");
            return;
        }

        addr1.setText(addr1.getText());
        addre1 = addr1.getText().toString();
        addr2.setText(addr2.getText());
        addre2 = addr2.getText().toString();
        etCity.setText(etCity.getText());
        city = etCity.getText().toString();
        etState.setText(etState.getText());
        state = etState.getText().toString();
        etZipcode.setText(etZipcode.getText());
        zipcode = etZipcode.getText().toString();

        saveToParse();

      //  setAddress(getApplicationContext(),addre1,addre2,city,state,zipcode);
       // Toast.makeText(AddressActivity.this, "shared preference result : " + getAddressLine1(getApplicationContext()) + " :: " + getAddressLine2(getApplicationContext()) + " :: "  + getCity(getApplicationContext())+ " :: " + getState(getApplicationContext()) + " :: " + getZipcode(getApplicationContext()), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AddressActivity.this,HomeMainActivity.class);
        startActivity(intent);
    }



    private void saveToParse() {
        final Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.type(large)");
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me", parameters, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(final GraphResponse response) {
                if(response != null){
                    try {
                        JSONObject data = response.getJSONObject();
                        String profilePicUrl = "";
                        if (data.has("picture")) {
                            profilePicUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");
                        }
                        Log.d("3764", "onCompleted: " + response.getJSONObject().toString());
                        String email = response.getJSONObject().getString("email");
                        String name = response.getJSONObject().getString("name");
                        String address = getAddressLine1(getApplicationContext()) + " " + getAddressLine2(getApplicationContext()) + ", " + getCity(getApplicationContext()) + ", " + getState(getApplicationContext()) + " - " + getZipcode(getApplicationContext());

                        ParseUser parseUser = ParseUser.getCurrentUser();
                        parseUser.setEmail(email);
                        parseUser.setUsername(name);
                        parseUser.put("address",address);
                        parseUser.put("profilePic",profilePicUrl);
                        parseUser.saveEventually();
                    } catch (final JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).executeAsync();
    }

}
