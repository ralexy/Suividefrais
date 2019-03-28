package fr.cned.emdsgil.suividevosfrais;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("GSB : Connexion");

        connexion_clic();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals(getString(R.string.retour_accueil))) {
            retourActivityPrincipale() ;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Retour à l'activité principale (le menu)
     */
    private void retourActivityPrincipale() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class) ;
        startActivity(intent) ;
    }

    /**
     * Méthode permettant de connecter l'utilisateur à GSB
     */
    private void connexion_clic() {
        findViewById(R.id.btnConnexion).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String login = ((EditText)findViewById(R.id.txtLogin)).getText().toString();
                final String motdepasse = ((EditText)findViewById(R.id.txtMotDePasse)).getText().toString();

                Log.d("API URL === ", Global.apiUrl);

                Log.d("Identifiants **** ", login + " " + motdepasse);

                // Instanciation de RequestQueue
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

                //Récupération de la réponse API et passage des identifiants pour la connexion
                StringRequest stringRequest = new StringRequest(Request.Method.POST,Global.apiUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Reponse API ------------", response);

                        try {
                            JSONObject jsonData = new JSONObject(response);

                            if(jsonData.has("result")) {
                                if (jsonData.has("message")) {
                                    Toast.makeText(LoginActivity.this, jsonData.getString("message"), Toast.LENGTH_LONG).show();
                                }

                                if (jsonData.has("idmembre")) {
                                    Global.idVisiteur = jsonData.getString("idmembre");

                                    // Redirection vers la vue principale
                                    Intent intent= new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            } else
                            {
                                Toast.makeText(LoginActivity.this, "Erreur de retour côté API", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this,"Erreur de connexion à l'API", Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("action", "login");
                        params.put("username", login);
                        params.put("password", motdepasse);

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
        });
    }
}
