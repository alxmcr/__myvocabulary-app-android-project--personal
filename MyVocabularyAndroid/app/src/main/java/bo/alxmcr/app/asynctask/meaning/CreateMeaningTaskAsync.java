package bo.alxmcr.app.asynctask.meaning;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;

import alxmcr.bo.vocabularyapp_v8.R;
import bo.alxmcr.app.VocabularyApplication;
import bo.alxmcr.app.activities.word.ListWordsActivity;
import bo.alxmcr.app.activities.word.WordActivity;
import bo.alxmcr.app.extras.exceptions.connectivity.AppServerNotAvailableException;
import bo.alxmcr.app.httpclients.meaning.MeaningHttpClientImpl;
import bo.alxmcr.app.httpclients.word.WordHttpClientImpl;
import bo.alxmcr.app.models.Meaning;
import bo.alxmcr.app.models.Word;
import bo.alxmcr.utils.general.GeneralUtils;

/**
 * Created by JoseCoca-i7 on 17/05/2015.
 */
public class CreateMeaningTaskAsync extends AsyncTask<Object, Void, Boolean> {

    public static final String TAG = CreateMeaningTaskAsync.class.getSimpleName();

    private Activity context;
    private Resources resources;
    private ProgressDialog progressDialog;
    private String error;
    private WordHttpClientImpl wordHttpClient;
    private MeaningHttpClientImpl meaningHttpClient;
    private String idWord;
	private VocabularyApplication app;

    public CreateMeaningTaskAsync(VocabularyApplication app, Activity context, Resources resources) {
		
		//Estableciendo el dominio de la app
        this.app = app;
        String domainApp = this.app.getModoDeConexionServidor();
		this.meaningHttpClient = new MeaningHttpClientImpl(domainApp);
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
                String titleProgressBar = this.resources.getString(R.string.title_activity_create_meaning);
                String msgProgressBar = this.resources.getString(R.string.msg_saving_meaning);
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
        Meaning meaning = null;
        int i = 0;

        try {

            String textMeaning = null;

            if (params != null) {
                Log.v(TAG, "params[]" + Arrays.toString(params));
                numberElements = params.length;
            } else {
                final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
                String msg = TAG + resources.getString(R.string.msg_error_parameters_of_input_is_null);
                Log.e(typeMessage, msg);
            }

            if (numberElements > 0) {
                Log.v(TAG, "numberElements->" + numberElements);
                //From Activity
                this.idWord = String.valueOf(params[i]);
                i++;
                numberElements--;
            } else {
                final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
                String msg = TAG + resources.getString(R.string.msg_error_array_is_empty);
                Log.e(typeMessage, msg);
            }

            if (numberElements > 0) {
                //From Activity
                textMeaning = String.valueOf(params[i]);
                i++;
                numberElements--;
            } else {
                final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
                String msg = TAG + resources.getString(R.string.msg_error_array_is_empty);
                Log.e(typeMessage, msg);
            }

            boolean isConnectionToInternet = GeneralUtils.isConnectingToInternet(this.context);

            if (this.idWord != null) {
                Log.v(TAG, "idWord:" + this.idWord);
            }

            if (textMeaning != null) {
                Log.v(TAG, "textMeaning:" + textMeaning);
            }

            if (isConnectionToInternet) {
                word = this.wordHttpClient.searchWordById(resources, this.idWord);
            } else {
                this.error = resources.getString(R.string.msg_error_not_connection_to_internet);
                final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
                Log.e(typeMessage, this.error);
            }

            if (word != null) {
                meaning = this.createInstanceMeaning(textMeaning, word);
                isCreated = this.meaningHttpClient.createMeaning(resources, meaning);
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
            Toast.makeText(this.context, this.error, Toast.LENGTH_SHORT).show();
        } else if (isCreated) {
            String msg = "Meaning was created, success";
            Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show();

            Intent myIntent = new Intent(this.context,
                    WordActivity.class);

            myIntent.putExtra("idWord", this.idWord);

            this.context.startActivity(myIntent);
        }
    }

    private Meaning createInstanceMeaning(String textMeaning, Word word) {

        Meaning meaning = null;

        if (textMeaning != null) {
            Log.v(TAG, "textMeaning:" + textMeaning);
            java.util.Date creationDateUtil = new java.util.Date();
            java.sql.Date creationDate = new java.sql.Date(creationDateUtil.getTime());

            Time creationTime = GeneralUtils.getCurrentTimeSQL();

            meaning = new Meaning();
            meaning.setText(textMeaning);
            meaning.setStatus("ACTIVE");
            meaning.setCreationDate(creationDate);
            meaning.setCreationTime(creationTime);
            meaning.setModificationDate(creationDate);
            meaning.setModificationTime(creationTime);
            meaning.setWord(word);
        } else {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            String msg = TAG + resources.getString(R.string.msg_error_string_is_null);
            Log.e(typeMessage, msg);
        }

        return meaning;
    }

}
