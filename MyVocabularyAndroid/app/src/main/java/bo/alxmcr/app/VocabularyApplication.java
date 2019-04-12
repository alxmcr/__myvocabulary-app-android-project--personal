package bo.alxmcr.app;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import bo.alxmcr.app.services.word.ReaderWordsService;
import bo.alxmcr.utils.app.MyAppUtils;

/**
 * Created by JoseCoca-i7 on 29/04/2015.
 */
public class VocabularyApplication extends Application {

    public static final String TAG = VocabularyApplication.class.getSimpleName();
    private int contadorConexiones;
    private String modoDeConexionServidor;

    public int getContadorConexiones() {
        return contadorConexiones;
    }

    public void setContadorConexiones(int contadorConexiones) {
        this.contadorConexiones = contadorConexiones;
    }

    public String getModoDeConexionServidor() {
        return modoDeConexionServidor;
    }

    public void setModoDeConexionServidor(String modoDeConexionServidor) {
        this.modoDeConexionServidor = modoDeConexionServidor;
    }
}
