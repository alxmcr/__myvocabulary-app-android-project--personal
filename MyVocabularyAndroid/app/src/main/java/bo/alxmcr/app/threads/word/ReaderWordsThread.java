package bo.alxmcr.app.threads.word;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.nfc.Tag;
import android.util.Log;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;

import bo.alxmcr.app.VocabularyApplication;
import bo.alxmcr.app.asynctask.word.ReadWordTaskAsync;
import bo.alxmcr.app.constants.ConstantsFormat;
import bo.alxmcr.app.constants.ConstantsIntentFilters;
import bo.alxmcr.app.httpclients.word.WordHttpClientImpl;
import bo.alxmcr.app.models.Word;
import bo.alxmcr.app.services.word.ReaderWordsService;
import bo.alxmcr.app.sqlite.WordSQLiteHelper;
import bo.alxmcr.app.sqlite.WordSQLiteOperations;
import bo.alxmcr.utils.general.GeneralUtils;

/**
 * Created by JOSECOCA on 01/05/2015.
 */
public class ReaderWordsThread extends Thread {

    public static final String TAG = ReaderWordsThread.class.getSimpleName();
    private static final int DELAY = 30000; //30 seconds
    private ReaderWordsService service;
    private Resources resources;
    private Context context;
    private WordSQLiteOperations dbOperations;

    public ReaderWordsThread(Context context) {
        this.context = context;
        this.dbOperations = new WordSQLiteOperations(this.context);
    }

    @Override
    public void run() {
        Log.d(TAG, "run()...");
        long t0 = System.currentTimeMillis();

        Log.d(TAG, "runThreadFlag->" + String.valueOf(this.service.isRunFlag()));
        while (this.service.isRunFlag()) {
            Log.d(TAG, "ReaderWordsService-" + TAG + "->" + "running...");
            long t1 = System.currentTimeMillis();
            int seconds = (int) (t1 - t0) / 1000;
            Log.d(TAG, "Elapsed: " + seconds + "\r");

            try {

                //Estableciendo el dominio de la app
                VocabularyApplication app = (VocabularyApplication) this.context.getApplicationContext();
                String domainApp = app.getModoDeConexionServidor();
                
                ArrayList<Word> listWords = new WordHttpClientImpl(domainApp).readWords(this.resources);

                if (listWords != null && !listWords.isEmpty()) {
                    ContentValues valuesDB = new ContentValues();

                    for (Word word : listWords) {

                        Date creationDate = word.getCreationDate();
                        Time creationTime = word.getCreationTime();
                        Date modificationDate = word.getModificationDate();
                        Time modificationTime = word.getModificationTime();

                        valuesDB.clear();
                        valuesDB.put("id", word.getId());
                        valuesDB.put("creationDate", GeneralUtils.convertDateSQLToString(creationDate, ConstantsFormat.FORMAT_DATE_1));
                        valuesDB.put("creationTime", GeneralUtils.convertTimeSQLToString(creationTime, ConstantsFormat.FORMAT_TIME_1));
                        valuesDB.put("modificationDate", GeneralUtils.convertDateSQLToString(modificationDate, ConstantsFormat.FORMAT_DATE_1));
                        valuesDB.put("modificationTime", GeneralUtils.convertTimeSQLToString(modificationTime, ConstantsFormat.FORMAT_TIME_1));
                        valuesDB.put("status", word.getStatus());
                        valuesDB.put("text", word.getText());

                        this.dbOperations.insertOrIgnore(valuesDB);
                    }
                } else {
                    Log.e(TAG, "List is null o empty");
                }


                //Comunicate progress
                Intent bcIntent = new Intent();
                bcIntent.setAction(ConstantsIntentFilters.ACTION_START);
                this.service.sendBroadcast(bcIntent);

                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                Log.d(TAG, "Start sleeping...");
                this.service.setRunFlag(false);
                //this.endThreadTask();
            } catch (ParseException e) {
                Log.e(TAG, "Error:" + e.getMessage(), e);
            }
        }
    }

    private void endThreadTask() {
        Log.d(TAG, "endThreadTask...");
        //Comunicate progress
        Intent bcIntent = new Intent();
        bcIntent.setAction(ConstantsIntentFilters.ACTION_END);
        this.service.sendBroadcast(bcIntent);
    }

    public ReaderWordsService getService() {
        return service;
    }

    public void setService(ReaderWordsService service) {
        this.service = service;
    }

    public Resources getResources() {
        return resources;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
