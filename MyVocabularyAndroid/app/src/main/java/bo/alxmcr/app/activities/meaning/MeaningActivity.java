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

import alxmcr.bo.vocabularyapp_v8.R;
import bo.alxmcr.app.VocabularyApplication;
import bo.alxmcr.app.activities.main.DashboardActivity;
import bo.alxmcr.app.activities.main.MainActivity;
import bo.alxmcr.app.activities.word.SearchWordActivity;
import bo.alxmcr.app.asynctask.meaning.DeleteMeaningByIdTaskAsync;
import bo.alxmcr.app.asynctask.meaning.SearchMeaningByIdTaskAsync;
import bo.alxmcr.app.asynctask.word.DeleteWordByIdTaskAsync;
import bo.alxmcr.app.asynctask.word.UpdateWordTaskAsync;

public class MeaningActivity extends Activity {

    public static final String TAG = MeaningActivity.class.getSimpleName();

    private String idMeaning;
    private String idWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "Start: onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meaning);

        final Activity self = this;

        TextView textMeaningSelected = (TextView) this.findViewById(R.id.textMeaningSelected);

        //Elements to view
        final EditText editTextIdWord = (EditText) this.findViewById(R.id.editTextIdWord);
        final EditText editTextTextWord = (EditText) this.findViewById(R.id.editTextTextWord);
        final EditText editTextStatusWord = (EditText) this.findViewById(R.id.editTextStatusWord);

        Button btnEditMeaning = (Button) this.findViewById(R.id.btnEditMeaning);
        Button btnDeleteMeaning = (Button) this.findViewById(R.id.btnDeleteMeaning);

        //Gets information of Intent
        this.idWord = this.getStringFromIntent("idWord");
        this.idMeaning = this.getStringFromIntent("idMeaning");

        final String auxIdWord = this.idWord;
        final String auxIdMeaning = this.idMeaning;

        final VocabularyApplication app = ((VocabularyApplication) getApplicationContext());
        
        new SearchMeaningByIdTaskAsync(app, this, this.getResources(), textMeaningSelected).execute(this.idMeaning);

        //Count number of connections to database
        int numberConnectionsDB = app.getContadorConexiones();
        Log.v(TAG, "#Connections:" + numberConnectionsDB);
        numberConnectionsDB++;
        app.setContadorConexiones(numberConnectionsDB);

        final TextView numberOfConnectionsDB = (TextView) this.findViewById(R.id.numberOfConnectionsDB);
        numberOfConnectionsDB.setText(String.valueOf(numberConnectionsDB));

        btnDeleteMeaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] data = {auxIdWord, auxIdMeaning};

                new DeleteMeaningByIdTaskAsync(app, self, self.getResources()).execute(data);
				
				//Count number of connections to database
				int numberConnectionsDB = app.getContadorConexiones();
				Log.v(TAG, "#Connections:" + numberConnectionsDB);

                numberOfConnectionsDB.setText(String.valueOf(numberConnectionsDB));

				numberConnectionsDB++;
                app.setContadorConexiones(numberConnectionsDB);
            }
        });

        btnEditMeaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(self,
                        EditMeaningActivity.class);

                myIntent.putExtra("idWord", auxIdWord);
                myIntent.putExtra("idMeaning", auxIdMeaning);

                startActivity(myIntent);
            }
        });
        Log.v(TAG, "End: onCreate()");
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
        getMenuInflater().inflate(R.menu.menu_meaning, menu);
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
