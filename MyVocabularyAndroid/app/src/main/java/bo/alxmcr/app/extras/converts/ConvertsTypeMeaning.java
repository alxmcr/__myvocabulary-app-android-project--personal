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
 * Created by Jose Coca on 22/04/2015.
 */
public class ConvertsTypeMeaning {

    public static final String TAG = "ConvertsTypeMeaning.";

    public static Word convertJSONObjectToWordWithMeanings(JSONObject wordJsonObject) throws JSONException, ParseException {
        Word word = null;
        JSONArray arrayMeaningJSON = null;
        List<Meaning> meanings = null;

        if (wordJsonObject != null) {
            Log.v(TAG, "wordJsonObject->" + wordJsonObject.toString());
            String idWord = wordJsonObject.getString("id");
            String creationDateStr = wordJsonObject.getString("creationDate");
            String creationTimeStr = wordJsonObject.getString("creationTime");
            String modificationDateStr = wordJsonObject.getString("modificationDate");
            String modificationTimeStr = wordJsonObject.getString("modificationTime");
            String statusWord = wordJsonObject.getString("status");
            String textWord = wordJsonObject.getString("text");


            Date creationDateWord = GeneralUtils.convertStringToDateSQL(creationDateStr, ConstantsFormat.FORMAT_DATE_1);
            Time creationTimeWord = GeneralUtils.convertStringToTimeSQL(creationTimeStr);
            Date modificationDateWord = GeneralUtils.convertStringToDateSQL(modificationDateStr, ConstantsFormat.FORMAT_DATE_1);
            Time modificationTimeWord = GeneralUtils.convertStringToTimeSQL(modificationTimeStr);

            //Meanings
            String meaningsWordStr = wordJsonObject.getString("meanings");

            if (meaningsWordStr.equals("null")) {
                meanings = new ArrayList<Meaning>();
            } else if (meaningsWordStr.isEmpty()) {
                meanings = new ArrayList<Meaning>();
            } else if (meaningsWordStr == null) {
                meanings = new ArrayList<Meaning>();
            } else if (meaningsWordStr.equals("[]")) {
                meanings = new ArrayList<Meaning>();
            } else {
                arrayMeaningJSON = new JSONArray(meaningsWordStr);
                meanings = ConvertsTypeMeaning.convertJSONArrayToArrayListMeanings(arrayMeaningJSON);
            }

            word = new Word();
            word.setId(idWord);
            word.setCreationDate(creationDateWord);
            word.setCreationTime(creationTimeWord);
            word.setModificationDate(modificationDateWord);
            word.setModificationTime(modificationTimeWord);
            word.setStatus(statusWord);
            word.setText(textWord);
            word.setMeanings(meanings);

        } else {
            Log.e(TAG, "JSONObject is null");
        }

        return word;
    }

    public static List<Meaning> convertJSONArrayToArrayListMeanings(JSONArray arrayMeaningJSON) throws JSONException, ParseException {
        ArrayList<Meaning> meanings = null;

        if (arrayMeaningJSON != null) {
            int lengthJSONArray = arrayMeaningJSON.length();

            if (lengthJSONArray > 0) {
                meanings = new ArrayList<Meaning>();
            }

            for (int i = 0; i < lengthJSONArray; i++) {
                JSONObject jsonObject = arrayMeaningJSON.getJSONObject(i);

                Meaning meaningi = ConvertsTypeMeaning.convertJSONObjectToMeaningWithOutWord(jsonObject);
                meanings.add(meaningi);
            }
        }

        return meanings;
    }

    public static Meaning convertJSONObjectToMeaningWithOutWord(JSONObject jsonObject) throws JSONException, ParseException {
        Meaning meaning = null;

        if (jsonObject != null) {
            String idMeaning = jsonObject.getString("id");
            String creationDateStr = jsonObject.getString("creationDate");
            String creationTimeStr = jsonObject.getString("creationTime");
            String modificationDateStr = jsonObject.getString("modificationDate");
            String modificationTimeStr = jsonObject.getString("modificationTime");
            String statusMeaning = jsonObject.getString("status");
            String textMeaning = jsonObject.getString("text");

            Date creationDateMeaning = GeneralUtils.convertStringToDateSQL(creationDateStr, ConstantsFormat.FORMAT_DATE_1);
            Time creationTimeMeaning = GeneralUtils.convertStringToTimeSQL(creationTimeStr);
            Date modificationDateMeaning = GeneralUtils.convertStringToDateSQL(modificationDateStr, ConstantsFormat.FORMAT_DATE_1);
            Time modificationTimeMeaning = GeneralUtils.convertStringToTimeSQL(modificationTimeStr);

            meaning = new Meaning();
            meaning.setId(idMeaning);
            meaning.setCreationDate(creationDateMeaning);
            meaning.setCreationTime(creationTimeMeaning);
            meaning.setModificationDate(modificationDateMeaning);
            meaning.setModificationTime(modificationTimeMeaning);
            meaning.setStatus(statusMeaning);
            meaning.setText(textMeaning);

        } else {
            Log.e(TAG, "JSONObject is null");
        }

        return meaning;
    }

    public static Word convertJSONObjectToWordWithOutMeanings(JSONObject wordJsonObject) throws JSONException, ParseException {
        Word word = null;

        if (wordJsonObject != null) {

            String idWord = wordJsonObject.getString("id");
            String creationDateStr = wordJsonObject.getString("creationDate");
            String creationTimeStr = wordJsonObject.getString("creationTime");
            String modificationDateStr = wordJsonObject.getString("modificationDate");
            String modificationTimeStr = wordJsonObject.getString("modificationTime");
            String statusWord = wordJsonObject.getString("status");
            String textWord = wordJsonObject.getString("text");


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

    public static JSONArray convertListMeaningsToJSONArray(List<Meaning> meanings) throws JSONException, ParseException {

        JSONArray meaningsArrayJson = null;

        if (meanings != null) {
            meaningsArrayJson = new JSONArray();
            for (Meaning meaning : meanings) {
                JSONObject meaningToJSONObject = ConvertsTypeMeaning.convertMeaningWithWordToJSONObject(meaning);
                meaningsArrayJson.put(meaningToJSONObject);
            }
        } else {
            Log.e(TAG, "List meanings is null");
        }

        return meaningsArrayJson;
    }

    public static JSONObject convertMeaningWithWordToJSONObject(Meaning meaning) throws JSONException, ParseException {
        JSONObject meaningToJSONObject = null;
        JSONObject jsonObjectWord = null;


        String jsonStr = null;
        String id = null;
        Date creationDate = null;
        Time creationTime = null;
        Date modificationDate = null;
        Time modificationTime = null;
        String status = null;
        String text = null;
        String creationDateStr = null;
        String creationTimeStr = null;
        String modificationDateStr = null;
        String modificationTimeStr = null;
        Word word = null;
        String idWord = null;
        String wordStr = null;

        if (meaning != null) {
            id = meaning.getId();
            creationDate = meaning.getCreationDate();
            creationTime = meaning.getCreationTime();
            modificationDate = meaning.getModificationDate();
            modificationTime = meaning.getModificationTime();
            status = meaning.getStatus();
            text = meaning.getText();
            word = meaning.getWord();

            //Converts
            creationDateStr = GeneralUtils.convertDateSQLToString(creationDate, ConstantsFormat.FORMAT_DATE_1);
            creationTimeStr = GeneralUtils.convertTimeSQLToString(creationTime, ConstantsFormat.FORMAT_TIME_1);
            modificationDateStr = GeneralUtils.convertDateSQLToString(modificationDate, ConstantsFormat.FORMAT_DATE_1);
            modificationTimeStr = GeneralUtils.convertTimeSQLToString(creationTime, ConstantsFormat.FORMAT_TIME_1);

        } else {
            Log.e(TAG, "Meaning is null");
        }

        if (word != null) {
            idWord = word.getId();
            jsonObjectWord = ConvertsTypeWord.convertWordWithOutMeaningsToJSONObject(word);
        } else {
            Log.e(TAG, "Word is null");
        }

        meaningToJSONObject = new JSONObject();

        meaningToJSONObject.put("id", id);
        meaningToJSONObject.put("creationDate", creationDateStr);
        meaningToJSONObject.put("creationTime", creationTimeStr);
        meaningToJSONObject.put("modificationDate", modificationDateStr);
        meaningToJSONObject.put("modificationTime", modificationTimeStr);
        meaningToJSONObject.put("status", status);
        meaningToJSONObject.put("text", text);
        meaningToJSONObject.put("word", jsonObjectWord);

        return meaningToJSONObject;
    }

    public static ArrayList<Meaning> convertJSONArrayToArrayListMeaningWithOutWord(JSONArray meaningsArray) throws JSONException, ParseException {
        ArrayList<Meaning> meanings = null;

        if (meaningsArray != null) {
            int lengthJSONArray = meaningsArray.length();

            if (lengthJSONArray > 0) {
                meanings = new ArrayList<Meaning>();
            }

            for (int i = 0; i < lengthJSONArray; i++) {
                JSONObject jsonObject = meaningsArray.getJSONObject(i);

                Meaning meaningi = ConvertsTypeMeaning.convertJSONObjectToMeaningWithOutWord(jsonObject);
                meanings.add(meaningi);
            }
        }

        return meanings;
    }

    public static String convertArrayListMeaningToStringJSON(ArrayList<Meaning> meaningsSearchMeaning) throws ParseException, JSONException {
        String listMeaningStr = null;
        JSONArray arrayMeaning = null;

        if (meaningsSearchMeaning != null) {
            arrayMeaning = new JSONArray();
            for (Meaning meaning : meaningsSearchMeaning) {
                JSONObject meaningJSON = ConvertsTypeMeaning.convertMeaningWithOutWordToJSONObject(meaning);
                arrayMeaning.put(meaningJSON);
            }
        } else {
            Log.e("Error.", "meaningsSearchMeaning is null");
        }

        if (arrayMeaning != null) {
            listMeaningStr = arrayMeaning.toString();
        } else {
            Log.e(TAG, "arrayMeaning is null");
        }

        return listMeaningStr;

    }

    private static JSONObject convertMeaningWithOutWordToJSONObject(Meaning meaning) throws ParseException, JSONException {
        JSONObject jsonObjectMeaning = null;
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

        if (meaning != null) {
            id = meaning.getId();
            creationDate = meaning.getCreationDate();
            creationTime = meaning.getCreationTime();
            modificationDate = meaning.getModificationDate();
            modificationTime = meaning.getModificationTime();
            status = meaning.getStatus();
            text = meaning.getText();

            //Converts
            creationDateStr = GeneralUtils.convertDateSQLToString(creationDate, ConstantsFormat.FORMAT_DATE_1);
            creationTimeStr = GeneralUtils.convertTimeSQLToString(creationTime, ConstantsFormat.FORMAT_TIME_1);
            modificationDateStr = GeneralUtils.convertDateSQLToString(modificationDate, ConstantsFormat.FORMAT_DATE_1);
            modificationTimeStr = GeneralUtils.convertTimeSQLToString(creationTime, ConstantsFormat.FORMAT_TIME_1);

        }

        jsonObjectMeaning = new JSONObject();
        jsonObjectMeaning.put("id", id);
        jsonObjectMeaning.put("creationDate", creationDateStr);
        jsonObjectMeaning.put("creationTime", creationTimeStr);
        jsonObjectMeaning.put("modificationDate", modificationDateStr);
        jsonObjectMeaning.put("modificationTime", modificationTimeStr);
        jsonObjectMeaning.put("status", status);
        jsonObjectMeaning.put("text", text);

        return jsonObjectMeaning;
    }

    public static Meaning convertJSONObjectToMeaningWithWord(JSONObject jsonObject) throws JSONException, ParseException {
        Meaning meaning = null;
        Word word = null;
        String wordStr = null;
        String idMeaning = null;
        String statusMeaning = null;
        String textMeaning = null;
        Date creationDateMeaning = null;
        Time creationTimeMeaning = null;
        Date modificationDateMeaning = null;
        Time modificationTimeMeaning = null;

        if (jsonObject != null) {
            Log.v(TAG, jsonObject.toString());

            idMeaning = jsonObject.getString("id");
            String creationDateStr = jsonObject.getString("creationDate");
            String creationTimeStr = jsonObject.getString("creationTime");
            String modificationDateStr = jsonObject.getString("modificationDate");
            String modificationTimeStr = jsonObject.getString("modificationTime");
            statusMeaning = jsonObject.getString("status");
            textMeaning = jsonObject.getString("text");
            wordStr = jsonObject.getString("word");


            creationDateMeaning = GeneralUtils.convertStringToDateSQL(creationDateStr, ConstantsFormat.FORMAT_DATE_1);
            creationTimeMeaning = GeneralUtils.convertStringToTimeSQL(creationTimeStr);
            modificationDateMeaning = GeneralUtils.convertStringToDateSQL(modificationDateStr, ConstantsFormat.FORMAT_DATE_1);
            modificationTimeMeaning = GeneralUtils.convertStringToTimeSQL(modificationTimeStr);

        } else {
            Log.e(TAG, "JSONObject is null");
        }

        if (wordStr != null) {
            JSONObject wordObjectJson = new JSONObject(wordStr);
            word = ConvertsTypeWord.convertJSONObjectToWordWithOutMeanings(wordObjectJson);
        } else {
            Log.e(TAG, "String is null");
        }

        meaning = new Meaning();
        meaning.setId(idMeaning);
        meaning.setCreationDate(creationDateMeaning);
        meaning.setCreationTime(creationTimeMeaning);
        meaning.setModificationDate(modificationDateMeaning);
        meaning.setModificationTime(modificationTimeMeaning);
        meaning.setStatus(statusMeaning);
        meaning.setText(textMeaning);
        meaning.setWord(word);

        return meaning;
    }

}
