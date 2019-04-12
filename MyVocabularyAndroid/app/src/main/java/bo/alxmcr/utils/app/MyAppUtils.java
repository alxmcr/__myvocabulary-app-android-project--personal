package bo.alxmcr.utils.app;

import android.content.Context;
import android.util.Log;

import java.io.File;

/**
 * Created by JoseCoca-i7 on 18/05/2015.
 */
public class MyAppUtils {

    public static final String TAG = MyAppUtils.class.getSimpleName();

    public static void trimCache(Context context) {
        Log.v(TAG, "Start: trimCache");
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        Log.v(TAG, "End: trimCache");
    }

    public static boolean deleteDir(File dir) {
        Log.v(TAG, "Start: deleteDir");
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        Log.v(TAG, "End: deleteDir");
        // The directory is now empty so delete it
        return dir.delete();
    }
}
