package com.example.saravia.laboratorio45_practica;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private Button btnInsertar;
    private Button btnConsultar;
    private Button btnEliminar;
    private Button btnLlamadas;
    private TextView txtResultados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Referencias a los controles
        txtResultados = (TextView)findViewById(R.id.TxtResultados);
        btnConsultar = (Button)findViewById(R.id.BtnConsultar);
        btnInsertar = (Button)findViewById(R.id.BtnInsertar);
        btnEliminar = (Button)findViewById(R.id.BtnEliminar);
        btnLlamadas = (Button)findViewById(R.id.BtnLlamadas);

        btnConsultar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //Columnas de la tabla a recuperar
                String[] projection = new String[] {
                        ClientesProvider.Clientes._ID,
                        ClientesProvider.Clientes.COL_NOMBRE,
                        ClientesProvider.Clientes.COL_TELEFONO,
                        ClientesProvider.Clientes.COL_EMAIL };

                Uri clientesUri =  ClientesProvider.CONTENT_URI;

                ContentResolver cr = getContentResolver();

                //Hacemos la consulta
                Cursor cur = cr.query(clientesUri,
                        projection, //Columnas a devolver
                        null,       //Condición de la query
                        null,       //Argumentos variables de la query
                        null);      //Orden de los resultados

                if (cur.moveToFirst())
                {
                    String nombre;
                    String telefono;
                    String email;

                    int colNombre = cur.getColumnIndex(ClientesProvider.Clientes.COL_NOMBRE);
                    int colTelefono = cur.getColumnIndex(ClientesProvider.Clientes.COL_TELEFONO);
                    int colEmail = cur.getColumnIndex(ClientesProvider.Clientes.COL_EMAIL);

                    txtResultados.setText("");

                    do
                    {

                        nombre = cur.getString(colNombre);
                        telefono = cur.getString(colTelefono);
                        email = cur.getString(colEmail);

                        txtResultados.append(nombre + " - " + telefono + " - " + email + "\n");

                    } while (cur.moveToNext());
                }
            }
        });

        btnInsertar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ContentValues values = new ContentValues();

                values.put(ClientesProvider.Clientes.COL_NOMBRE, "ClienteN");
                values.put(ClientesProvider.Clientes.COL_TELEFONO, "999111222");
                values.put(ClientesProvider.Clientes.COL_EMAIL, "nuevo@email.com");

                ContentResolver cr = getContentResolver();

                cr.insert(ClientesProvider.CONTENT_URI, values);
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ContentResolver cr = getContentResolver();

                cr.delete(ClientesProvider.CONTENT_URI, ClientesProvider.Clientes.COL_NOMBRE + " = 'ClienteN'", null);
            }
        });

        btnLlamadas.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                String[] projection = new String[] {
                        CallLog.Calls.TYPE,
                        CallLog.Calls.NUMBER };

                Uri llamadasUri =  CallLog.Calls.CONTENT_URI;

                ContentResolver cr = getContentResolver();

                Cursor cur = cr.query(llamadasUri,
                        projection, //Columnas a devolver
                        null,       //Condición de la query
                        null,       //Argumentos variables de la query
                        null);      //Orden de los resultados

                if (cur.moveToFirst())
                {
                    int tipo;
                    String tipoLlamada = "";
                    String telefono;

                    int colTipo = cur.getColumnIndex(CallLog.Calls.TYPE);
                    int colTelefono = cur.getColumnIndex(CallLog.Calls.NUMBER);

                    txtResultados.setText("");

                    do
                    {

                        tipo = cur.getInt(colTipo);
                        telefono = cur.getString(colTelefono);

                        if(tipo == CallLog.Calls.INCOMING_TYPE)
                            tipoLlamada = "ENTRADA";
                        else if(tipo == CallLog.Calls.OUTGOING_TYPE)
                            tipoLlamada = "SALIDA";
                        else if(tipo == CallLog.Calls.MISSED_TYPE)
                            tipoLlamada = "PERDIDA";

                        txtResultados.append(tipoLlamada + " - " + telefono + "\n");

                    } while (cur.moveToNext());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}