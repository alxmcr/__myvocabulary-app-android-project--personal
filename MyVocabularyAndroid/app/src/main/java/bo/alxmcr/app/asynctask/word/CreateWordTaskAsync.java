package bo.alxmcr.app.asynctask.word;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;

import alxmcr.bo.vocabularyapp_v8.R;
import bo.alxmcr.app.VocabularyApplication;
import bo.alxmcr.app.activities.word.ListWordsActivity;
import bo.alxmcr.app.extras.exceptions.connectivity.AppServerNotAvailableException;
import bo.alxmcr.app.httpclients.word.WordHttpClientImpl;
import bo.alxmcr.app.models.Meaning;
import bo.alxmcr.app.models.Word;
import bo.alxmcr.utils.general.GeneralUtils;

public class CreateWordTaskAsync extends AsyncTask<Object, Void, Boolean> {

    public static final String TAG = CreateWordTaskAsync.class.getSimpleName();

    private Activity context;
    private Resources resources;
    private ProgressDialog progressDialog;
    private String error;
    private WordHttpClientImpl wordHttpClient;
	private VocabularyApplication app;

    public CreateWordTaskAsync(VocabularyApplication app, Activity context, Resources resources) {
		
		//Estableciendo el dominio de la app
        this.app = app;
        String domainApp = this.app.getModoDeConexionServidor();
		this.wordHttpClient = new WordHttpClientImpl(domainApp);
		
		//estableciendo algunos atributos
        this.context = context;
        this.resources = resources;

        if (this.resources == null) {
            Log.e(TAG, "Error, resources is null");
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        try {

            if (this.context != null) {
                this.progressDialog = new ProgressDialog(this.context);
            } else {
                final String typeMessage = resources.getString(R.string.type_message_produced_error);
                String msgError = resources.getString(R.string.msg_error_context_is_null);
                Log.e(typeMessage, msgError);
            }

            if (this.progressDialog != null) {
                String titleProgressBar = this.resources.getString(R.string.title_activity_create_word);
                String msgProgressBar = this.resources.getString(R.string.msg_saving_word);
                this.progressDialog.setTitle(titleProgressBar);
                this.progressDialog.setMessage(msgProgressBar);
                this.progressDialog.show();
            } else {
                final String typeMessage = resources.getString(R.string.type_message_produced_error);
                String msgError = resources.getString(R.string.msg_error_resources_is_null);
                Log.e(typeMessage, msgError);
            }
        } catch (Exception e) {
            final String typeMessage = resources.getString(R.string.type_message_produced_exception);
            String msgError = e.getMessage();
            Log.e(typeMessage, msgError);
        }
    }

    @Override
    protected Boolean doInBackground(Object... params) {

        int numberElements = 0;
        boolean isCreated = false;
        Word word = null;

        try {

            String textWord = null;

            if (params != null) {
                numberElements = params.length;
            } else {
                final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
                String msg = TAG + resources.getString(R.string.msg_error_parameters_of_input_is_null);
                Log.e(typeMessage, msg);
            }

            if (numberElements > 0) {
                //From Activity
                textWord = String.valueOf(params[0]);
            } else {
                final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
                String msg = TAG + resources.getString(R.string.msg_error_array_is_empty);
                Log.e(typeMessage, msg);
            }

            if (textWord != null && !textWord.isEmpty()) {
                Log.v(TAG, "TextWord:" + textWord);
                word = this.createInstanceWord(textWord);
            } else {
                Log.e(TAG, "Error. TextWord is null or empty");
            }

            boolean isConnectionToInternet = GeneralUtils.isConnectingToInternet(this.context);

            if (isConnectionToInternet) {
                isCreated = this.wordHttpClient.createWord(resources, word);
            } else {
                this.error = resources.getString(R.string.msg_error_not_connection_to_internet);
                final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
                Log.e(typeMessage, this.error);
            }

        } catch (AppServerNotAvailableException e) {
            final String typeMessage = resources.getString(R.string.type_message_produced_exception);
            String msgError = e.getMessage();
            this.error = msgError;
            Log.e(typeMessage, msgError);
        }

        return isCreated;

    }

    @Override
    protected void onPostExecute(Boolean isCreated) {
        progressDialog.dismiss();

        if (this.error != null) {
            Log.e(TAG, "Error:" + this.error);
            Toast.makeText(this.context, this.error, Toast.LENGTH_SHORT).show();
        } else if (isCreated) {
            String msg = "Word was created, success";
            Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show();

            Intent myIntent = new Intent(this.context,
                    ListWordsActivity.class);

            this.context.startActivity(myIntent);
        }
    }

    private Word createInstanceWord(String textWord) {

        Word word = null;

        if (textWord != null) {
            Log.v(TAG, "textWord:" + textWord);
            java.util.Date creationDateUtil = new java.util.Date();
            java.sql.Date creationDate = new java.sql.Date(creationDateUtil.getTime());

            Time creationTime = GeneralUtils.getCurrentTimeSQL();

            word = new Word();
            word.setText(textWord);
            word.setStatus("ACTIVE");
            word.setCreationDate(creationDate);
            word.setCreationTime(creationTime);
            word.setModificationDate(creationDate);
            word.setModificationTime(creationTime);
            word.setMeanings(new ArrayList<Meaning>());
        } else {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            String msg = TAG + resources.getString(R.string.msg_error_string_is_null);
            Log.e(typeMessage, msg);
        }

        return word;
    }

}
