package bo.alxmcr.app.asynctask.meaning;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
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

public class SearchMeaningByIdTaskAsync extends AsyncTask<Object, Void, Meaning> {

    public static final String TAG = SearchMeaningByIdTaskAsync.class.getSimpleName();
    //Elements Activity
    private TextView textMeaningSelected;

    private Activity context;
    private ProgressDialog progressDialog;
    private Resources resources;
    private String error;
    private MeaningHttpClientImpl meaningHttpClient;
	private VocabularyApplication app;

    public SearchMeaningByIdTaskAsync(VocabularyApplication app, Activity context, Resources resources, TextView textMeaningSelected) {
		
		//Estableciendo el dominio de la app
        this.app = app;
        String domainApp = this.app.getModoDeConexionServidor();
		this.meaningHttpClient = new MeaningHttpClientImpl(domainApp);
		
		//estableciendo algunos atributos
        this.context = context;
        this.resources = resources;
        this.textMeaningSelected = textMeaningSelected;

        if (this.resources == null) {
            Log.e(TAG, "Error, resources is null");
        }
    }

    @Override
    protected void onPreExecute() {
        Log.v(TAG, "Start: onPreExecute()");
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
                String titleProgressBar = this.resources.getString(R.string.title_activity_meaning);
                String msgProgressBar = this.resources.getString(R.string.msg_obtaining_information_meaning);
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
        Log.v(TAG, "End: onPreExecute()");
    }

    @Override
    protected Meaning doInBackground(Object... params) {
        Log.v(TAG, "Start: doInBackground()");
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
        Log.v(TAG, "End: doInBackground()");
        return meaningById;
    }

    @Override
    protected void onPostExecute(Meaning meaning) {
        progressDialog.dismiss();

        if (this.error != null) {
            Toast.makeText(this.context, this.error, Toast.LENGTH_SHORT).show();
        } else {
            List<Meaning> meanings = null;
            String text = null;
            if (meaning != null) {
                text = meaning.getText();
            }

            this.updateTitleWord(text);
        }
    }

    private void updateTitleWord(String text) {
        if (text != null) {
            this.textMeaningSelected.setText(text);
        } else {
            final String msg = resources.getString(R.string.msg_error_string_is_null);
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show();
        }
    }

}
