package campos.dorado.luisfernando.apprealmproducto.modelos;

import campos.dorado.luisfernando.apprealmproducto.MyApp;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Producto extends RealmObject {
    @PrimaryKey
    private int id;
    @Required
    private String nombre_producto;
    private float precio;

    public Producto(){}

    public Producto(String nombre,float precio){
        this.id = MyApp.idProducto.incrementAndGet();
        this.nombre_producto = nombre;
        this.precio = precio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }
}
