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
import bo.alxmcr.app.asynctask.word.UpdateWordTaskAsync;
import bo.alxmcr.utils.general.GeneralUtils;

public class CreateWordActivity extends Activity {

    public static final String TAG = CreateWordActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_word);

        final Activity self = this;

        //Count number of connections to database
        final VocabularyApplication app = ((VocabularyApplication) getApplicationContext());
        int numberConnectionsDB = app.getContadorConexiones();
        Log.v(TAG, "#Connections:" + numberConnectionsDB);

        TextView numberOfConnectionsDB = (TextView) this.findViewById(R.id.numberOfConnectionsDB);
        numberOfConnectionsDB.setText(String.valueOf(numberConnectionsDB));

        Button btnSaveNewWord = (Button) this.findViewById(R.id.btnSaveNewWord);
        Button btnCleanUpNewWord = (Button) this.findViewById(R.id.btnCleanUpNewWord);

        final EditText editTextNewWord = (EditText) this.findViewById(R.id.editTextNewWord);

        btnSaveNewWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isConnectionToInternet = GeneralUtils.isConnectingToInternet(self);

                if (isConnectionToInternet) {

                    EditText editTextNewWord = (EditText) self.findViewById(R.id.editTextNewWord);

                    Editable editableTextNewWord = editTextNewWord.getText();
                    final String textWord = editableTextNewWord.toString().trim();

                    if (!textWord.isEmpty()) {
                        new CreateWordTaskAsync(app, self, self.getResources()).execute(textWord);

                        //Count number of connections to database
                        int numberConnectionsDB = app.getContadorConexiones();
                        Log.v(TAG, "#Connections:" + numberConnectionsDB);
                        numberConnectionsDB++;
                        app.setContadorConexiones(numberConnectionsDB);

                    } else {
                        String msg = "Error. Not empty inputs";
                        Toast.makeText(self, msg, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String error = getResources().getString(R.string.msg_error_not_connection_to_internet);
                    Log.e(TAG, "Error:" + error);
                    Toast.makeText(self, error, Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnCleanUpNewWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextNewWord.setText("");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_word, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_menu_action_dashboard:
                this.irAlDashboard();
                return true;
            case R.id.option_menu_action_exit:
                Intent myIntent = new Intent(this,
                        MainActivity.class);

                startActivity(myIntent);
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
