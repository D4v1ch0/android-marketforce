package rp3.auna.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import rp3.auna.R;
import rp3.auna.bean.ItemFile;

/**
 * Created by Jesus Villa on 12/12/2017.
 */

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.MyViewHolder> {

    private ArrayList<ItemFile> mImagesList;
    private Context mContext;
    private SparseBooleanArray mSparseBooleanArray;
    OnItem onItem;

    public FilesAdapter(Context context,  OnItem o) {
        mContext = context;
        mSparseBooleanArray = new SparseBooleanArray();
        this.mImagesList = new ArrayList<>();
        onItem=o;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public FilesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_file, parent, false);
        return new FilesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FilesAdapter.MyViewHolder holder, int position) {
        int imageUrl = mImagesList.get(position).getFlag();
        String nombre = mImagesList.get(position).getNombre();
        if(imageUrl==1){
            Drawable drawable = mContext.getResources().getDrawable(R.drawable.ic_action_new_picture);
            drawable.setTint(R.color.bg_button_bg_main);
            holder.imageView.setImageDrawable(drawable);
            holder.tvName.setText(nombre);
        }else{
            Drawable drawable = mContext.getResources().getDrawable(R.drawable.pdf_icon);
            holder.imageView.setImageDrawable(drawable);
            holder.tvName.setText(nombre);
        }
    }

    public void clear() {
        mImagesList.clear();
        notifyDataSetChanged();
    }

    public void addMoreItems(List<ItemFile> moreItems) {
        clear();
        mImagesList.addAll(moreItems);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mImagesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public ImageView imageView;
        public MyViewHolder(View view) {
            super(view);
            tvName= (TextView) view.findViewById(R.id.tvName);
            imageView = (ImageView) view.findViewById(R.id.ivFile);
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItem.onSelect(getAdapterPosition());
                }
            };
            view.setOnClickListener(listener);
        }
    }

    public interface OnItem{
        void onSelect(int position);
        void onSelectLong(int position);
    }
}