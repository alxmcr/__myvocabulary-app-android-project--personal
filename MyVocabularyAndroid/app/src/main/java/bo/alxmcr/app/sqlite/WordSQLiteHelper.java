package bo.alxmcr.app.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import bo.alxmcr.app.activities.word.ListWordsActivity;
import bo.alxmcr.app.constants.ConstantsSQLLiteDB;

/**
 * Created by JoseCoca-i7 on 02/05/2015.
 */
public class WordSQLiteHelper extends SQLiteOpenHelper {

    public static final String TAG = WordSQLiteHelper.class.getSimpleName();

    public WordSQLiteHelper(Context context, String nameDataBase, SQLiteDatabase.CursorFactory factory, int numberVersionDatabase) {
        super(context, nameDataBase, factory, numberVersionDatabase);
        Log.d(TAG, "Constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "sql: " + ConstantsSQLLiteDB.SQL_CREATE_TABLE_WORD);

        db.execSQL(ConstantsSQLLiteDB.SQL_CREATE_TABLE_WORD);
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade");

        String sqlDropTable = "DROP TABLE IF EXISTS WORD";
        db.execSQL(sqlDropTable);

        //Create table
        db.execSQL(ConstantsSQLLiteDB.SQL_CREATE_TABLE_WORD);
    }
}
