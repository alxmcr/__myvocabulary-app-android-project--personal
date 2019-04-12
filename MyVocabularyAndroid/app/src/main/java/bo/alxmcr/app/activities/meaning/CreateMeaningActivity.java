package bo.alxmcr.app.activities.meaning;

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

import java.util.Arrays;

import alxmcr.bo.vocabularyapp_v8.R;
import bo.alxmcr.app.VocabularyApplication;
import bo.alxmcr.app.activities.main.DashboardActivity;
import bo.alxmcr.app.activities.main.MainActivity;
import bo.alxmcr.app.activities.word.WordActivity;
import bo.alxmcr.app.asynctask.meaning.CreateMeaningTaskAsync;
import bo.alxmcr.app.asynctask.word.CreateWordTaskAsync;

public class CreateMeaningActivity extends Activity {

    public static final String TAG = CreateMeaningActivity.class.getSimpleName();
    private String idWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meaning);

        final Activity self = this;

        final TextView numberOfConnectionsDB = (TextView) this.findViewById(R.id.numberOfConnectionsDB);

        final VocabularyApplication app = ((VocabularyApplication) getApplicationContext());
        
        int numberConnectionsDB = app.getContadorConexiones();
        Log.v(TAG, "#Connections:" + numberConnectionsDB);

        numberOfConnectionsDB.setText(String.valueOf(numberConnectionsDB));

        Button btnSaveNewMeaning = (Button) this.findViewById(R.id.btnSaveNewMeaning);
        Button btnCleanUpNewMeaning = (Button) this.findViewById(R.id.btnCleanUpNewMeaning);

        final EditText editTextNewMeaning = (EditText) this.findViewById(R.id.editTextNewMeaning);

        //Gets information of Intent
        String nameParameter = "idWord";
        this.idWord = this.getStringFromIntent(nameParameter);

        final String auxIdWord = this.idWord;

        btnSaveNewMeaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Editable editableNewMeaning = editTextNewMeaning.getText();
                String textNewMeaning = editableNewMeaning.toString().trim();

                if (!auxIdWord.isEmpty() && !textNewMeaning.isEmpty()) {
                    String[] data = {auxIdWord, textNewMeaning};
                    Log.v(TAG, "data[]=" + Arrays.toString(data));
                    new CreateMeaningTaskAsync(app, self, self.getResources()).execute(data);
					
					//Count number of connections to database
					int numberConnectionsDB = app.getContadorConexiones();
					Log.v(TAG, "#Connections:" + numberConnectionsDB);

                    numberOfConnectionsDB.setText(String.valueOf(numberConnectionsDB));

					numberConnectionsDB++;
                    app.setContadorConexiones(numberConnectionsDB);
					
                } else {
                    String msg = "Error. Not empty inputs";
                    Toast.makeText(self, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCleanUpNewMeaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextNewMeaning.setText("");
            }
        });
    }

    private String getStringFromIntent(String nameParameter) {
        Bundle bundle = null;
        String textWordToSearch = null;

        if (nameParameter != null && !nameParameter.isEmpty()) {
            bundle = this.getIntent().getExtras();
        } else {
            final String typeMessage = TAG + this.getResources().getString(R.string.type_message_produced_error);
            String msg = TAG + this.getResources().getString(R.string.msg_error_string_is_null_or_empty);
            Log.e(typeMessage, msg);
        }

        String budleResult = null;
        if (bundle != null) {
            budleResult = bundle.getString(nameParameter);
        } else {
            final String typeMessage = TAG + this.getResources().getString(R.string.type_message_produced_error);
            String msg = TAG + this.getResources().getString(R.string.msg_error_bundle_is_null);
            Log.e(typeMessage, msg);
        }

        if (budleResult != null) {
            textWordToSearch = budleResult.toString();
        } else {
            final String typeMessage = TAG + this.getResources().getString(R.string.type_message_produced_error);
            String msg = TAG + this.getResources().getString(R.string.msg_error_string_is_null);
            Log.e(typeMessage, msg);
        }


        return textWordToSearch;
    }
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_meaning, menu);
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
