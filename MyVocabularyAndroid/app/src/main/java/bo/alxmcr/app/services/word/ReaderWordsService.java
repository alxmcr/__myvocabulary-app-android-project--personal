package bo.alxmcr.app.services.word;

import android.app.Application;
import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.os.IBinder;
import android.util.Log;

import bo.alxmcr.app.constants.ConstantsIntentFilters;
import bo.alxmcr.app.threads.word.ReaderWordsThread;


/**
 * Created by JOSECOCA on 01/05/2015.
 */
public class ReaderWordsService extends Service {

    public static final String TAG = ReaderWordsService.class.getSimpleName();
    private boolean runFlag = false;
    private Resources resources;
    private ReaderWordsThread thread;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Application app = this.getApplication();

        //Create thread
        this.thread = new ReaderWordsThread(this);
        this.thread.setResources(app.getResources());
        this.thread.setService(this);

        Log.d(TAG, "onCreated");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if(!this.runFlag){
            this.runFlag = true;
            this.thread.start();
        }

        Log.d(TAG, "onStart");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        this.runFlag = false;
        this.thread.interrupt();
        this.thread = null;

        Log.d(TAG, "onDestroy");
    }

    public boolean isRunFlag() {
        return runFlag;
    }

    public void setRunFlag(boolean runFlag) {
        this.runFlag = runFlag;
    }

    @Override
    public Resources getResources() {
        return resources;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }
}

