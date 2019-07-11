package senac.finance.adapters;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import senac.finance.R;

public class FinanceViewHolder extends RecyclerView.ViewHolder {

    final ImageView tipo;
    final TextView dia;
    final TextView valor;
    final ImageButton delete;
    private View.OnClickListener mOnItemClickListener;

    public FinanceViewHolder(@NonNull View itemView) {
        super(itemView);
        tipo = itemView.findViewById(R.id.imageView);
        dia = itemView.findViewById(R.id.txtDia);
        valor = itemView.findViewById(R.id.txtValor);
        delete = itemView.findViewById(R.id.btnDelete);

        itemView.setTag(this);
        itemView.setOnClickListener(mOnItemClickListener);
    }
}
