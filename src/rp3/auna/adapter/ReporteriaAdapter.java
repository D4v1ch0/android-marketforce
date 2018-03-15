package rp3.auna.adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import java.util.List;

import rp3.auna.R;
import rp3.auna.bean.models.TitleChild;
import rp3.auna.bean.models.TitleParent;

/**
 * Created by Jesus Villa on 23/01/2018.
 */

public class ReporteriaAdapter extends ExpandableRecyclerAdapter<ReporteriaAdapter.TitleParentViewHolder,ReporteriaAdapter.TitleChildViewHolder> {
    private static final String TAG = ReporteriaAdapter.class.getSimpleName();
    private Context _context;
    private ListenerItems listenerItems;
    LayoutInflater inflater;

    public ReporteriaAdapter(Context context, List<ParentObject> parentItemList,ListenerItems listenerItems) {
        super(context, parentItemList);
        inflater = LayoutInflater.from(context);
        this.listenerItems = listenerItems;
    }

    //region Recycler

    @Override
    public TitleParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.item_drawer_expandable,viewGroup,false);
        return new TitleParentViewHolder(view);
    }

    @Override
    public TitleChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.item_drawer_subtitle,viewGroup,false);
        return new TitleChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(TitleParentViewHolder titleParentViewHolder, int i, Object o) {
        TitleParent title = (TitleParent)o;
        titleParentViewHolder.titleName.setText(title.getTitle());
        titleParentViewHolder.icon.setImageDrawable(title.getDrawable());
        if(i==2) {
            Log.d(TAG, "flatPosition == 2...");
            titleParentViewHolder.arrow.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBindChildViewHolder(TitleChildViewHolder titleChildViewHolder, final int i, Object o) {
        final TitleChild title = (TitleChild)o;
        titleChildViewHolder.subTitleTextView.setText(title.getSubtitle());
        titleChildViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenerItems.setChild(i,title);
            }
        });
        //titleChildViewHolder.option2.setText(title.getOption2());
    }

    //


    public class TitleParentViewHolder extends ParentViewHolder {
        private TextView titleName;
        private ImageView arrow;
        private ImageView icon;

        public TitleParentViewHolder(View itemView) {
            super(itemView);
            titleName = (TextView) itemView.findViewById(R.id.textViewDrawerItemTitleMain11);
            arrow = (ImageView) itemView.findViewById(R.id.list_item_arrow11);
            icon = (ImageView) itemView.findViewById(R.id.imageViewDrawerIconMain11);
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            Log.d(TAG,"onClickParent:"+getAdapterPosition());
            listenerItems.setParent(getAdapterPosition());
        }

        @Override
        public void setExpanded(boolean isExpanded) {
            super.setExpanded(isExpanded);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                if (isExpanded) {
                    animateExpand();
                } else {
                    animateCollapse();
                }
            }
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

    public class TitleChildViewHolder extends ChildViewHolder {
        private TextView subTitleTextView;
        public TitleChildViewHolder(View itemView) {
            super(itemView);
            subTitleTextView = (TextView) itemView.findViewById(R.id.subtitle11);
        }


    }

    public interface ListenerItems{
        void setParent(int positionParent);
        void setChild(int positionChild,TitleChild position);
    }
}
