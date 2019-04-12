package bo.alxmcr.app.asynctask.word;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;

import alxmcr.bo.vocabularyapp_v8.R;
import bo.alxmcr.app.VocabularyApplication;
import bo.alxmcr.app.activities.word.ListWordsActivity;
import bo.alxmcr.app.adapters.word.WordAdapter;
import bo.alxmcr.app.constants.ConstantsFormat;
import bo.alxmcr.app.httpclients.word.WordHttpClientImpl;
import bo.alxmcr.app.models.Word;
import bo.alxmcr.app.sqlite.WordSQLiteOperations;
import bo.alxmcr.utils.general.GeneralUtils;

/**
 * Created by JoseCoca-i7 on 02/05/2015.
 */
public class ReadWordTaskAsync extends AsyncTask<Object, Void, ArrayList<Word>> {

    public static final String TAG = ReadWordTaskAsync.class.getSimpleName();

    private Activity context;
    private Resources resources;
    private ProgressDialog progressDialog;
    private String error;
    private WordHttpClientImpl wordHttpClient;
    private ListView listViewWords;
    private TextView textViewNumberWords;
	private VocabularyApplication app;


    public ReadWordTaskAsync(VocabularyApplication app, Activity context, Resources resources, ListView listViewWords, TextView textViewNumberWords) {
		
		//Estableciendo el dominio de la app
        this.app = app;
        String domainApp = this.app.getModoDeConexionServidor();
		this.wordHttpClient = new WordHttpClientImpl(domainApp);
		
		//estableciendo algunos atributos
        this.context = context;
        this.resources = resources;
        this.listViewWords = listViewWords;
        this.textViewNumberWords = textViewNumberWords;

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
                String titleProgressBar = this.resources.getString(R.string.title_activity_list_words);
                String msgProgressBar = this.resources.getString(R.string.msg_obtaining_words_from_server);
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
    protected ArrayList<Word> doInBackground(Object... params) {

        ArrayList<Word> listWords = null;

        boolean isConnectionToInternet = GeneralUtils.isConnectingToInternet(this.context);

        if (isConnectionToInternet) {
            listWords = this.wordHttpClient.readWords(this.resources);
        } else {
            this.error = resources.getString(R.string.msg_error_not_connection_to_internet);
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            Log.e(typeMessage, this.error);
        }

        return listWords;

    }

    @Override
    protected void onPostExecute(ArrayList<Word> words) {
        progressDialog.dismiss();


        if (this.error != null) {
            Log.e(TAG, "Error:" + this.error);
            Toast.makeText(this.context, this.error, Toast.LENGTH_SHORT).show();
        } else if (words != null && !words.isEmpty()) {

            //Set number of words
            int numberWords = 0;
            numberWords = words.size();

            //Updating view
            this.updateNumberOfWords(numberWords);
            this.updateListViewWords(words);
        }

    }

    private void updateNumberOfWords(int numberWords) {
        this.textViewNumberWords.setText(String.valueOf(numberWords));
    }

    private void updateListViewWords(ArrayList<Word> words) {
        if (words != null) {
            WordAdapter adapter = new WordAdapter(this.context, R.layout.activity_list_words_item_word, words);
            this.listViewWords.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            final String msg = resources.getString(R.string.msg_error_list_is_empty);
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    public Resources getResources() {
        return resources;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public Activity getContext() {
        return context;
    }

    public void setContext(Activity context) {
        this.context = context;
    }
}
