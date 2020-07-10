package campos.dorado.luisfernando.apprealmproducto.utilitarios;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class AutoIncrementable {
    public static <T extends RealmObject> AtomicInteger generarID(Realm bd, Class<T> objModelo)
    {
        RealmResults<T> resultado = bd.where(objModelo).findAll();
        if(resultado.size()>0){
            return  new AtomicInteger(resultado.max("id").intValue());
        }
        else
            return new AtomicInteger(0);
    }
}
