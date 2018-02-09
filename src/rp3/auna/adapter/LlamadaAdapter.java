package rp3.auna.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rp3.auna.R;

/**
 * Created by Jesus Villa on 18/10/2017.
 */

public class LlamadaAdapter  extends RecyclerView.Adapter<LlamadaAdapter.DireccionViewHolder> {

    public int mSelectedItem = -1;
    OnSelectedListener _callback;
    private List<String> _list;
    private Activity _context;


    public LlamadaAdapter(Activity context, OnSelectedListener listener) {
        this._context = context;
        this._list = new ArrayList<>();
        this._callback = listener;

    }

    @Override
    public LlamadaAdapter.DireccionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LlamadaAdapter.DireccionViewHolder vh;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dialog_normal, parent, false);
        vh = new DireccionViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final LlamadaAdapter.DireccionViewHolder viewHolder, final int position) {
        viewHolder.tvDescripcion.setText(_list.get(position));
        viewHolder.rbSelected.setChecked(position == mSelectedItem);
    }

    @Override
    public int getItemCount() {
        return _list.size();
    }

    public void addMoreItems(List<String> moreItems) {
        _list.addAll(moreItems);
        notifyDataSetChanged();
    }

    public void clear() {
        _list.clear();
        notifyDataSetChanged();
    }


    public interface OnSelectedListener {
        void onSelected(String selected,int position);
    }

    public class DireccionViewHolder extends RecyclerView.ViewHolder  {
        @BindView(R.id.tvDireccion) TextView tvDescripcion;
        @BindView(R.id.rbDireccion) RadioButton rbSelected;
        public DireccionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = getAdapterPosition();
                    notifyItemRangeChanged(0, _list.size());
                    _callback.onSelected(_list.get(mSelectedItem),getAdapterPosition());
                }
            };
            rbSelected.setOnClickListener(clickListener);
        }
    }
}
