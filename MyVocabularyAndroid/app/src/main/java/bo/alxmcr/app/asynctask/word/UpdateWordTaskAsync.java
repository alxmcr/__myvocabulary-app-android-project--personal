package bo.alxmcr.app.asynctask.word;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.sql.Time;

import alxmcr.bo.vocabularyapp_v8.R;
import bo.alxmcr.app.VocabularyApplication;
import bo.alxmcr.app.activities.word.ListWordsActivity;
import bo.alxmcr.app.extras.exceptions.connectivity.AppServerNotAvailableException;
import bo.alxmcr.app.httpclients.word.WordHttpClientImpl;
import bo.alxmcr.app.models.Word;
import bo.alxmcr.utils.general.GeneralUtils;

/**
 * Created by JoseCoca-i7 on 17/05/2015.
 */
public class UpdateWordTaskAsync extends AsyncTask<Object, Void, Boolean> {

    public static final String TAG = UpdateWordTaskAsync.class.getSimpleName();

    private Activity context;
    private Resources resources;
    private ProgressDialog progressDialog;
    private String error;
    private WordHttpClientImpl wordHttpClient;
	private VocabularyApplication app;

    public UpdateWordTaskAsync(VocabularyApplication app, Activity context, Resources resources) {
		
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
                String titleProgressBar = this.resources.getString(R.string.title_activity_edit_word);
                String msgProgressBar = this.resources.getString(R.string.msg_updating_data_word_to_server);
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
        boolean isUpdated = false;
        Word word = null;
        int i = 0;

        String idWord = null;
        String newTextWord = null;
        String newStatusWord = null;

        if (params != null) {
            numberElements = params.length;
        } else {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            String msg = TAG + resources.getString(R.string.msg_error_parameters_of_input_is_null);
            Log.e(typeMessage, msg);
        }

        if (numberElements > 0) {
            //From Activity
            idWord = String.valueOf(params[i]);
            i++;
            numberElements--;
        } else {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            String msg = TAG + resources.getString(R.string.msg_error_array_is_empty);
            Log.e(typeMessage, msg);
        }

        if (numberElements > 0) {
            //From Activity
            newTextWord = String.valueOf(params[i]);
            i++;
            numberElements--;
        } else {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            String msg = TAG + resources.getString(R.string.msg_error_array_is_empty);
            Log.e(typeMessage, msg);
        }

        if (numberElements > 0) {
            //From Activity
            newStatusWord = String.valueOf(params[i]);
            i++;
            numberElements--;
        } else {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            String msg = TAG + resources.getString(R.string.msg_error_array_is_empty);
            Log.e(typeMessage, msg);
        }


        boolean isConnectionToInternet = GeneralUtils.isConnectingToInternet(this.context);

        if (isConnectionToInternet) {
            word = this.wordHttpClient.searchWordById(resources, idWord);
        } else {
            this.error = resources.getString(R.string.msg_error_not_connection_to_internet);
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            Log.e(typeMessage, this.error);
        }

        if (word != null) {

            java.util.Date modificationDateUtil = new java.util.Date();
            java.sql.Date modificationDate = new java.sql.Date(modificationDateUtil.getTime());

            Time modificationTime = GeneralUtils.getCurrentTimeSQL();

            word.setText(newTextWord);
            word.setStatus(newStatusWord);
            word.setModificationDate(modificationDate);
            word.setModificationTime(modificationTime);

            try {
                isUpdated = this.wordHttpClient.updateWord(resources, word);
            } catch (AppServerNotAvailableException e) {
                final String typeMessage = resources.getString(R.string.type_message_produced_exception);
                String msgError = typeMessage + e.getMessage();
                Log.e(TAG, msgError, e);
            }
        }

        return isUpdated;
    }

    @Override
    protected void onPostExecute(Boolean isUpdated) {
        progressDialog.dismiss();

        if (this.error != null) {
            Toast.makeText(this.context, this.error, Toast.LENGTH_SHORT).show();
        } else if (isUpdated) {
            String msg = "Word was updated, success";
            Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show();

            Intent myIntent = new Intent(this.context,
                    ListWordsActivity.class);

            this.context.startActivity(myIntent);
        }
    }
}
