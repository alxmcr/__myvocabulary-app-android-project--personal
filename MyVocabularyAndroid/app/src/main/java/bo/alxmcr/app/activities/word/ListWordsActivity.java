package bo.alxmcr.app.activities.word;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;

import alxmcr.bo.vocabularyapp_v8.R;
import bo.alxmcr.app.VocabularyApplication;
import bo.alxmcr.app.activities.main.DashboardActivity;
import bo.alxmcr.app.activities.main.MainActivity;
import bo.alxmcr.app.adapters.word.WordAdapter;
import bo.alxmcr.app.asynctask.word.ReadWordTaskAsync;
import bo.alxmcr.app.broadcast_receivers.word.ReadWordsBroadcastReceiver;
import bo.alxmcr.app.constants.ConstantsIntentFilters;
import bo.alxmcr.app.models.Word;
import bo.alxmcr.app.services.word.ReaderWordsService;
import bo.alxmcr.app.sqlite.WordSQLiteOperations;
import bo.alxmcr.utils.general.GeneralUtils;

public class ListWordsActivity extends Activity {

    public static final String TAG = ListWordsActivity.class.getSimpleName();

    private ArrayList<Word> listWords = new ArrayList<Word>();
    private ListView listViewWords;
    private TextView textViewNumberWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_words);

        //Get TextView app
        this.textViewNumberWords = (TextView) this.findViewById(R.id.textViewNumberWords);

        //Get ListView app
        this.listViewWords = (ListView) this.findViewById(R.id.listViewWords);

        if (this.listViewWords != null) {
            if (GeneralUtils.isConnectingToInternet(this)) {
                Log.d(TAG, "Have connection to internet");

                VocabularyApplication app = ((VocabularyApplication) getApplicationContext());
                
                new ReadWordTaskAsync(app, this, getResources(), this.listViewWords, this.textViewNumberWords).execute();
				
				//Count number of connections to database
                int numberConnectionsDB = app.getContadorConexiones();
                Log.v(TAG, "#Connections:" + numberConnectionsDB);
                numberConnectionsDB++;
                app.setContadorConexiones(numberConnectionsDB);
                TextView numberOfConnectionsDB = (TextView) this.findViewById(R.id.numberOfConnectionsDB);
                numberOfConnectionsDB.setText(String.valueOf(numberConnectionsDB));
            } else {
                Log.d(TAG, "Not have connection to internet...");
            }
        } else {
            Log.e(TAG, "Error, listView is null");
        }

        //Action for any item of ListView
        this.actionItemListViewWord(listViewWords);
    }

    private void updateNumberOfWords(int numberWords) {
        this.textViewNumberWords.setText(String.valueOf(numberWords));
    }

    private void updateListViewWords(ArrayList<Word> words) {
        if (words != null) {
            WordAdapter adapter = new WordAdapter(this, R.layout.activity_list_words_item_word, words);
            this.listViewWords.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            final String msg = getResources().getString(R.string.msg_error_list_is_empty);
            final String typeMessage = TAG + getResources().getString(R.string.type_message_produced_error);
            Toast.makeText(this, typeMessage + msg, Toast.LENGTH_SHORT).show();
        }
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
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_words, menu);
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
