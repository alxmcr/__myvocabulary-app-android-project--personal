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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import alxmcr.bo.vocabularyapp_v8.R;
import bo.alxmcr.app.VocabularyApplication;
import bo.alxmcr.app.activities.main.DashboardActivity;
import bo.alxmcr.app.activities.main.MainActivity;
import bo.alxmcr.app.activities.meaning.CreateMeaningActivity;
import bo.alxmcr.app.activities.meaning.MeaningActivity;
import bo.alxmcr.app.asynctask.word.DeleteWordByIdTaskAsync;
import bo.alxmcr.app.asynctask.word.SearchWordByIdTaskAsync;
import bo.alxmcr.app.models.Word;

public class WordActivity extends Activity {

    public static final String TAG = WordActivity.class.getSimpleName();
    private String idWord;
    private ListView listViewMeanings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "Start: onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);

		
        final TextView textWordSelected = (TextView) this.findViewById(R.id.textWordSelected);
        Button btnDeleteWord = (Button) this.findViewById(R.id.btnDeleteWord);
        Button btnEditWord = (Button) this.findViewById(R.id.btnEditWord);
        Button btnNewMeaning = (Button) this.findViewById(R.id.btnNewMeaning);

        this.listViewMeanings = (ListView) this.findViewById(R.id.listViewMeanings);

        //Gets information of Intent
        String nameParameter = "idWord";
        this.idWord = this.getStringFromIntent(nameParameter);

        final VocabularyApplication app = ((VocabularyApplication) getApplicationContext());
        
        new SearchWordByIdTaskAsync(app, this, getResources(), textWordSelected, listViewMeanings).execute(this.idWord);
		
		//Count number of connections to database
		int numberConnectionsDB = app.getContadorConexiones();
		Log.v(TAG, "#Connections:" + numberConnectionsDB);

		numberConnectionsDB++;
        app.setContadorConexiones(numberConnectionsDB);

        TextView numberOfConnectionsDB = (TextView) this.findViewById(R.id.numberOfConnectionsDB);
        numberOfConnectionsDB.setText(String.valueOf(numberConnectionsDB));

        final Activity self = this;
        final String auxIdWord = this.idWord;

        btnDeleteWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeleteWordByIdTaskAsync(app, self, self.getResources()).execute(auxIdWord);
				
				//Count number of connections to database
				int numberConnectionsDB = app.getContadorConexiones();
				Log.v(TAG, "#Connections:" + numberConnectionsDB);
				numberConnectionsDB++;
                app.setContadorConexiones(numberConnectionsDB);
            }
        });

        btnEditWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(self,
                        EditWordActivity.class);
                myIntent.putExtra("idWord", auxIdWord);

                startActivity(myIntent);
            }
        });

        btnNewMeaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(self,
                        CreateMeaningActivity.class);
                myIntent.putExtra("idWord", auxIdWord);

                startActivity(myIntent);
            }
        });

        //Action for any item of ListView
        this.actionItemListViewMeanings(listViewMeanings);
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

    private void actionItemListViewMeanings(ListView listViewMeanings) {
        Log.v(TAG, "Start: actionItemListViewMeanings()");

        if (listViewMeanings != null) {

            final Activity self = this;
            final String auxIdWord = this.idWord;

            AdapterView.OnItemClickListener onItemListener = new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Value textView 'item selected'
                    TextView textViewIdMeaningSelected = (TextView) view.findViewById(R.id.textViewIdMeaning);

                    //Get idWord 'item selected'
                    String idMeaning = "";
                    if (textViewIdMeaningSelected != null) {
                        idMeaning = String.valueOf(textViewIdMeaningSelected.getText());
                    }
                    idMeaning = idMeaning.trim();

                    //
                    Resources resources = self.getResources();
                    if (idMeaning != null) {

                        Intent myIntent = new Intent(self,
                                MeaningActivity.class);

                        myIntent.putExtra("idMeaning", idMeaning);
                        myIntent.putExtra("idWord", auxIdWord);

                        startActivity(myIntent);
                    } else {
                        final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
                        final String msg = TAG + resources.getString(R.string.msg_error_string_is_null);
                        Log.e(typeMessage, msg);
                    }

                }
            };


            listViewMeanings.setOnItemClickListener(onItemListener);

        } else {
            final String typeMessage = this.getResources().getString(R.string.type_message_produced_error);
            String msgError = this.getResources().getString(R.string.msg_error_list_view_is_null);
            Log.e(typeMessage, msgError);
        }
        Log.v(TAG, "End: actionItemListViewMeanings()");
    }
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_word, menu);
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
