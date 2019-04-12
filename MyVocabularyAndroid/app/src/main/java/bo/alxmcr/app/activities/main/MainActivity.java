package bo.alxmcr.app.activities.main;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import alxmcr.bo.vocabularyapp_v8.R;
import bo.alxmcr.app.VocabularyApplication;
import bo.alxmcr.app.asynctask.dbmanaged.ListPIDPostgreSQLTaskASync;
import bo.alxmcr.app.broadcast_receivers.word.ReadWordsBroadcastReceiver;
import bo.alxmcr.app.constants.ConstantsIntentFilters;
import bo.alxmcr.app.constants.ConstantsURLs;
import bo.alxmcr.utils.app.MyAppUtils;

public class MainActivity extends Activity {

    public static final String TAG = DashboardActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VocabularyApplication app = (VocabularyApplication) getApplicationContext();
        this.setTypeDomainConnectionToServer(app, ConstantsURLs.DOMAIN_APP_REMOTE);

        this.resetCountConnectionsDB(app);

        //Go to 'Dashboard'
        Button btnSignIn = (Button) this.findViewById(R.id.btnSignIn);
        this.goToDashboard(btnSignIn);
    }

    private void setTypeDomainConnectionToServer(VocabularyApplication app, String domainAppRemote) {
        app.setModoDeConexionServidor(domainAppRemote);
    }

    private void resetCountConnectionsDB(VocabularyApplication app) {
        Log.v(TAG, "Start: resetCountConnectionsDB()");

        ListPIDPostgreSQLTaskASync taskListPids = new ListPIDPostgreSQLTaskASync(app, this, getResources());
        taskListPids.execute();

        Log.v(TAG, "End: resetCountConnectionsDB()");
    }

    private void goToDashboard(Button btnSignIn) {
        if (btnSignIn != null) {

            final Activity self = this;

            btnSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(self,
                            DashboardActivity.class);

                    startActivity(myIntent);
                }
            });
        } else {
            final String typeMessage = getResources().getString(R.string.type_message_produced_error);
            String msgError = getResources().getString(R.string.msg_error_button_is_null);
            Log.e(typeMessage, msgError);
        }
    }
}
