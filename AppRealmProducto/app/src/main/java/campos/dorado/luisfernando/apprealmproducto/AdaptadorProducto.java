package campos.dorado.luisfernando.apprealmproducto;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import campos.dorado.luisfernando.apprealmproducto.modelos.Categoria;
import campos.dorado.luisfernando.apprealmproducto.modelos.Producto;

public class AdaptadorProducto extends BaseAdapter {
    //atributos
    private Context contexto;
    private List<Producto> list;
    private int layot;

    public AdaptadorProducto(Context contexto, List<Producto> list, int layot) {
        this.contexto = contexto;
        this.list = list;
        this.layot = layot;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProductoHolder ph;
        if (convertView ==null){
            convertView = LayoutInflater.from(contexto).inflate(layot,null);
            ph = new ProductoHolder();
            ph.id = convertView.findViewById(R.id.textViewidProducto);
            ph.nombre = convertView.findViewById(R.id.textViewNombreProducto);
            ph.precio = convertView.findViewById(R.id.textViewPrecioProducto);
            convertView.setTag(ph);
        }
        else {
            ph = (ProductoHolder) convertView.getTag();
        }
        Producto producto = list.get(position);
        ph.id.setText("ID :"+producto.getId());
        ph.nombre.setText(producto.getNombre_producto());
        ph.precio.setText("Bs: "+producto.getPrecio());
        return convertView;
    }

    public class ProductoHolder
    {
        TextView id;
        TextView nombre;
        TextView precio;

    }
}
