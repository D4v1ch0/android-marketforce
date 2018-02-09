package rp3.auna.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.provider.CallLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;

import rp3.auna.R;
import rp3.auna.bean.DrawerItem;
import rp3.auna.bean.ItemSubTitle;
import rp3.auna.bean.TitleMenu;


/**
 * Created by Jesus Villa on 22/01/2018.
 */

public class ReporteAdapter extends ExpandableRecyclerViewAdapter<ReporteAdapter.TitleViewHolder, ReporteAdapter.SubTitleViewHolder> {
    private static final String TAG = ReporteAdapter.class.getSimpleName();
    private Context _context;
    private ItemClickChild mListener;
    public ListenerAnimation listenerAnimation;

    public ReporteAdapter(Context context, List<? extends ExpandableGroup> groups, ItemClickChild mListener,ListenerAnimation listenerAnimation) {
        super(groups);
        this._context = context;
        this.mListener = mListener;
        this.listenerAnimation = listenerAnimation;
    }

    //region Methods

    @Override
    public TitleViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG,"onCreateGroupViewHolder...");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_drawer_expandable, parent, false);
        return new TitleViewHolder(view);
    }

    @Override
    public SubTitleViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG,"onCreateChildViewHolder...");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_drawer_subtitle, parent, false);
        return new SubTitleViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(SubTitleViewHolder holder, int flatPosition, ExpandableGroup group,
                                      final int childIndex) {
        Log.d(TAG,"onBindCHILDViewHolder...");
        final TitleMenu item = (TitleMenu) group;
        Log.d(TAG," size:"+item.getItemCount());
        //if(flatPosition==2){
            Log.d(TAG,"flatposition==2...");
            final ItemSubTitle subTitle = item.getItems().get(childIndex);
            Log.d(TAG,subTitle.toString());
            holder.setSubTitletName(subTitle.getSubTitle());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onChildClick(childIndex,subTitle);
                    listenerAnimation.setChild(subTitle);
                }
            });
        //}
    }

    @Override
    public void onBindGroupViewHolder(TitleViewHolder holder, int flatPosition, ExpandableGroup group) {
        Log.d(TAG,"onBindGROUPViewHolder...flatPosition:"+flatPosition);
        if(flatPosition==2) {
            Log.d(TAG, "flatPosition == 2...");
            holder.arrow.setVisibility(View.VISIBLE);
        }

        holder.setGenreTitle(_context, group);
    }

    //endregion

    //region Clases

    public class TitleViewHolder extends GroupViewHolder {
        private TextView titleName;
        private ImageView arrow;
        private ImageView icon;


        public TitleViewHolder(View itemView) {
            super(itemView);
            titleName = (TextView) itemView.findViewById(R.id.textViewDrawerItemTitleMain11);
            arrow = (ImageView) itemView.findViewById(R.id.list_item_arrow11);
            icon = (ImageView) itemView.findViewById(R.id.imageViewDrawerIconMain11);
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listenerAnimation.setParent(getAdapterPosition());
                }
            };
            itemView.setClickable(true);
            itemView.setOnClickListener(clickListener);
        }


        public void setGenreTitle(Context context, ExpandableGroup title) {
            if (title instanceof TitleMenu) {
                TitleMenu titleMenu = ((TitleMenu) title);
                if(titleMenu!=null){
                    if(titleMenu.getDrawable()!=null){
                        icon.setImageDrawable(titleMenu.getDrawable());
                    }
                    if(titleMenu.getTitulo()!=null){
                        titleName.setText(titleMenu.getTitulo());
                    }
                }
            }
        }

        @Override
        public void expand() {
            Log.d(TAG,"expand:"+getAdapterPosition());
            listenerAnimation.setAnimate(getAdapterPosition(),1);
            animateExpand();
        }

        @Override
        public void collapse() {
            listenerAnimation.setAnimate(getAdapterPosition(),0);
            animateCollapse();
            Log.d(TAG,"collapse:"+getAdapterPosition());
        }

        private void animateExpand() {
            RotateAnimation rotate =
                    new RotateAnimation(360, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            arrow.setAnimation(rotate);
        }

        private void animateCollapse() {
            RotateAnimation rotate =
                    new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            arrow.setAnimation(rotate);
        }
    }

    public class SubTitleViewHolder extends ChildViewHolder {

        private TextView subTitleTextView;

        public SubTitleViewHolder(View itemView) {
            super(itemView);
            subTitleTextView = (TextView) itemView.findViewById(R.id.subtitle11);
            itemView.setClickable(true);
        }

        public void setSubTitletName(String name) {
            subTitleTextView.setText(name);
        }
    }

    //endregion

    public interface ItemClickChild{
        void onChildClick(int position,ItemSubTitle itemSubTitle);
    }

    public interface ListenerAnimation{
        void setAnimate(int position,int animate);
        void setParent(int positionParent);
        void setChild(ItemSubTitle position);
    }
}
