package bo.alxmcr.utils.typography;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import alxmcr.bo.vocabularyapp_v8.R;

/**
 * Created by Jose Coca on 25/04/2015.
 */
public class TypographyUtils {

    public static final String TAG = "TypographyUtils.";

    public static boolean setTypographyForATextView(TextView textView, String fontPath, AssetManager assets, Resources resources) {

        if (resources == null) {
            Log.e(TAG, "resources is null");
            return false;
        }

        if (assets == null) {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            final String msg = resources.getString(R.string.msg_error_assets_is_null);
            Log.e(typeMessage, msg);
            return false;
        }

        if (textView == null) {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            final String msg = resources.getString(R.string.msg_error_text_view_is_null);
            Log.e(typeMessage, msg);

            return false;
        }

        try {
            Typeface tf = Typeface.createFromAsset(assets, fontPath);
            textView.setTypeface(tf);
        } catch (RuntimeException e) {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_exception);
            final String msg = e.getMessage();
            Log.e(typeMessage, msg, e);
        } catch (Exception e) {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_exception);
            final String msg = e.getMessage();
            Log.e(typeMessage, msg, e);
        }

        return true;
    }

    public static boolean setTypographyForAButton(Button button, String fontPath, AssetManager assets, Resources resources) {
        if (resources == null) {
            Log.e(TAG, "resources is null");
            return false;
        }

        if (assets == null) {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            final String msg = resources.getString(R.string.msg_error_assets_is_null);
            Log.e(typeMessage, msg);
            return false;
        }

        if (button == null) {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            final String msg = resources.getString(R.string.msg_error_button_is_null);
            Log.e(typeMessage, msg);

            return false;
        }

        // Loading Font Face
        try {
            Typeface tf = Typeface.createFromAsset(assets, fontPath);
            button.setTypeface(tf);
        } catch (RuntimeException e) {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_exception);
            final String msg = e.getMessage();
            Log.e(typeMessage, msg, e);
        } catch (Exception e) {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_exception);
            final String msg = e.getMessage();
            Log.e(typeMessage, msg, e);
        }

        return true;
    }

    public static boolean setTypographyForAnEditText(EditText editText, String fontPath, AssetManager assets, Resources resources) {
        if (resources == null) {
            Log.e(TAG, "resources is null");
            return false;
        }

        if (assets == null) {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            final String msg = resources.getString(R.string.msg_error_assets_is_null);
            Log.e(typeMessage, msg);
            return false;
        }

        if (editText == null) {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            final String msg = resources.getString(R.string.msg_error_edit_text_is_null);
            Log.e(typeMessage, msg);

            return false;
        }

        // Loading Font Face
        try {
            Typeface tf = Typeface.createFromAsset(assets, fontPath);
            editText.setTypeface(tf);
        } catch (RuntimeException e) {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_exception);
            final String msg = e.getMessage();
            Log.e(typeMessage, msg, e);
        } catch (Exception e) {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_exception);
            final String msg = e.getMessage();
            Log.e(typeMessage, msg, e);
        }

        return true;
    }
}
