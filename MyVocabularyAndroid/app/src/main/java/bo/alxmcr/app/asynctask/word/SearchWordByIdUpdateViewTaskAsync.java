package bo.alxmcr.app.asynctask.word;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import alxmcr.bo.vocabularyapp_v8.R;
import bo.alxmcr.app.VocabularyApplication;
import bo.alxmcr.app.adapters.meaning.MeaningAdapter;
import bo.alxmcr.app.httpclients.word.WordHttpClientImpl;
import bo.alxmcr.app.models.Meaning;
import bo.alxmcr.app.models.Word;
import bo.alxmcr.utils.general.GeneralUtils;

/**
 * Created by JoseCoca-i7 on 17/05/2015.
 */
public class SearchWordByIdUpdateViewTaskAsync extends AsyncTask<Object, Void, Word> {

    public static final String TAG = SearchWordByIdUpdateViewTaskAsync.class.getSimpleName();

    //Elements Activity
    private EditText editTextIdWord;
    private EditText editTextTextWord;
    private EditText editTextStatusWord;

    private ProgressDialog progressDialog;
    private Activity context;
    private Resources resources;
    private String error;
    private WordHttpClientImpl wordHttpClient;
	private VocabularyApplication app;

    public SearchWordByIdUpdateViewTaskAsync(VocabularyApplication app, Activity context, Resources resources, EditText editTextIdWord, EditText editTextTextWord, EditText editTextStatusWord) {
		
		//Estableciendo el dominio de la app
        this.app = app;
        String domainApp = this.app.getModoDeConexionServidor();
		this.wordHttpClient = new WordHttpClientImpl(domainApp);
		
		//estableciendo algunos atributos
        this.context = context;
        this.resources = resources;
        this.editTextIdWord = editTextIdWord;
        this.editTextTextWord = editTextTextWord;
        this.editTextStatusWord = editTextStatusWord;

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
                String titleProgressBar = this.resources.getString(R.string.title_activity_search_meaning);
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
    }

    @Override
    protected Word doInBackground(Object... params) {
        int numberElements = 0;
        Word wordById = null;

        String idWord = null;

        if (params != null) {
            numberElements = params.length;
        } else {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            String msg = TAG + resources.getString(R.string.msg_error_parameters_of_input_is_null);
            Log.e(typeMessage, msg);
        }

        if (numberElements > 0) {
            //From Activity
            idWord = String.valueOf(params[0]);
        } else {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            String msg = TAG + resources.getString(R.string.msg_error_array_is_empty);
            Log.e(typeMessage, msg);
        }

        boolean isConnectionToInternet = GeneralUtils.isConnectingToInternet(this.context);

        if (isConnectionToInternet) {
            wordById = this.wordHttpClient.searchWordById(resources, idWord);
        } else {
            this.error = resources.getString(R.string.msg_error_not_connection_to_internet);
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            Log.e(typeMessage, this.error);
        }
        return wordById;
    }

    @Override
    protected void onPostExecute(Word word) {
        if (this.error != null) {
            Toast.makeText(this.context, this.error, Toast.LENGTH_SHORT).show();
        } else {
            String idWord = null;
            String text = null;
            String status = null;
            if (word != null) {
                idWord = word.getId();
                text = word.getText();
                status = word.getStatus();
            }

            this.updateIdWord(idWord);
            this.updateTextWord(text);
            this.updateStatusWord(status);
        }
    }

    private void updateIdWord(String idWord) {
        if (idWord != null) {
            this.editTextIdWord.setText(idWord);
        } else {
            final String msg = resources.getString(R.string.msg_error_string_is_null);
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateTextWord(String text) {
        if (text != null) {
            this.editTextTextWord.setText(text);
        } else {
            final String msg = resources.getString(R.string.msg_error_string_is_null);
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateStatusWord(String status) {
        if (status != null) {
            this.editTextStatusWord.setText(status);
        } else {
            final String msg = resources.getString(R.string.msg_error_string_is_null);
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show();
        }
    }

}
