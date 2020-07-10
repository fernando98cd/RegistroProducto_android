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

public class AdaptadorCategoria extends BaseAdapter {

    //atributos
    private Context contexto;
    private List<Categoria> list;
    private int layot;

    public AdaptadorCategoria(Context contexto, List<Categoria> list, int layot) {
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
        CategoriaHolder ch;
        if (convertView ==null){
            convertView = LayoutInflater.from(contexto).inflate(layot,null);
            ch = new CategoriaHolder();
            ch.nombre = convertView.findViewById(R.id.textViewNombreCategoria);
            ch.cantidad = convertView.findViewById(R.id.textViewCantProductos);
            ch.fechaCreacion = convertView.findViewById(R.id.textViewFechaCreacion);
            convertView.setTag(ch);
        }
        else {
            ch = (CategoriaHolder) convertView.getTag();
        }
        Categoria categoria = list.get(position);
        ch.nombre.setText(categoria.getNombre());
        ch.cantidad.setText("("+categoria.getProductos().size()+")");
        String fechaformateada = DateUtils.formatDateTime(this.contexto,categoria.getCreatedAt().getTime(),DateUtils.FORMAT_ABBREV_MONTH);
        ch.fechaCreacion.setText(fechaformateada);
        return convertView;
    }

    public class CategoriaHolder
    {
        TextView nombre;
        TextView cantidad;
        TextView fechaCreacion;

    }
}
