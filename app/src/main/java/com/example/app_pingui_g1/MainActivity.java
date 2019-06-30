package com.example.app_pingui_g1;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.view.*;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


public class MainActivity extends AppCompatActivity {

    String usuario = null;
    String password = null;

    private RequestQueue mQueue;
    private String token = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mQueue = Volley.newRequestQueue(this);

    }



    public void obtenerDatos(View v) {

        EditText username = (EditText) findViewById(R.id.txt_usr);
        usuario = username.getText().toString();
        EditText pass = (EditText) findViewById(R.id.txt_psw);
        password = pass.getText().toString();

        iniciarSesion(usuario, password);
    }

    private void iniciarSesion(String usuario, String password) {
        Map<String, String> params = new HashMap();
        params.put("username", usuario);
        params.put("password", password);

        JSONObject parametros = new JSONObject(params);

        String login_url = "https://amstdb.herokuapp.com/db/nuevo-jwt";

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, login_url, parametros,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        try {
                            token = response.getString("token");
                            Intent menuPrincipal = new Intent(getBaseContext(), Menu.class);
                            menuPrincipal.putExtra("token", token);
                            startActivity(menuPrincipal);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Alerta");
                alertDialog.setMessage("Credenciales No validas");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
        mQueue.add(request);

    }


}
