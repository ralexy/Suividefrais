package fr.cned.emdsgil.suividevosfrais;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import fr.cned.emdsgil.suividevosfrais.Global;
import fr.cned.emdsgil.suividevosfrais.Serializer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("GSB : Suivi des frais");
        // récupération des informations sérialisées
        recupSerialize();
        // chargement des méthodes événementielles
        cmdMenu_clic(((ImageButton) findViewById(R.id.cmdKm)), KmActivity.class);
        cmdMenu_clic(((ImageButton) findViewById(R.id.cmdHf)), HfActivity.class);
        cmdMenu_clic(((ImageButton) findViewById(R.id.cmdHfRecap)), HfRecapActivity.class);
        cmdMenu_clic(((ImageButton) findViewById(R.id.cmdRepas)), RepasActivity.class);
        cmdMenu_clic(((ImageButton) findViewById(R.id.cmdNuitee)), NuiteeActivity.class);
        cmdMenu_clic(((ImageButton) findViewById(R.id.cmdEtape)), EtapeActivity.class);
        cmdTransfert_clic(((ImageButton) findViewById(R.id.cmdTransfert)), MainActivity.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Récupère la sérialisation si elle existe
     */
    private void recupSerialize() {
        /* Pour éviter le warning "Unchecked cast from Object to Hash" produit par un casting direct :
         * Global.listFraisMois = (Hashtable<Integer, FraisMois>) Serializer.deSerialize(Global.filename, MainActivity.this);
         * On créé un Hashtable générique <?,?> dans lequel on récupère l'Object retourné par la méthode deSerialize, puis
         * on cast chaque valeur dans le type attendu.
         * Seulement ensuite on affecte cet Hastable à Global.listFraisMois.
        */
        Hashtable<?, ?> monHash = (Hashtable<?, ?>) Serializer.deSerialize(MainActivity.this);
        if (monHash != null) {
            Hashtable<Integer, FraisMois> monHashCast = new Hashtable<>();
            for (Hashtable.Entry<?, ?> entry : monHash.entrySet()) {
                monHashCast.put((Integer) entry.getKey(), (FraisMois) entry.getValue());
            }
            Global.listFraisMois = monHashCast;
        }
        // si rien n'a été récupéré, il faut créer la liste
        if (Global.listFraisMois == null) {
            Global.listFraisMois = new Hashtable<>();
            /* Retrait du type de l'HashTable (Optimisation Android Studio)
			 * Original : Typage explicit =
			 * Global.listFraisMois = new Hashtable<Integer, FraisMois>();
			*/

        }
    }

    /**
     * Sur la sélection d'un bouton dans l'activité principale ouverture de l'activité correspondante
     */
    private void cmdMenu_clic(ImageButton button, final Class classe) {
        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // ouvre l'activité
                Intent intent = new Intent(MainActivity.this, classe);
                startActivity(intent);
            }
        });
    }

    /**
     * Cas particulier du bouton pour le transfert d'informations vers le serveur
     */
    private void cmdTransfert_clic(ImageButton button, final Class classe) {
        findViewById(R.id.cmdTransfert).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                if(Global.idVisiteur == null) {
                    Intent intent= new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    // envoi les informations sérialisées vers le serveur
                    //Log.d("MSG ----------", Global.getListFraisMoisJSON());

                    // Instanciation de RequestQueue
                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

                    //Récupération de la réponse API et passage des identifiants pour la connexion
                    StringRequest stringRequest = new StringRequest(Request.Method.POST,Global.apiUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Reponse API -----------", response);

                            try {
                                JSONObject jsonData = new JSONObject(response);

                                if(jsonData.has("result")) {
                                    if (jsonData.has("message")) {
                                        Toast.makeText(MainActivity.this, jsonData.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                } else
                                {
                                    Toast.makeText(MainActivity.this, "Erreur de retour côté API", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this,"Erreur de connexion à l'API", Toast.LENGTH_LONG).show();
                        }
                    }){
                        @Override
                        protected Map<String,String> getParams(){
                            Map<String,String> params = new HashMap<>();
                            params.put("action", "synchronize");
                            params.put("memberId", Global.idVisiteur);
                            params.put("expenses", Global.getListFraisMoisJSON());

                            return params;
                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("Content-Type","application/x-www-form-urlencoded");
                            return params;
                        }
                    };
                    // Ajout de la requête à la file d'attente
                    queue.add(stringRequest);
                }
            }
        });
    }
}
