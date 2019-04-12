package bo.alxmcr.app.httpclients.meaning;

import android.content.res.Resources;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;

import alxmcr.bo.vocabularyapp_v8.R;
import bo.alxmcr.app.constants.ConstantsMediaTypes;
import bo.alxmcr.app.constants.ConstantsMessages;
import bo.alxmcr.app.constants.ConstantsURLs;
import bo.alxmcr.app.extras.converts.ConvertsTypeMeaning;
import bo.alxmcr.app.extras.converts.ConvertsTypeWord;
import bo.alxmcr.app.extras.exceptions.connectivity.AppServerNotAvailableException;
import bo.alxmcr.app.models.Meaning;
import bo.alxmcr.app.models.Word;
import bo.alxmcr.utils.general.GeneralUtils;
import bo.alxmcr.utils.httpclient.MethodsHttpUtilsImpl;

/**
 * Created by JoseCoca-i7 on 16/05/2015.
 */
public class MeaningHttpClientImpl implements MeaningHttpClient {

    public static final String TAG = MeaningHttpClientImpl.class.getSimpleName();

    public static final String URL_API_HTTP_CLIENT_WORDS = "meanings";
    public static final String URL_API_ACTION_SEARCH = "search";

    private MethodsHttpUtilsImpl methodsHttpUtilsImpl;
    private String domainApp;

    public MeaningHttpClientImpl(String domainApp) {
        this.methodsHttpUtilsImpl = new MethodsHttpUtilsImpl();
        this.domainApp = domainApp;
    }

    @Override
    public boolean createMeaning(Resources resources, Meaning meaning) throws AppServerNotAvailableException {
        boolean result = false;

        Log.v(TAG, "Start createMeaning");
        HttpResponse response = null;
        String respStr = null;
        String dataJson = null;
        JSONObject jsonObject = null;

        String url = GeneralUtils.getURLRestBasic(this.domainApp, ConstantsURLs.KEYWORD_API_PATH, URL_API_HTTP_CLIENT_WORDS);
        String mediaType = ConstantsMediaTypes.APPLICATION_JSON;

        try {

            JSONObject wordJsonObject = ConvertsTypeMeaning.convertMeaningWithWordToJSONObject(meaning);

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
        Log.v(TAG, "End createMeaning");

        return result;
    }

    @Override
    public Meaning searchMeaningById(Resources resources, String idMeaning) {
        Log.v(TAG, "Start searchMeaningById");
        HttpResponse response = null;
        String respStr = null;
        JSONObject jsonObject = null;
        Meaning meaningSearched = null;

        String url = GeneralUtils.getURLRestWithValue(this.domainApp, ConstantsURLs.KEYWORD_API_PATH, URL_API_HTTP_CLIENT_WORDS, idMeaning);
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
                meaningSearched = ConvertsTypeMeaning.convertJSONObjectToMeaningWithOutWord(jsonObject);
            } else {
                final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
                String msg = TAG + resources.getString(R.string.msg_error_json_array_is_null);
                Log.e(typeMessage, msg);
            }

        } catch (IOException e) {
            final String typeMessage = resources.getString(R.string.type_message_produced_error);
            final String msg = typeMessage + e.getMessage();
            Log.e(TAG, msg, e);
        } catch (JSONException e) {
            final String typeMessage = resources.getString(R.string.type_message_produced_error);
            final String msg = typeMessage + e.getMessage();
            Log.e(TAG, msg, e);
        } catch (ParseException e) {
            final String typeMessage = resources.getString(R.string.type_message_produced_error);
            final String msg = typeMessage + e.getMessage();
            Log.e(TAG, msg, e);
        }
        Log.v(TAG, "End searchMeaningById");
        return meaningSearched;
    }

    @Override
    public boolean updateMeaning(Resources resources, Meaning meaning) throws AppServerNotAvailableException {
        boolean result = false;

        Log.v(TAG, "Start updateMeaning");
        HttpResponse response = null;
        String respStr = null;
        String dataJson = null;
        JSONObject jsonObject = null;

        String idMeaning = null;
        if (meaning != null) {
            Log.v(TAG, "NewMeaning->" + meaning);
            idMeaning = meaning.getId();
        }

        String url = GeneralUtils.getURLRestWithValue(this.domainApp, ConstantsURLs.KEYWORD_API_PATH, URL_API_HTTP_CLIENT_WORDS, idMeaning);
        String mediaType = ConstantsMediaTypes.APPLICATION_JSON;

        try {

            JSONObject meaningJsonObject = ConvertsTypeMeaning.convertMeaningWithWordToJSONObject(meaning);

            if (meaningJsonObject != null) {
                dataJson = meaningJsonObject.toString();
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
        Log.v(TAG, "End updateMeaning");

        return result;
    }

    @Override
    public boolean deleteMeaningById(Resources resources, String idMeaning) throws AppServerNotAvailableException {
        Log.v(TAG, "Start deleteMeaningById");
        HttpResponse response = null;
        String respStr = null;
        boolean result = false;
        JSONObject jsonObject = null;

        String url = GeneralUtils.getURLRestWithValue(this.domainApp, ConstantsURLs.KEYWORD_API_PATH, URL_API_HTTP_CLIENT_WORDS, idMeaning);
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
        Log.v(TAG, "End deleteMeaningById");

        return result;
    }
}
