package campos.dorado.luisfernando.apprealmproducto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.OutputStreamWriter;

import campos.dorado.luisfernando.apprealmproducto.modelos.Categoria;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    RealmResults<Categoria> categoria;
    Realm realm;

    ListView listaViewCategoria;
    AdaptadorCategoria adaptadorCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realm = Realm.getDefaultInstance();
        /*
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
        //Insertamos producto de forma manual

        realm.beginTransaction();
        Categoria objcategoria = new Categoria("frutas");
        realm.copyToRealm(objcategoria);
        Categoria objcategoria1 = new Categoria("verduras");
        realm.copyToRealm(objcategoria1);
        Categoria objcategoria2 = new Categoria("lacteos");
        realm.copyToRealm(objcategoria2);
        Categoria objcategoria3 = new Categoria("carnes");
        realm.copyToRealm(objcategoria3);
        realm.commitTransaction();

        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
        */



        categoria = realm.where(Categoria.class).findAll();
        adaptadorCategoria = new AdaptadorCategoria(this,categoria,R.layout.layout_adaptador_categoria);
        listaViewCategoria = findViewById(R.id.listViewCategoria);
        listaViewCategoria.setAdapter(adaptadorCategoria);

        //para actualizar automaticamente
        categoria.addChangeListener(new RealmChangeListener<RealmResults<Categoria>>() {
            @Override
            public void onChange(RealmResults<Categoria> categorias) {
                adaptadorCategoria.notifyDataSetChanged();
            }
        });

        //PARA ADICIONAR UN EVENTO A LOS ITEMS
        listaViewCategoria.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,ProductoActivity.class);
                intent.putExtra("id",categoria.get(position).getId());
                startActivity(intent);
            }
        });

        registerForContextMenu(listaViewCategoria);
    }

    public void btnAddCategoria(View view)
    {
        mostrarPantallaAdicionar("adicionar categoria de productos");
    }

    public void mostrarPantallaAdicionar(String msg) {
        AlertDialog.Builder ventana = new AlertDialog.Builder(this);
        ventana.setTitle("Categoria");
        ventana.setMessage(msg);
        final View ventanainflada = LayoutInflater.from(this).inflate(R.layout.layout_add_categoria,null);
        ventana.setView(ventanainflada);

        ventana.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText txtNombreCategoria = ventanainflada.findViewById(R.id.txtNombreCategoria);
                String nombreCategoria = txtNombreCategoria.getText().toString().trim();
                if (nombreCategoria.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "El nombre de la categoria no debe ser nulo", Toast.LENGTH_SHORT).show();
                }
                else
                    adicionarCategoria(nombreCategoria);
            }
        });
        ventana.create().show();
    }
    private void adicionarCategoria(String nombreCategoria){
        try {
            Categoria categoria = new Categoria(nombreCategoria);
            realm.beginTransaction();
            realm.copyToRealm(categoria);
            realm.commitTransaction();
            Toast.makeText(this, "Categoria registrado correctamente", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(this, "ERROR DEBIDO A"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_categoria_activity,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_categoria_borrar_todo:
                borrarTodoCategoria();
                return  true;
            case R.id.menu_categoria_exportar:
                exportarToJSON();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public  void exportarToJSON()
    {
        try {
            OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput("categorias.json", Context.MODE_PRIVATE));
            archivo.write(categoria.asJSON());
            archivo.close();
            Toast.makeText(this, "Archivo exportado correctamente", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
    public void borrarTodoCategoria(){
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
    }

    //para el menu contextual


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        AdapterView.AdapterContextMenuInfo informacion = (AdapterView.AdapterContextMenuInfo)menuInfo;
        menu.setHeaderTitle(categoria.get(informacion.position).getNombre());
        getMenuInflater().inflate(R.menu.menu_categoria_contextual_activity,menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo informacion = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.menu_categoria_eliminar:
                eliminarCategoria(categoria.get(informacion.position));
                return  true;
            case R.id.menu_categoria_modificar:
                mostrarPantallaModificarCategoria(categoria.get(informacion.position));
                return true;
        }
        return super.onContextItemSelected(item);
    }

    public void mostrarPantallaModificarCategoria(final Categoria categoria) {
        AlertDialog.Builder ventana = new AlertDialog.Builder(this);
        ventana.setTitle("Categoria");
        ventana.setMessage("Modificar Categoria");
        final View ventanainflada = LayoutInflater.from(this).inflate(R.layout.layout_add_categoria,null);
        ventana.setView(ventanainflada);
        final EditText txtNombreCategoria = ventanainflada.findViewById(R.id.txtNombreCategoria);
        txtNombreCategoria.setText(categoria.getNombre());
        ventana.setPositiveButton("Modificar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //EditText txtNombreCategoria = ventanainflada.findViewById(R.id.txtNombreCategoria);
                String nombreCategoria = txtNombreCategoria.getText().toString().trim();
                if (nombreCategoria.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "El nombre de la categoria no debe ser nulo", Toast.LENGTH_SHORT).show();
                }
                else {
                    ModoficarCategoria(nombreCategoria,categoria);
                }
            }
        });
        ventana.create().show();
    }

    private void eliminarCategoria(Categoria categoria) {
        realm.beginTransaction();
        categoria.deleteFromRealm();
        realm.commitTransaction();
    }

    public void ModoficarCategoria(String nuevoNombre,Categoria categoria)
    {
        realm.beginTransaction();
        categoria.setNombre(nuevoNombre);
        realm.copyToRealmOrUpdate(categoria);
        realm.commitTransaction();
    }


}

//menu principal

