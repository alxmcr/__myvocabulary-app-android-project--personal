package bo.alxmcr.app.asynctask.meaning;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import alxmcr.bo.vocabularyapp_v8.R;
import bo.alxmcr.app.VocabularyApplication;
import bo.alxmcr.app.activities.meaning.MeaningActivity;
import bo.alxmcr.app.activities.word.WordActivity;
import bo.alxmcr.app.extras.exceptions.connectivity.AppServerNotAvailableException;
import bo.alxmcr.app.httpclients.meaning.MeaningHttpClientImpl;
import bo.alxmcr.app.models.Meaning;
import bo.alxmcr.app.models.Word;
import bo.alxmcr.utils.general.GeneralUtils;

/**
 * Created by JoseCoca-i7 on 17/05/2015.
 */
public class DeleteMeaningByIdTaskAsync extends AsyncTask<Object, Void, Boolean> {

    public static final String TAG = DeleteMeaningByIdTaskAsync.class.getSimpleName();

    private Activity context;
    private Resources resources;
    private ProgressDialog progressDialog;
    private String error;
    private MeaningHttpClientImpl meaningHttpClient;
    private String idWord;
    private String idMeaning;
	private VocabularyApplication app;

    public DeleteMeaningByIdTaskAsync(VocabularyApplication app, Activity context, Resources resources) {
		
		//Estableciendo el dominio de la app
        this.app = app;
        String domainApp = this.app.getModoDeConexionServidor();
		this.meaningHttpClient = new MeaningHttpClientImpl(domainApp);
		
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
                String titleProgressBar = this.resources.getString(R.string.title_activity_delete_meaning);
                String msgProgressBar = this.resources.getString(R.string.msg_deleting_meaning);
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
        boolean result = false;
        int i = 0;

        if (params != null) {
            numberElements = params.length;
        } else {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            String msg = TAG + resources.getString(R.string.msg_error_parameters_of_input_is_null);
            Log.e(typeMessage, msg);
        }

        if (numberElements > 0) {
            //From Activity
            this.idWord  = String.valueOf(params[i]);
            i++;
            numberElements--;
        } else {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            String msg = TAG + resources.getString(R.string.msg_error_array_is_empty);
            Log.e(typeMessage, msg);
        }

        if (numberElements > 0) {
            //From Activity
            this.idMeaning = String.valueOf(params[i]);
            i++;
            numberElements--;
        } else {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            String msg = TAG + resources.getString(R.string.msg_error_array_is_empty);
            Log.e(typeMessage, msg);
        }

        try {
            boolean isConnectionToInternet = GeneralUtils.isConnectingToInternet(this.context);

            if (isConnectionToInternet) {

                result = this.meaningHttpClient.deleteMeaningById(resources, this.idMeaning);

            } else {
                this.error = resources.getString(R.string.msg_error_not_connection_to_internet);
                final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
                Log.e(typeMessage, this.error);
            }

        } catch (AppServerNotAvailableException e) {
            final String typeMessage = resources.getString(R.string.type_message_produced_exception);
            String msgError = typeMessage + e.getMessage();
            Log.e(TAG, msgError, e);
        }
        return result;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        this.progressDialog.dismiss();

        if (this.error != null) {
            Toast.makeText(this.context, this.error, Toast.LENGTH_SHORT).show();
        } else if (aBoolean) {
            Intent myIntent = new Intent(this.context,
                    WordActivity.class);

            myIntent.putExtra("idWord", this.idWord);

            this.context.startActivity(myIntent);
        }
    }
}
