package bo.alxmcr.app.activities.word;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;

import alxmcr.bo.vocabularyapp_v8.R;
import bo.alxmcr.app.VocabularyApplication;
import bo.alxmcr.app.activities.main.DashboardActivity;
import bo.alxmcr.app.activities.main.MainActivity;
import bo.alxmcr.app.adapters.word.WordAdapter;
import bo.alxmcr.app.asynctask.word.ReadWordTaskAsync;
import bo.alxmcr.app.asynctask.word.SearchWordByTextTaskAsync;
import bo.alxmcr.app.extras.converts.ConvertsTypeWord;
import bo.alxmcr.app.models.Word;
import bo.alxmcr.app.sqlite.WordSQLiteOperations;
import bo.alxmcr.utils.general.GeneralUtils;

public class MatchesWordsActivity extends Activity {

    public static final String TAG = MatchesWordsActivity.class.getSimpleName();

    private ListView listViewWords;
    private TextView textViewNumberWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches_words);

        //Get TextView app
        this.textViewNumberWords = (TextView) this.findViewById(R.id.textViewNumberWords);

        //Get ListView app
        this.listViewWords = (ListView) this.findViewById(R.id.listViewWords);

        if (this.listViewWords != null) {
            //Gets information of Intent
            String nameParameter = "textWordToSearch";
            String textWordToSearch = this.getInformationOfIntent(nameParameter);

            VocabularyApplication app = ((VocabularyApplication) getApplicationContext());

            new SearchWordByTextTaskAsync(app, this, this.getResources(), this.listViewWords, this.textViewNumberWords).execute(textWordToSearch);
			
			//Count number of connections to database
			int numberConnectionsDB = app.getContadorConexiones();
			Log.v(TAG, "#Connections:" + numberConnectionsDB);
            TextView numberOfConnectionsDB = (TextView) this.findViewById(R.id.numberOfConnectionsDB);
            numberOfConnectionsDB.setText(String.valueOf(numberConnectionsDB));
			numberConnectionsDB++;
            app.setContadorConexiones(numberConnectionsDB);
			
        } else {
            Log.e(TAG, "Error, listView is null");
        }

        //Action for any item of ListView
        this.actionItemListViewWord(listViewWords);
    }

    private void actionItemListViewWord(ListView listViewWords) {
        if (listViewWords != null) {

            final Activity self = this;

            AdapterView.OnItemClickListener onItemListener = new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Value textView 'item selected'
                    TextView textViewIdWordSelected = (TextView) view.findViewById(R.id.textViewIdWord);

                    //Get idWord 'item selected'
                    Word word = null;
                    String idWord = "";
                    if (textViewIdWordSelected != null) {
                        idWord = String.valueOf(textViewIdWordSelected.getText());
                    }
                    idWord = idWord.trim();

                    //
                    Resources resources = self.getResources();
                    if (idWord != null) {

                        Intent myIntent = new Intent(self,
                                WordActivity.class);

                        myIntent.putExtra("idWord", idWord);

                        startActivity(myIntent);
                    } else {
                        final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
                        final String msg = TAG + resources.getString(R.string.msg_error_string_is_null);
                        Log.e(typeMessage, msg);
                    }

                }
            };


            listViewWords.setOnItemClickListener(onItemListener);

        } else {
            final String typeMessage = this.getResources().getString(R.string.type_message_produced_error);
            String msgError = this.getResources().getString(R.string.msg_error_list_view_is_null);
            Log.e(typeMessage, msgError);
        }
    }

    private String getInformationOfIntent(String nameParameter) {
        Bundle bundle = null;
        String textWordToSearch = null;

        if (nameParameter != null && !nameParameter.isEmpty()) {
            bundle = this.getIntent().getExtras();
        } else {
            final String typeMessage = TAG + this.getResources().getString(R.string.type_message_produced_error);
            String msg = TAG + this.getResources().getString(R.string.msg_error_string_is_null_or_empty);
            Log.e(typeMessage, msg);
        }

        if (bundle != null) {
            textWordToSearch = bundle.getString(nameParameter).toString();
        } else {
            final String typeMessage = TAG + this.getResources().getString(R.string.type_message_produced_error);
            String msg = TAG + this.getResources().getString(R.string.msg_error_bundle_is_null);
            Log.e(typeMessage, msg);
        }

        return textWordToSearch;
    }

@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_matches_words, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId ())
        {
            case R.id.option_menu_action_dashboard:
                this.irAlDashboard();
                return true;
            case R.id.option_menu_action_exit:
                this.closeApp();
                return true;
            default:
                return super.onOptionsItemSelected (item);
        }
    }

    private void irAlDashboard() {
        Intent myIntent = new Intent(this,
                DashboardActivity.class);

        startActivity(myIntent);
    }

     private void closeApp() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
