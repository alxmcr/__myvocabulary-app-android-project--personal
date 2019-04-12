package bo.alxmcr.app.activities.main;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import alxmcr.bo.vocabularyapp_v8.R;
import bo.alxmcr.app.VocabularyApplication;
import bo.alxmcr.app.activities.word.CreateWordActivity;
import bo.alxmcr.app.activities.word.ListWordsActivity;
import bo.alxmcr.app.activities.word.SearchWordActivity;
import bo.alxmcr.app.asynctask.dbmanaged.ListPIDPostgreSQLTaskASync;
import bo.alxmcr.utils.general.GeneralUtils;

public class DashboardActivity extends Activity {

    public static final String TAG = DashboardActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        final Activity self = this;

        //Count number of connections to database
        int numberConnectionsDB = ((VocabularyApplication) getApplicationContext()).getContadorConexiones();
        Log.v(TAG, "#Connections:" + numberConnectionsDB);

        TextView numberOfConnectionsDB = (TextView) this.findViewById(R.id.numberOfConnectionsDB);
        numberOfConnectionsDB.setText(String.valueOf(numberConnectionsDB));

        Button btnActionNewWord = (Button) this.findViewById(R.id.btnActionNewWord);
        Button btnActionListWord = (Button) this.findViewById(R.id.btnActionListWord);
        Button btnActionSearchWord = (Button) this.findViewById(R.id.btnActionSearchWord);

        btnActionNewWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(self,
                        CreateWordActivity.class);

                startActivity(myIntent);
            }
        });

        btnActionListWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isConnectionToInternet = GeneralUtils.isConnectingToInternet(self);

                if (isConnectionToInternet) {
                    Intent myIntent = new Intent(self,
                            ListWordsActivity.class);

                    startActivity(myIntent);
                } else {
                    String error = getResources().getString(R.string.msg_error_not_connection_to_internet);
                    Log.e(TAG, "Error:" + error);
                    Toast.makeText(self, error, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnActionSearchWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(self,
                        SearchWordActivity.class);

                startActivity(myIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
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
