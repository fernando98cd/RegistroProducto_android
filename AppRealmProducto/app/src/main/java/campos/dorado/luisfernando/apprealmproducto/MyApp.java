package campos.dorado.luisfernando.apprealmproducto;

import android.app.Application;

import java.util.concurrent.atomic.AtomicInteger;

import campos.dorado.luisfernando.apprealmproducto.modelos.Categoria;
import campos.dorado.luisfernando.apprealmproducto.modelos.Producto;
import campos.dorado.luisfernando.apprealmproducto.utilitarios.AutoIncrementable;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;

public class MyApp extends Application {

    public static AtomicInteger idProducto = new AtomicInteger();
    public static AtomicInteger idCategoria = new AtomicInteger();

    @Override
    public void onCreate() {
        super.onCreate();
        configurarRealmBd();
        Realm realm = Realm.getDefaultInstance();
        idCategoria = AutoIncrementable.generarID(realm, Categoria.class);
        idProducto = AutoIncrementable.generarID(realm, Producto.class);
    }

    private void configurarRealmBd() {
        Realm.init(getApplicationContext());
        RealmConfiguration config = new RealmConfiguration.Builder().name("myrealmbd.realm").build();
        Realm.setDefaultConfiguration(config);
    }
}
