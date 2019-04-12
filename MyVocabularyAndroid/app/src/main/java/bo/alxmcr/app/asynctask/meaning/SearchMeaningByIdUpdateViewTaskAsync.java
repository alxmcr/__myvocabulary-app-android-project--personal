package bo.alxmcr.app.asynctask.meaning;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import alxmcr.bo.vocabularyapp_v8.R;
import bo.alxmcr.app.VocabularyApplication;
import bo.alxmcr.app.httpclients.meaning.MeaningHttpClientImpl;
import bo.alxmcr.app.httpclients.word.WordHttpClientImpl;
import bo.alxmcr.app.models.Meaning;
import bo.alxmcr.app.models.Word;
import bo.alxmcr.utils.general.GeneralUtils;

public class SearchMeaningByIdUpdateViewTaskAsync extends AsyncTask<Object, Void, Meaning> {

    public static final String TAG = SearchMeaningByIdUpdateViewTaskAsync.class.getSimpleName();
    //Elements Activity
    private EditText editTextIdMeaning;
    private EditText editTextTextMeaning;
    private EditText editTextStatusMeaning;

    private ProgressDialog progressDialog;
    private Activity context;
    private Resources resources;
    private String error;
    private MeaningHttpClientImpl meaningHttpClient;
	private VocabularyApplication app;

    public SearchMeaningByIdUpdateViewTaskAsync(VocabularyApplication app, Activity context, Resources resources, EditText editTextIdMeaning, EditText editTextTextMeaning, EditText editTextStatusMeaning) {
		
		//Estableciendo el dominio de la app
        this.app = app;
        String domainApp = this.app.getModoDeConexionServidor();
		this.meaningHttpClient = new MeaningHttpClientImpl(domainApp);
		
		//estableciendo algunos atributos
        this.context = context;
        this.resources = resources;
        this.editTextIdMeaning = editTextIdMeaning;
        this.editTextTextMeaning = editTextTextMeaning;
        this.editTextStatusMeaning = editTextStatusMeaning;

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
                String titleProgressBar = this.resources.getString(R.string.title_activity_search_word);
                String msgProgressBar = this.resources.getString(R.string.msg_obtaining_information_word);
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
    protected Meaning doInBackground(Object... params) {
        int numberElements = 0;
        Meaning meaningById = null;

        String idMeaning = null;

        if (params != null) {
            numberElements = params.length;
        } else {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            String msg = TAG + resources.getString(R.string.msg_error_parameters_of_input_is_null);
            Log.e(typeMessage, msg);
        }

        if (numberElements > 0) {
            //From Activity
            idMeaning = String.valueOf(params[0]);
        } else {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            String msg = TAG + resources.getString(R.string.msg_error_array_is_empty);
            Log.e(typeMessage, msg);
        }

        boolean isConnectionToInternet = GeneralUtils.isConnectingToInternet(this.context);

        if (isConnectionToInternet) {
            meaningById = this.meaningHttpClient.searchMeaningById(resources, idMeaning);
        } else {
            this.error = resources.getString(R.string.msg_error_not_connection_to_internet);
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            Log.e(typeMessage, this.error);
        }
        return meaningById;
    }

    @Override
    protected void onPostExecute(Meaning meaning) {
        if (this.error != null) {
            Toast.makeText(this.context, this.error, Toast.LENGTH_SHORT).show();
        } else {
            String idMeaning = null;
            String text = null;
            String status = null;
            if (meaning != null) {
                idMeaning = meaning.getId();
                text = meaning.getText();
                status = meaning.getStatus();
            }

            this.updateIdMeaning(idMeaning);
            this.updateTextMeaning(text);
            this.updateStatusMeaning(status);
        }
    }

    private void updateIdMeaning(String idMeaning) {
        if (idMeaning != null) {
            this.editTextIdMeaning.setText(idMeaning);
        } else {
            final String msg = resources.getString(R.string.msg_error_string_is_null);
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateTextMeaning(String text) {
        if (text != null) {
            this.editTextTextMeaning.setText(text);
        } else {
            final String msg = resources.getString(R.string.msg_error_string_is_null);
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateStatusMeaning(String status) {
        if (status != null) {
            this.editTextStatusMeaning.setText(status);
        } else {
            final String msg = resources.getString(R.string.msg_error_string_is_null);
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show();
        }
    }

}
