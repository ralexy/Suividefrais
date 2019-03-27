package fr.cned.emdsgil.suividevosfrais;

import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import fr.cned.emdsgil.suividevosfrais.FraisMois;

abstract class Global {

    // tableau d'informations mémorisées
    public static Hashtable<Integer, FraisMois> listFraisMois = new Hashtable<>();
    /* Retrait du type de l'Hashtable (Optimisation Android Studio)
     * Original : Typage explicit =
	 * public static Hashtable<Integer, FraisMois> listFraisMois = new Hashtable<Integer, FraisMois>();
	*/

    // fichier contenant les informations sérialisées
    public static final String filename = "save.fic";

    /*
      * Id du visiteur, vide si non connecté à l'app web GSB
      * Permet de mettre à jour les informations du bon visiteur médical
     */
    public static String idVisiteur = "a17";

    // Url de l'API GSB
    public static final String apiUrl = "http://192.168.1.6/GSB_API/api.php";

    /**
     * Modification de l'affichage de la date (juste le mois et l'année, sans le jour)
     */
    public static void changeAfficheDate(DatePicker datePicker, boolean afficheJours) {
        try {
            Field f[] = datePicker.getClass().getDeclaredFields();
            for (Field field : f) {
                int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
                datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), null);
                if (daySpinnerId != 0)
                {
                    View daySpinner = datePicker.findViewById(daySpinnerId);
                    if (!afficheJours)
                    {
                        daySpinner.setVisibility(View.GONE);
                    }
                }
            }
        } catch (SecurityException | IllegalArgumentException e) {
            Log.d("ERROR", e.getMessage());
        }

        getListFraisMoisJSON();
    }

    public static void getListFraisMoisJSON() {
        String jsonObj = new Gson().toJson(listFraisMois);

        Set<Integer> keys = listFraisMois.keySet();

        for (Integer key : keys) {
            System.out.println(listFraisMois.get(key).toString());
            System.out.println(jsonObj.toString());
        }
    }
}
