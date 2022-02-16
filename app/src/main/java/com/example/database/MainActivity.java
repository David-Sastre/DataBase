package com.example.database;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    //Declaramos los elementos de la interfaz
    private Button btnCrear;
    private Button btnVeure;
    private Button btnEliminar;
    private EditText editTitol;
    private EditText editComent;
    private EditText txtTitol;
    private EditText txtComent;

    //Declaración del spinner y su Adapter
    private Spinner spinComentaris;
    private ArrayAdapter spinnerAdapter;

    //Lista de comentarios y comentario actual
    private ArrayList<Comentario>lista;
    private Comentario c;

    //Controlador de bases de datos
    private DataBaseAssistant db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Iniciamos los elementos de la interfaz
        editTitol=(EditText) findViewById(R.id.editTitol);
        editComent =(EditText)findViewById(R.id.editComent);
        txtTitol=(EditText) findViewById(R.id.txtTitol);
        txtComent =(EditText)findViewById(R.id.txtComent);

        //Los elementos del panel inferior no seran editables
        txtTitol.setEnabled(false);
        txtComent.setEnabled(false);

        btnCrear=(Button)findViewById(R.id.btnCrear);
        btnVeure=(Button)findViewById(R.id.btnVeure);
        btnEliminar=(Button)findViewById(R.id.btnEliminar);

        btnCrear.setOnClickListener(this);
        btnVeure.setOnClickListener(this);
        btnEliminar.setOnClickListener(this);
        //Iniciamos el controlador de la base de datos
        db=new DataBaseAssistant(this);

        //Iniciamos el spinner y la lista de comentarios
        spinComentaris=(Spinner) findViewById(R.id.spinComentaris);
        lista=db.getComments();

        //Creamos el adapter y lo asociamos al spinner
        spinnerAdapter=new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,lista);
        spinComentaris.setAdapter(spinnerAdapter);
        spinComentaris.setOnItemSelectedListener(this);


    }

    @Override
    public void onClick(View v) {
        //Acciones de cada boton
        switch(v.getId()){
            case R.id.btnCrear:
                //Insertamos un nuevo elemento en base de datos
                db.insertar(editTitol.getText().toString(), editComent.getText().toString());
                //Actualizamos la lista de comentarios
                lista=db.getComments();
                //Actualizamos el adapter y lo asociamos de nuevo al spinner
                spinnerAdapter=new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,lista);
                spinComentaris.setAdapter(spinnerAdapter);
                //Limpiamos el formulario
                editTitol.setText("");
                editComent.setText("");

                break;
            case R.id.btnVeure:
                //Si hay algun comentario seleccionado mostramos sus valores en la parte inferior
                if(c!=null) {
                    txtTitol.setText(c.getNombre());
                    txtComent.setText(c.getComentario());
                }
                break;
            case R.id.btnEliminar:
                //Si hay algun comentario seleccionado lo borramos de la base de datos y actualizamos el spinner
                if(c!=null) {
                    db.borrar(c.getId());
                    lista = db.getComments();
                    spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, lista);
                    spinComentaris.setAdapter(spinnerAdapter);
                    //Limpiamos los datos del panel inferior
                    txtTitol.setText("");
                    txtComent.setText("");
                    //Eliminamos el Comentario actual puesto que ya no existe en base de datos
                    c=null;
                }
                break;
        }
    }




    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {
        if (adapterView.getId() == R.id.spinComentaris) {
            //Si hay elementos en la base de datos, establecemos el comentario actual a partir del
            //indice del elemento seleccionado en el spinner
            if (lista.size() > 0) {
                c = lista.get(i);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}