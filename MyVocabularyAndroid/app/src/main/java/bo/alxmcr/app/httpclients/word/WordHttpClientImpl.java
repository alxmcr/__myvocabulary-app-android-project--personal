package bo.alxmcr.app.httpclients.word;

import android.content.res.Resources;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import alxmcr.bo.vocabularyapp_v8.R;
import bo.alxmcr.app.constants.ConstantsMediaTypes;
import bo.alxmcr.app.constants.ConstantsMessages;
import bo.alxmcr.app.constants.ConstantsURLs;
import bo.alxmcr.app.extras.converts.ConvertsTypeMeaning;
import bo.alxmcr.app.extras.converts.ConvertsTypeWord;
import bo.alxmcr.app.extras.exceptions.connectivity.AppServerNotAvailableException;
import bo.alxmcr.app.models.Word;
import bo.alxmcr.utils.general.GeneralUtils;
import bo.alxmcr.utils.httpclient.MethodsHttpUtilsImpl;

/**
 * Created by Jose Coca on 21/04/2015.
 */
public class WordHttpClientImpl implements WordHttpClient {

    public static final String TAG = WordHttpClientImpl.class.getSimpleName();

    public static final String URL_API_HTTP_CLIENT_WORDS = "words";
    public static final String URL_API_ACTION_SEARCH = "search";

    private MethodsHttpUtilsImpl methodsHttpUtilsImpl;
    private String domainApp;

    public WordHttpClientImpl(String domainApp) {
        this.methodsHttpUtilsImpl = new MethodsHttpUtilsImpl();
        this.domainApp = domainApp;
    }


    @Override
    public ArrayList<Word> readWords(Resources resources) {
        Log.v(TAG, "Start readWords");
        HttpResponse response = null;
        String respStr = null;
        JSONArray arrayJson = null;
        ArrayList<Word> words = null;

        String url = GeneralUtils.getURLRestBasic(this.domainApp, ConstantsURLs.KEYWORD_API_PATH, URL_API_HTTP_CLIENT_WORDS);
        String mediaType = ConstantsMediaTypes.APPLICATION_JSON;

        try {

            if (this.methodsHttpUtilsImpl != null) {
                response = this.methodsHttpUtilsImpl.doGet(url, mediaType);
            }

            if (response != null) {
                respStr = GeneralUtils.convertHttpResponseToString(response);
                Log.v(TAG, "Server: respStr->" + respStr);
            }

            if (respStr != null && !ConstantsMessages.ERROR_API_REST.equals(respStr)) {
                arrayJson = new JSONArray(respStr);
            } else {
                final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
                String msg = TAG + resources.getString(R.string.msg_error_string_is_null);
                Log.e(typeMessage, msg);
            }

            if (arrayJson != null) {
                words = ConvertsTypeWord.convertJSONArrayToArrayListWordWithOutMeanings(arrayJson);
            } else {
                final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
                String msg = TAG + resources.getString(R.string.msg_error_json_array_is_null);
                Log.e(typeMessage, msg);
            }

        } catch (IOException e) {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            final String msg = e.getMessage();
            Log.e(TAG, typeMessage + msg, e);
        } catch (JSONException e) {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            final String msg = e.getMessage();
            Log.e(TAG, typeMessage + msg, e);
        } catch (ParseException e) {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            final String msg = e.getMessage();
            Log.e(TAG, typeMessage + msg, e);
        }
        Log.v(TAG, "End readWords");
        return words;
    }

    @Override
    public boolean createWord(Resources resources, Word word) throws AppServerNotAvailableException {
        boolean result = false;

        Log.v(TAG, "Start createWord");
        HttpResponse response = null;
        String respStr = null;
        String dataJson = null;
        JSONObject jsonObject = null;

        String url = GeneralUtils.getURLRestBasic(this.domainApp, ConstantsURLs.KEYWORD_API_PATH, URL_API_HTTP_CLIENT_WORDS);
        String mediaType = ConstantsMediaTypes.APPLICATION_JSON;

        try {

            JSONObject wordJsonObject = ConvertsTypeWord.convertWordWithOutMeaningsToJSONObject(word);

            if (wordJsonObject != null) {
                dataJson = wordJsonObject.toString();
            }

            if (this.methodsHttpUtilsImpl != null) {
                response = this.methodsHttpUtilsImpl.doPost(url, mediaType, dataJson);
            }

            if (response != null) {
                respStr = GeneralUtils.convertHttpResponseToString(response);
                Log.v(TAG, "Server: respStr->" + respStr);
            }

            if (respStr != null && !ConstantsMessages.ERROR_API_REST.equals(respStr)) {
                jsonObject = new JSONObject(respStr);
            } else {
                final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
                String msg = TAG + resources.getString(R.string.msg_error_string_is_null);
                Log.e(typeMessage, msg);
            }

            if (jsonObject != null) {
                result = true;
            }

        } catch (IOException e) {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            final String msg = TAG + e.getMessage();
            Log.e(typeMessage, msg);
        } catch (JSONException e) {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            final String msg = TAG + e.getMessage();
            Log.e(typeMessage, msg);
        } catch (ParseException e) {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            final String msg = TAG + e.getMessage();
            Log.e(typeMessage, msg);
        }
        Log.v(TAG, "End createWord");

        return result;
    }

    @Override
    public ArrayList<Word> searchWordByText(Resources resources, String textWord) {
        Log.v(TAG, "Start searchWordByText");
        HttpResponse response = null;
        String respStr = null;
        JSONArray arrayJson = null;
        ArrayList<Word> wordsSearched = null;


        if (textWord != null) {
            textWord = textWord.toLowerCase();
            textWord = textWord.trim();
        }

        String url = GeneralUtils.getURLRestWithActionPlusValue(this.domainApp, ConstantsURLs.KEYWORD_API_PATH, URL_API_HTTP_CLIENT_WORDS, URL_API_ACTION_SEARCH, textWord);
        String mediaType = ConstantsMediaTypes.APPLICATION_JSON;

        try {

            if (this.methodsHttpUtilsImpl != null) {
                response = this.methodsHttpUtilsImpl.doGet(url, mediaType);
            }

            if (response != null) {
                respStr = GeneralUtils.convertHttpResponseToString(response);
                Log.v(TAG, "Server: respStr->" + respStr);
            }

            if (respStr != null && !ConstantsMessages.ERROR_API_REST.equals(respStr)) {
                arrayJson = new JSONArray(respStr);
            } else {
                final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
                String msg = TAG + resources.getString(R.string.msg_error_string_is_null);
                Log.e(typeMessage, msg);
            }

            if (arrayJson != null) {
                wordsSearched = ConvertsTypeWord.convertJSONArrayToArrayListWordWithOutMeanings(arrayJson);
            } else {
                final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
                String msg = TAG + resources.getString(R.string.msg_error_json_array_is_null);
                Log.e(typeMessage, msg);
            }

        } catch (IOException e) {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            final String msg = TAG + e.getMessage();
            Log.e(typeMessage, msg);
        } catch (JSONException e) {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            final String msg = TAG + e.getMessage();
            Log.e(typeMessage, msg);
        } catch (ParseException e) {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            final String msg = TAG + e.getMessage();
            Log.e(typeMessage, msg);
        }
        Log.v(TAG, "End searchWordByText");
        return wordsSearched;
    }

    @Override
    public Word searchWordById(Resources resources, String idWord) {
        Log.v(TAG, "Start searchWordById");
        HttpResponse response = null;
        String respStr = null;
        JSONObject jsonObject = null;
        Word wordsSearched = null;


        if (idWord != null) {
            Log.v(TAG, "idWord->" + idWord);
        } else {
            Log.e(TAG, "idWord is null");
        }

        String url = GeneralUtils.getURLRestWithValue(this.domainApp, ConstantsURLs.KEYWORD_API_PATH, URL_API_HTTP_CLIENT_WORDS, idWord);
        String mediaType = ConstantsMediaTypes.APPLICATION_JSON;

        try {

            if (this.methodsHttpUtilsImpl != null) {
                response = this.methodsHttpUtilsImpl.doGet(url, mediaType);
            }

            if (response != null) {
                respStr = GeneralUtils.convertHttpResponseToString(response);
                Log.v(TAG, "Server: respStr->" + respStr);
            }

            if (respStr != null && !ConstantsMessages.ERROR_API_REST.equals(respStr)) {
                jsonObject = new JSONObject(respStr);
            } else {
                final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
                String msg = TAG + resources.getString(R.string.msg_error_string_is_null);
                Log.e(typeMessage, msg);
            }

            if (jsonObject != null) {
                wordsSearched = ConvertsTypeMeaning.convertJSONObjectToWordWithMeanings(jsonObject);
            } else {
                final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
                String msg = TAG + resources.getString(R.string.msg_error_json_array_is_null);
                Log.e(typeMessage, msg);
            }

        } catch (IOException e) {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            final String msg = typeMessage + e.getMessage();
            Log.e(TAG, msg, e);
        } catch (JSONException e) {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            final String msg = typeMessage + e.getMessage();
            Log.e(TAG, msg, e);
        } catch (ParseException e) {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            final String msg = typeMessage + e.getMessage();
            Log.e(TAG, msg, e);
        }
        Log.v(TAG, "End searchWordById");
        return wordsSearched;
    }

    @Override
    public boolean deleteWordById(Resources resources, String idWord) throws AppServerNotAvailableException {
        Log.v(TAG, "Start deleteWordById");
        HttpResponse response = null;
        String respStr = null;
        boolean result = false;
        JSONObject jsonObject = null;

        String url = GeneralUtils.getURLRestWithValue(this.domainApp, ConstantsURLs.KEYWORD_API_PATH, URL_API_HTTP_CLIENT_WORDS, idWord);
        String mediaType = ConstantsMediaTypes.APPLICATION_JSON;

        try {

            if (this.methodsHttpUtilsImpl != null) {
                response = this.methodsHttpUtilsImpl.doDelete(url, mediaType);
            }

            if (response != null) {
                respStr = GeneralUtils.convertHttpResponseToString(response);
                Log.v(TAG, "Server: respStr->" + respStr);
            }

            if (respStr != null && !ConstantsMessages.ERROR_API_REST.equals(respStr)) {
                jsonObject = new JSONObject(respStr);
            } else {
                final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
                String msg = TAG + resources.getString(R.string.msg_error_string_is_null);
                Log.e(typeMessage, msg);
            }

            if (jsonObject != null) {
                result = true;
            }

        } catch (IOException e) {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            final String msg = TAG + e.getMessage();
            Log.e(typeMessage, msg, e);
        } catch (AppServerNotAvailableException e) {
            throw e;
        } catch (JSONException e) {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            final String msg = TAG + e.getMessage();
            Log.e(typeMessage, msg, e);
        }
        Log.v(TAG, "End deleteWordById");

        return result;
    }

    @Override
    public boolean updateWord(Resources resources, Word word) throws AppServerNotAvailableException {
        boolean result = false;

        Log.v(TAG, "Start updateWord");
        HttpResponse response = null;
        String respStr = null;
        String dataJson = null;
        JSONObject jsonObject = null;

        String idWord = null;
        if (word != null) {
            idWord = word.getId();
        }

        String url = GeneralUtils.getURLRestWithValue(this.domainApp, ConstantsURLs.KEYWORD_API_PATH, URL_API_HTTP_CLIENT_WORDS, idWord);
        String mediaType = ConstantsMediaTypes.APPLICATION_JSON;

        try {

            JSONObject wordJsonObject = ConvertsTypeWord.convertWordWithOutMeaningsToJSONObject(word);

            if (wordJsonObject != null) {
                dataJson = wordJsonObject.toString();
            }

            if (this.methodsHttpUtilsImpl != null) {
                response = this.methodsHttpUtilsImpl.doPut(url, mediaType, dataJson);
            }

            if (response != null) {
                respStr = GeneralUtils.convertHttpResponseToString(response);
                Log.v(TAG, "Server: respStr->" + respStr);
            }

            if (respStr != null && !ConstantsMessages.ERROR_API_REST.equals(respStr)) {
                jsonObject = new JSONObject(respStr);
            } else {
                final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
                String msg = TAG + resources.getString(R.string.msg_error_string_is_null);
                Log.e(typeMessage, msg);
            }

            if (jsonObject != null) {
                result = true;
            }

        } catch (IOException e) {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            final String msg = TAG + e.getMessage();
            Log.e(typeMessage, msg);
        } catch (JSONException e) {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            final String msg = TAG + e.getMessage();
            Log.e(typeMessage, msg);
        } catch (ParseException e) {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            final String msg = TAG + e.getMessage();
            Log.e(typeMessage, msg);
        }
        Log.v(TAG, "End updateWord");

        return result;
    }


}
