package campos.dorado.luisfernando.apprealmproducto.modelos;

import java.util.Date;

import campos.dorado.luisfernando.apprealmproducto.MyApp;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;


//se convierte en una bd
/*
* PASO 1 CREAR UNA CLASE QUE SERVIRA COMO UNA BASE DE DATOS
* PASO 2 CREAR LOS ATRIBUTOS O TIPOS DE DATOS QUE TENDRA LA BD
* PASO 3 CREAR DOS CONTRUCTORES UNO VACIO Y OTRO INSTANCIADO LOS ATRIBUTOS
* PASO 4 CREAR SUS SETTER Y GETTER*/




public class Categoria extends RealmObject {
    @PrimaryKey
    private int id;
    @Required // equivale a not null
    private String nombre;
    @Required
    private Date createdAt;

    //crear relacion de 1 a muchos con producto
    RealmList<Producto> productos;

    ////////////////////////////////////////
    public Categoria()
    {
    }
    public Categoria(String nombre){
        this.id = MyApp.idCategoria.incrementAndGet();
        this.nombre = nombre;
        this.createdAt = new Date();
        this.productos = new RealmList<Producto>();
    }


    ///////////////////////////////////////////////
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public RealmList<Producto> getProductos() {
        return productos;
    }

    public void setProductos(RealmList<Producto> productos) {
        this.productos = productos;
    }
}
