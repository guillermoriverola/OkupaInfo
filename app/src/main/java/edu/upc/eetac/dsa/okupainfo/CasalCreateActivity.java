package edu.upc.eetac.dsa.okupainfo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import javax.ws.rs.core.Form;

import edu.upc.eetac.dsa.okupainfo.client.OkupaInfoClient;
import edu.upc.eetac.dsa.okupainfo.client.OkupaInfoClientException;

/**
 * Created by Guillermo on 21/05/2016.
 */
public class CasalCreateActivity extends AppCompatActivity {
    EditText etSubject = null;
    EditText etContent = null;
    Button btCreate = null;
    private CreateCasalTask mCreateCasalTask = null;
    private final static String TAG = LoginActivity.class.toString();
    class CreateCasalTask extends AsyncTask<Void, Void, Boolean> {
        private Form form;

        public CreateCasalTask(Form form) {
            this.form = form;

        }

        @Override
        protected Boolean doInBackground(Void... params) {

            boolean result = false;
            try
            {
                result = OkupaInfoClient.getInstance().CreateCasal(form);

            } catch (OkupaInfoClientException e) {
                // TODO: Handle gracefully
                Log.d(TAG, e.getMessage());
            }

            return result;
        }


        @Override
        protected void onPostExecute(Boolean result) {
            if (result == true) {
                Intent i = getIntent();
                setResult(RESULT_OK, i);
                finish();
            }

            if (result == false) {
                Intent i = getIntent();
                setResult(RESULT_CANCELED, i);
                finish();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_casal_create);
        etSubject = (EditText)findViewById(R.id.Subject);
        etContent = (EditText)findViewById(R.id.Content);
        btCreate = (Button)findViewById(R.id.Create);

        btCreate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(etSubject.getText().length()!=0 && etContent.getText().length()!=0)
                {
                    String subject = etSubject.getText().toString();
                    String content = etContent.getText().toString();
                    Form form = new Form();
                    form.param("subject", subject);
                    form.param("content", content);

                    // Execute AsyncTask
                    mCreateCasalTask = new CreateCasalTask(form);
                    mCreateCasalTask.execute((Void) null);
                }


                else
                {
                    Log.d(TAG, "Debes escribir en ambos campos para crear el Casal");
                }
            }

        });
    }

}
