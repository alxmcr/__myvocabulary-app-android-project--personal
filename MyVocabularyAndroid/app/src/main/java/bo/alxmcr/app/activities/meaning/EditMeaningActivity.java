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
import bo.alxmcr.app.activities.word.WordActivity;
import bo.alxmcr.app.asynctask.meaning.SearchMeaningByIdUpdateViewTaskAsync;
import bo.alxmcr.app.asynctask.meaning.UpdateMeaningTaskAsync;
import bo.alxmcr.app.asynctask.word.SearchWordByIdUpdateViewTaskAsync;
import bo.alxmcr.app.asynctask.word.UpdateWordTaskAsync;

public class EditMeaningActivity extends Activity {

    public static final String TAG = EditMeaningActivity.class.getSimpleName();
    private String idWord;
    private String idMeaning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_meaning);

        final Activity self = this;

        //Elements to view
        final EditText editTextIdMeaning = (EditText) this.findViewById(R.id.editTextIdMeaning);
        final EditText editTextTextMeaning = (EditText) this.findViewById(R.id.editTextTextMeaning);
        final EditText editTextStatusMeaning = (EditText) this.findViewById(R.id.editTextStatusMeaning);

        Button btnSaveEditMeaning = (Button) this.findViewById(R.id.btnSaveEditMeaning);
        Button btnCleanUpEditMeaning = (Button) this.findViewById(R.id.btnCleanUpEditMeaning);

        this.idWord = this.getStringFromIntent("idWord");
        this.idMeaning = this.getStringFromIntent("idMeaning");

        final String auxIdWord = this.idWord;
        final String auxIdMeaning = this.idMeaning;

        final VocabularyApplication app = ((VocabularyApplication) getApplicationContext());

        new SearchMeaningByIdUpdateViewTaskAsync(app, this, this.getResources(), editTextIdMeaning, editTextTextMeaning, editTextStatusMeaning).execute(this.idMeaning);
		
		//Count number of connections to database
		int numberConnectionsDB = app.getContadorConexiones();
		Log.v(TAG, "#Connections:" + numberConnectionsDB);

        TextView numberOfConnectionsDB = (TextView) this.findViewById(R.id.numberOfConnectionsDB);
        numberOfConnectionsDB.setText(String.valueOf(numberConnectionsDB));

        numberConnectionsDB++;
		((VocabularyApplication)getApplicationContext()).setContadorConexiones(numberConnectionsDB);

        btnSaveEditMeaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable editableTextWord = editTextTextMeaning.getText();
                Editable editableStatusWord = editTextStatusMeaning.getText();
                String textMeaning = editableTextWord.toString().trim();
                String statusMeaning = editableStatusWord.toString().trim();

                if (!auxIdWord.isEmpty() && !auxIdMeaning.isEmpty() && !textMeaning.isEmpty() && !statusMeaning.isEmpty()) {
                    String[] data = {auxIdWord, auxIdMeaning, textMeaning, statusMeaning};

                    new UpdateMeaningTaskAsync(app, self, self.getResources()).execute(data);
					
					//Count number of connections to database
					int numberConnectionsDB = app.getContadorConexiones();
					Log.v(TAG, "#Connections:" + numberConnectionsDB);
					numberConnectionsDB++;
                    app.setContadorConexiones(numberConnectionsDB);
					
                } else {
                    String msg = "Error. Not empty inputs";
                    Toast.makeText(self, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCleanUpEditMeaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextTextMeaning.setText("");
                editTextStatusMeaning.setText("");
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
        getMenuInflater().inflate(R.menu.menu_edit_meaning, menu);
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
