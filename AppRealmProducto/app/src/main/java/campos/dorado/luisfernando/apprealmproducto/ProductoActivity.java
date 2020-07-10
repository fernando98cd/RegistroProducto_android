package campos.dorado.luisfernando.apprealmproducto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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

import campos.dorado.luisfernando.apprealmproducto.modelos.Categoria;
import campos.dorado.luisfernando.apprealmproducto.modelos.Producto;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmModel;

public class ProductoActivity extends AppCompatActivity {
    ListView listViewProductos;
    AdaptadorProducto adaptador;
    RealmList<Producto> productos;
    Realm realm;

    Categoria categoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);
        realm = Realm.getDefaultInstance();
        int idCategoria = getIntent().getExtras().getInt("id");
        categoria = realm.where(Categoria.class).equalTo("id",idCategoria).findFirst();

        Toast.makeText(this, "Categorias: "+categoria.getNombre(), Toast.LENGTH_LONG).show();
        productos = categoria.getProductos();
        this.setTitle("Adicionar : "+categoria.getNombre());
        listViewProductos = findViewById(R.id.listViewProducto);
        adaptador = new AdaptadorProducto(this,productos,R.layout.layout_adaptador_producto);
        listViewProductos.setAdapter(adaptador);

        //notificador de cambios de categorias para actualizar el listview de productos
        categoria.addChangeListener(new RealmChangeListener<RealmModel>() {
            @Override
            public void onChange(RealmModel realmModel) {
                adaptador.notifyDataSetChanged();
            }
        });
        registerForContextMenu(listViewProductos);
    }


    public void btnAddProducto_click(View view)
    {
        mostrarPantallaAdicionar("adicionar Productos para la categoria"+categoria.getNombre());
    }

    private void mostrarPantallaAdicionar(String msg) {
        AlertDialog.Builder ventana = new AlertDialog.Builder(this);
        ventana.setTitle("Productos");
        ventana.setMessage(msg);
        final View ventanainflada = LayoutInflater.from(this).inflate(R.layout.layout_add_producto,null);
        ventana.setView(ventanainflada);

        ventana.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try
                {
                    EditText txtNombreProducto = ventanainflada.findViewById(R.id.txtNombre_Producto);
                    EditText txtPrecioProducto = ventanainflada.findViewById(R.id.txtPrecio_Producto);
                    String nombreProducto = txtNombreProducto.getText().toString().trim();
                    float precio = Float.parseFloat(txtPrecioProducto.getText().toString());
                    if (nombreProducto.isEmpty())
                    {
                        throw new Exception("Error el nombre es obligatorio");
                    }
                    else
                    if(precio <=0)
                        throw new Exception("EL precio debe ser mayor a 0");
                    else
                        adicionarProducto(nombreProducto,precio);
                }
                catch (Exception e) {
                    Toast.makeText(ProductoActivity.this, "ERROR DEBIDO A :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        ventana.create().show();
    }
    private void adicionarProducto(String nombreProducto,float precio){
        try {
            Producto producto = new Producto(nombreProducto,precio);
            realm.beginTransaction();
            realm.copyToRealm(producto);
            categoria.getProductos().add(producto);
            realm.commitTransaction();
            Toast.makeText(this, "Producto  registrado correctamente", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(this, "ERROR DEBIDO A"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //para el menu  ---------------------------


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_producto_activity,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_producto_borrartodo:
                borrarTodoProducto();
                return  true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void borrarTodoProducto(){
        realm.beginTransaction();
        categoria.getProductos().deleteAllFromRealm();
        realm.commitTransaction();
    }

    //para el menu contextual


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_producto_contextual_activity,menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo informacion = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.menu_producto_eliminar:
                eliminarProducto(productos.get(informacion.position));
                return  true;
            case R.id.menu_producto_modificar:
                mostrarPantallaModificarProducto(productos.get(informacion.position));
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void eliminarProducto(Producto producto) {
        try {
            realm.beginTransaction();
            producto.deleteFromRealm();
            realm.commitTransaction();
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void mostrarPantallaModificarProducto(final Producto producto) {
        AlertDialog.Builder ventana = new AlertDialog.Builder(this);
        ventana.setTitle("Productos");
        ventana.setMessage("Modificar Productos");
        final View ventanainflada = LayoutInflater.from(this).inflate(R.layout.layout_add_producto,null);
        ventana.setView(ventanainflada);
        final EditText txtNombreProducto = ventanainflada.findViewById(R.id.txtNombre_Producto);
        final EditText txtPrecioProducto = ventanainflada.findViewById(R.id.txtPrecio_Producto);
        txtNombreProducto.setText(producto.getNombre_producto());
        txtPrecioProducto.setText(producto.getPrecio()+"");

        ventana.setPositiveButton("Modificar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try
                {
                    String nombreProducto = txtNombreProducto.getText().toString().trim();
                    float precio = Float.parseFloat(txtPrecioProducto.getText().toString());
                    if (nombreProducto.isEmpty())
                    {
                        throw new Exception("Error el nombre es obligatorio");
                    }
                    else
                    if(precio <=0)
                        throw new Exception("EL precio debe ser mayor a 0");
                    else
                        ModificarProducto(nombreProducto,precio,producto);
                }
                catch (Exception e) {
                    Toast.makeText(ProductoActivity.this, "ERROR DEBIDO A :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        ventana.create().show();
    }

    public  void ModificarProducto(String nombreProducto,float precio,Producto producto)
    {
        try {
            realm.beginTransaction();
            producto.setNombre_producto(nombreProducto);
            producto.setPrecio(precio);
            realm.copyToRealmOrUpdate(producto);
            realm.commitTransaction();
            Toast.makeText(this, "Producto  modificado correctamente", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(this, "ERROR DEBIDO A"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
