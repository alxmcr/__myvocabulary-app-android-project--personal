package bo.alxmcr.app.activities.word;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import alxmcr.bo.vocabularyapp_v8.R;
import bo.alxmcr.app.VocabularyApplication;
import bo.alxmcr.app.activities.main.DashboardActivity;
import bo.alxmcr.app.activities.main.MainActivity;
import bo.alxmcr.app.asynctask.word.CreateWordTaskAsync;
import bo.alxmcr.app.asynctask.word.SearchWordByTextTaskAsync;
import bo.alxmcr.utils.general.GeneralUtils;

public class SearchWordActivity extends Activity {

    public static final String TAG = SearchWordActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_word);

        final Activity self = this;

        //Count number of connections to database
        int numberConnectionsDB = ((VocabularyApplication) getApplicationContext()).getContadorConexiones();
        Log.v(TAG, "#Connections:" + numberConnectionsDB);

        TextView numberOfConnectionsDB = (TextView) this.findViewById(R.id.numberOfConnectionsDB);
        numberOfConnectionsDB.setText(String.valueOf(numberConnectionsDB));

        Button btnSearchWord = (Button) this.findViewById(R.id.btnSearchWord);
        Button btnCleanUpSearchWord = (Button) this.findViewById(R.id.btnCleanUpSearchWord);

        btnSearchWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isConnectionToInternet = GeneralUtils.isConnectingToInternet(self);

                if (isConnectionToInternet) {

                    EditText editTextSearchWord = (EditText) self.findViewById(R.id.editTextSearchWord);

                    if (editTextSearchWord != null) {
                        Editable editableTextSearchWord = editTextSearchWord.getText();
                        final String textWordToSearch = editableTextSearchWord.toString().trim();

                        if (!textWordToSearch.isEmpty()) {
                            Intent myIntent = new Intent(self,
                                    MatchesWordsActivity.class);

                            myIntent.putExtra("textWordToSearch", textWordToSearch);

                            startActivity(myIntent);
                        } else {
                            String msg = "Error. Not empty inputs";
                            Toast.makeText(self, msg, Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        Log.e(TAG, "EditText not exists");
                    }
                } else {
                    String error = getResources().getString(R.string.msg_error_not_connection_to_internet);
                    Log.e(TAG, "Error:" + error);
                    Toast.makeText(self, error, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCleanUpSearchWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextSearchWord = (EditText) self.findViewById(R.id.editTextSearchWord);
                editTextSearchWord.setText("");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_word, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_menu_action_dashboard:
                this.irAlDashboard();
                return true;
            case R.id.option_menu_action_exit:
                this.closeApp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
