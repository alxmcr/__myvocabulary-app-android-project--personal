package bo.alxmcr.app.asynctask.dbmanaged;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import alxmcr.bo.vocabularyapp_v8.R;
import bo.alxmcr.app.VocabularyApplication;
import bo.alxmcr.app.extras.exceptions.connectivity.AppServerNotAvailableException;
import bo.alxmcr.app.httpclients.dbmanaged.ListPIDPostgreSQLHttpClientImpl;
import bo.alxmcr.app.httpclients.word.WordHttpClientImpl;
import bo.alxmcr.app.models.Word;
import bo.alxmcr.utils.general.GeneralUtils;

/**
 * Created by JoseCoca-i7 on 24/05/2015.
 */
public class ListPIDPostgreSQLTaskASync extends AsyncTask<Object, Void, ArrayList<Integer>> {

    public static final String TAG = ListPIDPostgreSQLTaskASync.class.getSimpleName();
    private Activity context;
    private ProgressDialog progressDialog;
    private Resources resources;
    private String error;
    private ListPIDPostgreSQLHttpClientImpl listPidsHttpClient;
    private int numberPids;
    private VocabularyApplication app;

    public ListPIDPostgreSQLTaskASync(VocabularyApplication app, Activity context, Resources resources) {
        //Estableciendo el dominio de la app
        this.app = app;
        String domainApp = this.app.getModoDeConexionServidor();
        this.listPidsHttpClient = new ListPIDPostgreSQLHttpClientImpl(domainApp);

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
                String titleProgressBar = this.resources.getString(R.string.title_count_connections);
                String msgProgressBar = this.resources.getString(R.string.msg_obtaining_count_connections_from_server);
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
    protected ArrayList<Integer> doInBackground(Object... params) {
        ArrayList<Integer> pids = null;

        boolean isConnectionToInternet = GeneralUtils.isConnectingToInternet(this.context);

        try {

            if (isConnectionToInternet) {

                pids = this.listPidsHttpClient.listPIDPostgreSQL(this.resources);

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

        return pids;
    }

    @Override
    protected void onPostExecute(ArrayList<Integer> pids) {
        progressDialog.dismiss();

        if (this.error != null) {
            Log.e(TAG, "Error:" + this.error);
            Toast.makeText(this.context, this.error, Toast.LENGTH_SHORT).show();
        } else if (pids != null && !pids.isEmpty()) {
            //Set number of words
            numberPids = pids.size();
            Log.v(TAG, "numberPids->" + numberPids);

            //Set contador de conexiones
            this.app.setContadorConexiones(numberPids);

            int numberConnectionsDB = this.app.getContadorConexiones();
            Log.v(TAG, "#Connections:" + numberConnectionsDB);
        }
    }

    public int getNumberPids() {
        return numberPids;
    }

    public void setNumberPids(int numberPids) {
        this.numberPids = numberPids;
    }
}
