package bo.alxmcr.app.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;

import bo.alxmcr.app.constants.ConstantsFormat;
import bo.alxmcr.app.constants.ConstantsSQLLiteDB;
import bo.alxmcr.app.models.Word;
import bo.alxmcr.utils.general.GeneralUtils;

/**
 * Created by JoseCoca-i7 on 02/05/2015.
 */
public class WordSQLiteOperations {

    public static final String TAG = WordSQLiteHelper.class.getSimpleName();

    private WordSQLiteHelper helperSQLite;

    public WordSQLiteOperations(Context context) {
        Log.d(TAG, "Constructor()");
        String nameDataBase = ConstantsSQLLiteDB.NAME_DATABASE;
        this.helperSQLite = new WordSQLiteHelper(context, nameDataBase, null, 1);
    }

    public SQLiteDatabase openToRead() {
        return this.helperSQLite.getReadableDatabase();
    }

    public SQLiteDatabase openToWrite() {
        return this.helperSQLite.getWritableDatabase();
    }

    public void insertOrIgnore(ContentValues values) {
        Log.d(TAG, "insertOrIgnore()");
        SQLiteDatabase dataBase = this.openToWrite();
        try {
            String nameTable = ConstantsSQLLiteDB.TABLE_WORD;
            dataBase.insertWithOnConflict(nameTable, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            Log.d(TAG, "Insert word is success");
        } finally {
            Log.d(TAG, "Closing database...");
            dataBase.close();
        }
    }

    public ArrayList<Word> selectWords() {
        Log.d(TAG, "selectWords()");

        ArrayList<Word> words = null;
        Date creationDateWord = null;
        Time creationTimeWord = null;
        Date modificationDateWord = null;
        Time modificationTimeWord = null;

        SQLiteDatabase dataBase = this.openToRead();

        try {

            String nameTable = ConstantsSQLLiteDB.TABLE_WORD;
            Cursor cursor = dataBase.query(nameTable, null, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                words = new ArrayList<Word>();
                while (cursor.isAfterLast() == false) {

                    String idWord = cursor.getString(0);
                    String creationDateStr = cursor.getString(1);
                    String creationTimeStr = cursor.getString(2);
                    String modificationDateStr = cursor.getString(3);
                    String modificationTimeStr = cursor.getString(4);
                    String statusWord = cursor.getString(5);
                    String textWord = cursor.getString(6);


                    creationDateWord = GeneralUtils.convertStringToDateSQL(creationDateStr, ConstantsFormat.FORMAT_DATE_1);
                    creationTimeWord = GeneralUtils.convertStringToTimeSQL(creationTimeStr);
                    modificationDateWord = GeneralUtils.convertStringToDateSQL(modificationDateStr, ConstantsFormat.FORMAT_DATE_1);
                    modificationTimeWord = GeneralUtils.convertStringToTimeSQL(modificationTimeStr);


                    Word word = new Word();
                    word.setId(idWord);
                    word.setCreationDate(creationDateWord);
                    word.setCreationTime(creationTimeWord);
                    word.setModificationDate(modificationDateWord);
                    word.setModificationTime(modificationTimeWord);
                    word.setStatus(statusWord);
                    word.setText(textWord);

                    words.add(word);

                    cursor.moveToNext();
                }

                Log.d(TAG, "Select words is success");
            }

        } catch (ParseException e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            Log.d(TAG, "Closing database...");
            dataBase.close();
        }


        return words;
    }
}
