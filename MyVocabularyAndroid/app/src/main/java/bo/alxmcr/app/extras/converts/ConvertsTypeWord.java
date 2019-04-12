package bo.alxmcr.app.extras.converts;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import bo.alxmcr.app.constants.ConstantsFormat;
import bo.alxmcr.app.models.Meaning;
import bo.alxmcr.app.models.Word;
import bo.alxmcr.utils.general.GeneralUtils;

/**
 * Created by JoseCoca-i7 on 22/04/2015.
 */
public class ConvertsTypeWord {

    public static final String TAG = ConvertsTypeWord.class.getSimpleName();

    public static ArrayList<Word> convertJSONArrayToArrayListWordWithOutMeanings(JSONArray jsonArray) throws JSONException, ParseException {
        ArrayList<Word> words = null;

        if (jsonArray != null) {
            int lengthJSONArray = jsonArray.length();

            if (lengthJSONArray > 0) {
                words = new ArrayList<Word>();
            }

            for (int i = 0; i < lengthJSONArray; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Word wordi = ConvertsTypeWord.convertJSONObjectToWordWithOutMeanings(jsonObject);
                words.add(wordi);
            }
        }

        return words;
    }

    public static Word convertJSONObjectToWordWithOutMeanings(JSONObject jsonObject) throws JSONException, ParseException {
        Word word = null;

        if (jsonObject != null) {
            String idWord = jsonObject.getString("id");
            String creationDateStr = jsonObject.getString("creationDate");
            String creationTimeStr = jsonObject.getString("creationTime");
            String modificationDateStr = jsonObject.getString("modificationDate");
            String modificationTimeStr = jsonObject.getString("modificationTime");
            String statusWord = jsonObject.getString("status");
            String textWord = jsonObject.getString("text");

            Date creationDateWord = GeneralUtils.convertStringToDateSQL(creationDateStr, ConstantsFormat.FORMAT_DATE_1);
            Time creationTimeWord = GeneralUtils.convertStringToTimeSQL(creationTimeStr);
            Date modificationDateWord = GeneralUtils.convertStringToDateSQL(modificationDateStr, ConstantsFormat.FORMAT_DATE_1);
            Time modificationTimeWord = GeneralUtils.convertStringToTimeSQL(modificationTimeStr);

            word = new Word();
            word.setId(idWord);
            word.setCreationDate(creationDateWord);
            word.setCreationTime(creationTimeWord);
            word.setModificationDate(modificationDateWord);
            word.setModificationTime(modificationTimeWord);
            word.setStatus(statusWord);
            word.setText(textWord);

        } else {
            Log.e(TAG, "JSONObject is null");
        }

        return word;
    }


    public static String convertArrayListWordToStringJSON(ArrayList<Word> wordsSearchWord) throws JSONException, ParseException {

        String listWordStr = null;
        JSONArray arrayWord = null;

        if (wordsSearchWord != null) {
            arrayWord = new JSONArray();
            for (Word word : wordsSearchWord) {
                JSONObject wordJSON = ConvertsTypeWord.convertWordWithOutMeaningsToJSONObject(word);
                arrayWord.put(wordJSON);
            }
        } else {
            Log.e(TAG, "Error. wordsSearchWord is null");
        }

        if (arrayWord != null) {
            listWordStr = arrayWord.toString();
        } else {
            Log.e(TAG, "arrayWord is null");
        }


        return listWordStr;
    }

    public static JSONObject convertWordWithOutMeaningsToJSONObject(Word word) throws JSONException, ParseException {
        JSONObject jsonObjectWord = null;
        String jsonStr = null;
        String id = null;
        Date creationDate = null;
        Time creationTime = null;
        Date modificationDate = null;
        Time modificationTime = null;
        String creationDateStr = null;
        String creationTimeStr = null;
        String modificationDateStr = null;
        String modificationTimeStr = null;
        String status = null;
        String text = null;

        if (word != null) {
            id = word.getId();
            creationDate = word.getCreationDate();
            creationTime = word.getCreationTime();
            modificationDate = word.getModificationDate();
            modificationTime = word.getModificationTime();
            status = word.getStatus();
            text = word.getText();

            //Converts
            creationDateStr = GeneralUtils.convertDateSQLToString(creationDate, ConstantsFormat.FORMAT_DATE_1);
            creationTimeStr = GeneralUtils.convertTimeSQLToString(creationTime, ConstantsFormat.FORMAT_TIME_1);
            modificationDateStr = GeneralUtils.convertDateSQLToString(modificationDate, ConstantsFormat.FORMAT_DATE_1);
            modificationTimeStr = GeneralUtils.convertTimeSQLToString(creationTime, ConstantsFormat.FORMAT_TIME_1);

            jsonObjectWord = new JSONObject();
            jsonObjectWord.put("id", id);
            jsonObjectWord.put("creationDate", creationDateStr);
            jsonObjectWord.put("creationTime", creationTimeStr);
            jsonObjectWord.put("modificationDate", modificationDateStr);
            jsonObjectWord.put("modificationTime", modificationTimeStr);
            jsonObjectWord.put("status", status);
            jsonObjectWord.put("text", text);
        }else{
            Log.e(TAG, "Error. Word is null");
        }

        return jsonObjectWord;
    }

    public static JSONObject convertWordWithMeaningsToJSONObject(Word word) throws ParseException, JSONException {
        JSONObject jsonObjectWord = null;
        String jsonStr = null;
        String id = null;
        Date creationDate = null;
        Time creationTime = null;
        Date modificationDate = null;
        Time modificationTime = null;
        String creationDateStr = null;
        String creationTimeStr = null;
        String modificationDateStr = null;
        String modificationTimeStr = null;
        String status = null;
        String text = null;
        List<Meaning> meanings = null;
        JSONArray meaningsToJSONArray = null;
        String meaningsStr = null;

        if (word != null) {
            id = word.getId();
            creationDate = word.getCreationDate();
            creationTime = word.getCreationTime();
            modificationDate = word.getModificationDate();
            modificationTime = word.getModificationTime();
            status = word.getStatus();
            text = word.getText();
            meanings = word.getMeanings();

            //Converts
            creationDateStr = GeneralUtils.convertDateSQLToString(creationDate, ConstantsFormat.FORMAT_DATE_1);
            creationTimeStr = GeneralUtils.convertTimeSQLToString(creationTime, ConstantsFormat.FORMAT_TIME_1);
            modificationDateStr = GeneralUtils.convertDateSQLToString(modificationDate, ConstantsFormat.FORMAT_DATE_1);
            modificationTimeStr = GeneralUtils.convertTimeSQLToString(creationTime, ConstantsFormat.FORMAT_TIME_1);

        }

        if (meanings != null) {
            meaningsToJSONArray = ConvertsTypeMeaning.convertListMeaningsToJSONArray(meanings);
        } else {
            Log.e(TAG, "Error. List meanings is null");
        }

        if (meaningsToJSONArray != null) {
            meaningsStr = meaningsToJSONArray.toString();
        } else {
            meaningsStr = "[]";
        }

        jsonObjectWord = new JSONObject();
        jsonObjectWord.put("id", id);
        jsonObjectWord.put("creationDate", creationDateStr);
        jsonObjectWord.put("creationTime", creationTimeStr);
        jsonObjectWord.put("modificationDate", modificationDateStr);
        jsonObjectWord.put("modificationTime", modificationTimeStr);
        jsonObjectWord.put("status", status);
        jsonObjectWord.put("text", text);
        jsonObjectWord.put("meanings", meaningsStr);

        return jsonObjectWord;
    }

}
