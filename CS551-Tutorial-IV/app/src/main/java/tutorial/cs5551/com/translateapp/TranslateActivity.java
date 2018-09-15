package tutorial.cs5551.com.translateapp;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TranslateActivity extends AppCompatActivity {

    String API_URL = "https://api.fullcontact.com/v2/person.json?";
    String API_KEY = "b29103a702edd6a";

    String YANDEX_API_KEY = "trnsl.1.1.20151023T145251Z.bf1ca7097253ff7e.c0b0a88bea31ba51f72504cc0cc42cf891ed90d2";

    // UI elements we will be using
    String sourceText;
    TextView outputTextView;
    Spinner fromSpinner;
    Spinner toSpinner;

    Map<String, String> langToCode;

    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        // Create variables to store references to the UI elements, so we can modify them in the Java code.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        outputTextView = (TextView) findViewById(R.id.txt_Result);
        fromSpinner = (Spinner) findViewById(R.id.spin_from);
        toSpinner = (Spinner) findViewById(R.id.spin_to);

        setSupportActionBar(toolbar);

        langToCode = new HashMap<String, String>();

        populateSupportedLanguages();
    }

    public void populateSupportedLanguages() {
        // https://tech.yandex.com/translate/doc/dg/reference/getLangs-docpage/
        /* Result looks like this:      {"dirs":["az-ru","be-bg","be-cs","be-de","be-en","be-es","be-fr", ...,"uk-tr"], "langs": {"af":"Afrikaans"}, ...}  */

        String getURL = "https://translate.yandex.net/api/v1.5/tr.json/getLangs?key="
                + YANDEX_API_KEY +
                "&ui=en";//The API service URL

        OkHttpClient client = new OkHttpClient();
        try { // attempt to do the following. Catch the error (below) if there's an exception (error).

            // Send a request to the API URL
            Request request = new Request.Builder()
                    .url(getURL)
                    .build();

            // Create a callback for the request. When the request returns, run onResponse if it's a success.
            client.newCall(request).enqueue(new Callback() {

                // This handles a failed API request
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println(e.getMessage());
                }

                // This handles a successful API request
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    // Retrieve the results, which are in JSON format
                    final JSONObject jsonResult;
                    final JSONObject langMap;
                    final Iterator<String> keys;
                    final String result = response.body().string();

                    // Try the following...
                    try {

                        // Turn the results into a JSON object, and then get the array within it.
                        jsonResult = new JSONObject(result);
                        //JSONArray convertedTextArray = jsonResult.getJSONArray("langs");
                        langMap = jsonResult.optJSONObject("langs");
                        keys = langMap.keys();

                        // Create a list of strings to store all the language names.
                        List<String> languageList = new ArrayList<>();
                        while ( keys.hasNext() )
                        {
                            String key = keys.next();
                            String value = langMap.getString(key);
                            languageList.add(value);
                            langToCode.put(value, key);
                        }

                        // Create an adapter to link the list of languages to our spinner objects.
                        final ArrayAdapter<String> languageAdapter = new ArrayAdapter<String>(TranslateActivity.this, android.R.layout.simple_spinner_item, languageList);

                        // Execute this once the UI thread is ready to update.
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Update the spinners to contain the language lists.
                                fromSpinner.setAdapter(languageAdapter);
                                toSpinner.setAdapter(languageAdapter);

                                // Default "from" to English
                                int englishPosition = languageAdapter.getPosition("English");
                                fromSpinner.setSelection(englishPosition);

                                // Default "to" to Spanish
                                int spanishPosition = languageAdapter.getPosition("Spanish");
                                toSpinner.setSelection(spanishPosition);
                            }
                        }); // callback

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }); // callback
        } // external try
        catch (Exception ex) {
            outputTextView.setText(ex.getMessage());
        }

    }

    public void logout(View v) {
        Intent redirect = new Intent(TranslateActivity.this, LoginActivity.class);
        startActivity(redirect);
    }

    public void translateText(View v) {
        TextView sourceTextView = (TextView) findViewById(R.id.txt_Email);

        sourceText = sourceTextView.getText().toString();

        String fromLanguage = fromSpinner.getSelectedItem().toString();
        String fromCode = langToCode.get(fromLanguage);

        String toLanguage = toSpinner.getSelectedItem().toString();
        String toCode = langToCode.get(toLanguage);

        String getURL = "https://translate.yandex.net/api/v1.5/tr.json/translate?key="
                + YANDEX_API_KEY +
                "&text=" + sourceText +"&" +
                "lang=" + fromCode + "-" + toCode + "&[format=plain]&[options=1]&[callback=set]";//The API service URL

        final String response1 = "";
        OkHttpClient client = new OkHttpClient();
        try {
            Request request = new Request.Builder()
                    .url(getURL)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println(e.getMessage());
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final JSONObject jsonResult;
                    final String result = response.body().string();
                    try {
                        jsonResult = new JSONObject(result);
                        JSONArray convertedTextArray = jsonResult.getJSONArray("text");
                        final String convertedText = convertedTextArray.get(0).toString();
                        Log.d("okHttp", jsonResult.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                outputTextView.setText(convertedText);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


        } catch (Exception ex) {
            outputTextView.setText(ex.getMessage());

        }

    }
}