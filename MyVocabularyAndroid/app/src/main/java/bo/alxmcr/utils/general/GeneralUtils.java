package bo.alxmcr.utils.general;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.sql.Time;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import bo.alxmcr.app.constants.ConstantsURLs;

/**
 * Created by Jose Coca on 21/04/2015.
 */
public class GeneralUtils {
    public static final String TAG = "GeneralUtils.";
    //1second = 1000ms
    public static final int TIME_OUT = 20000; //20seconds

    public static boolean isConnectingToInternet(Activity context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    public static String convertHttpResponseToString(HttpResponse response) throws IOException {
        String responseStr = null;
        HttpEntity entityResponse = null;


        if (response == null) {
            final String typeMessage = TAG + "Produced an error";
            final String msg = TAG + "Response is null";
            Log.e(typeMessage, msg);
        } else {
            entityResponse = response.getEntity();
        }

        if (entityResponse == null) {
            final String typeMessage = TAG + "Produced an error";
            final String msg = TAG + "EntityResponse is null";
            Log.e(typeMessage, msg);
        } else {
            responseStr = EntityUtils.toString(response.getEntity());
        }

        //Log.v(TAG, "responseStr=" + responseStr);

        return responseStr;
    }

    public static String generateRandomString(int length) {
        StringBuffer buffer = new StringBuffer();
        String characters = "";

        characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

        int charactersLength = characters.length();

        for (int i = 0; i < length; i++) {
            double index = Math.random() * charactersLength;
            buffer.append(characters.charAt((int) index));
        }

        return buffer.toString();
    }

    public static java.sql.Date convertStringToDateSQL(String dateSQLStr, String formatDate) throws ParseException {
        java.sql.Date dateSQL = null;
        Date dateUtil = null;

        if (dateSQLStr == null) {
            return dateSQL;
        }

        if (formatDate == null) {
            return dateSQL;
        }

        SimpleDateFormat fDate = new SimpleDateFormat(formatDate);
        dateUtil = fDate.parse(dateSQLStr);
        dateSQL = new java.sql.Date(dateUtil.getTime());

        return dateSQL;
    }

    public static String convertDateSQLToString(java.sql.Date dateSQL, String formatDate) throws ParseException {


        if (dateSQL == null) {
            return "";
        }

        if (formatDate == null) {
            return "";
        }

        String dateSQLStr = "";

        if (dateSQLStr != null) {
            SimpleDateFormat fDate = new SimpleDateFormat(formatDate);
            dateSQLStr = fDate.format(dateSQL);
        }

        return dateSQLStr;
    }


    public static Time convertStringToTimeSQL(String timeSQLStr) throws ParseException {
        Time timeSQL = null;

        if (timeSQLStr == null) {
            return timeSQL;
        }

        timeSQL = Time.valueOf(timeSQLStr);

        return timeSQL;
    }

    public static String convertTimeSQLToString(Time timeSQL, String formatTime) throws ParseException {
        String timeSQLStr = "";

        if (timeSQL == null) {
            return timeSQLStr;
        }

        if (formatTime == null) {
            return timeSQLStr;
        }

        SimpleDateFormat fDate = new SimpleDateFormat(formatTime);
        timeSQLStr = fDate.format(timeSQL);

        return timeSQLStr;
    }

    public static String getURLRestBasic(String domainApp, String keywordApiPath, String keywordApiModule) {
        MessageFormat ms = new MessageFormat(ConstantsURLs.FORMAT_URL_API_BASIC);
        String[] configApi = {domainApp,
                keywordApiPath,
                keywordApiModule};
        String url = ms.format(configApi);
        return url;
    }

    public static String getURLRestWithValue(String domainApp, String keywordApiPath, String keywordApiModule, String value) {
        MessageFormat ms = new MessageFormat(ConstantsURLs.FORMAT_URL_API_VALUE);
        String[] configApi = {domainApp,
                keywordApiPath,
                keywordApiModule, value};
        String url = ms.format(configApi);
        return url;
    }

    public static String getURLRestWithActionPlusValue(String domainApp, String keywordApiPath, String keywordApiModule, String action, String valueToAction) {
        MessageFormat ms = new MessageFormat(ConstantsURLs.FORMAT_URL_API_ACTION_PLUS_VALUE);
        String[] configApi = {domainApp,
                keywordApiPath,
                keywordApiModule, action, valueToAction};
        String url = ms.format(configApi);
        return url;
    }

    public static Time getCurrentTimeSQL() {
        Calendar currentDateAndTime = Calendar.getInstance();
        return new Time(currentDateAndTime.getTimeInMillis());
    }
}
