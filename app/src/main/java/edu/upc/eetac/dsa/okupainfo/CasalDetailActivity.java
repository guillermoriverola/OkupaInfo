package edu.upc.eetac.dsa.okupainfo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import edu.upc.eetac.dsa.okupainfo.client.OkupaInfoClient;
import edu.upc.eetac.dsa.okupainfo.client.OkupaInfoClientException;
import edu.upc.eetac.dsa.okupainfo.client.entity.Casal;

/**
 * Created by Guillermo on 21/05/2016.
 */
public class CasalDetailActivity extends AppCompatActivity {
    GetCasalTask mGetCasalTask = null;
    String uri = null;
    String casalid = null;
    String name = null;
    String description = null;
    private final static String TAG = CasalDetailActivity.class.toString();
    TextView textViewCasalid = null;
    TextView textViewName = null;
    TextView textViewDescription = null;

    class GetCasalTask extends AsyncTask<Void, Void, String> {
        private String uri;

        public GetCasalTask(String uri) {
            this.uri = uri;

        }

        @Override
        protected String doInBackground(Void... params) {
            String jsonCasal = null;
            try {
                jsonCasal = OkupaInfoClient.getInstance().getCasal(uri);
            } catch (OkupaInfoClientException e) {
                // TODO: Handle gracefully
                Log.d(TAG, e.getMessage());
            }
            return jsonCasal;
        }

        @Override
        protected void onPostExecute(String jsonCasal) {
            Log.d(TAG, jsonCasal);
            Casal casal = (new Gson()).fromJson(jsonCasal, Casal.class);
            casalid = casal.getCasalid();
            name = casal.getName();
            description = casal.getDescription();

            textViewCasalid.setText(casalid);
            textViewName.setText(name);
            textViewDescription.setText(description);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_casal_detail);

        uri = (String) getIntent().getExtras().get("uri");

        textViewCasalid = (TextView) findViewById(R.id.textViewCasalid);
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewDescription = (TextView) findViewById(R.id.textViewDescription);

        // Execute AsyncTask
        mGetCasalTask = new GetCasalTask(uri);
        mGetCasalTask.execute((Void) null);


    }
}
