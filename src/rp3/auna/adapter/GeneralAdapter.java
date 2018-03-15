package rp3.auna.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rp3.auna.R;

/**
 * Created by Jesus Villa on 03/11/2017.
 */

public class GeneralAdapter extends RecyclerView.Adapter<GeneralAdapter.GeneralViewHolder> {
    public int mSelectedItem = -1;
    OnSelectedListener _callback;
    private List<String> _list;
    private Context _context;


    public GeneralAdapter(Context context, OnSelectedListener listener) {
        this._context = context;
        this._list = new ArrayList<>();
        this._callback = listener;

    }

    @Override
    public GeneralAdapter.GeneralViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GeneralAdapter.GeneralViewHolder vh;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_general_value, parent, false);
        vh = new GeneralAdapter.GeneralViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final GeneralAdapter.GeneralViewHolder viewHolder, final int position) {
        viewHolder.tvDescripcion.setText(_list.get(position));
        viewHolder.radioButton.setChecked(position == mSelectedItem);
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

    public class GeneralViewHolder extends RecyclerView.ViewHolder  {
        @BindView(R.id.tvGeneralSelected) TextView tvDescripcion;
        @BindView(R.id.radioClick) RadioButton radioButton;
        public GeneralViewHolder(View itemView) {
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
            radioButton.setOnClickListener(clickListener);
            /*radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        mSelectedItem = getAdapterPosition();
                        notifyItemRangeChanged(0, _list.size());
                        _callback.onSelected(_list.get(mSelectedItem), getAdapterPosition());
                    }
                }
            });*/
            //tvDescripcion.setOnClickListener(clickListener);
        }
    }
}