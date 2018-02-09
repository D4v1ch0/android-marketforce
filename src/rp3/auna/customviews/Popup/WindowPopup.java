package rp3.auna.customviews.Popup;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.transition.Transition;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import rp3.auna.R;
import rp3.auna.util.helper.Util;

/**
 * Created by Jesus Villa on 07/11/2017.
 */

public class WindowPopup  {
    private static final String TAG = WindowPopup.class.getSimpleName();
    private static final int MSG_DISMISS_TOOLTIP = 1000;
    private Context ctx;
    private android.widget.PopupWindow tipWindow;
    private View contentView;
    private LayoutInflater inflater;
    private View tvLlamada;
    private View tvVisita;
    private View line;
    private View viewShared;
    private callbackWindow callback;
    private View lyviewcontent;

    public WindowPopup(Context ctx,callbackWindow callback) {
        this.ctx = ctx;
        tipWindow = new android.widget.PopupWindow(ctx);
        inflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.popupwindow, null);
        this.callback = callback;
        views(contentView);
        initActions();
    }

    private void views(View view){
        tvLlamada =  view.findViewById(R.id.tvAgendarLlamada);
        tvVisita = view.findViewById(R.id.tvAgendarVisita);
        viewShared = view.findViewById(R.id.viewShared);
    }

    public void init(int tip){
        switch (tip){
            case 1:
                tvVisita.setVisibility(View.GONE);
                break;
            case 2:
                tvLlamada.setVisibility(View.GONE);
                break;
            default:
                tvVisita.setVisibility(View.VISIBLE);
                tvLlamada.setVisibility(View.VISIBLE);
        }
    }

    private void initActions(){
        tvLlamada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onSelected(1);
                dismissTooltip();
            }
        });

        tvVisita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onSelected(2);
                dismissTooltip();
            }
        });

        viewShared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onSelected(3);
                dismissTooltip();
            }
        });
    }


    public void showToolTip(View anchor) {
        tipWindow.setHeight(ActionBar.LayoutParams.WRAP_CONTENT);
        tipWindow.setWidth(ActionBar.LayoutParams.WRAP_CONTENT);
        tipWindow.setOutsideTouchable(true);
        tipWindow.setTouchable(true);
        tipWindow.setFocusable(true);
        tipWindow.setBackgroundDrawable(new BitmapDrawable());
        tipWindow.setContentView(contentView);
        int screen_pos[] = new int[2];
// Get location of anchor view on screen
        anchor.getLocationOnScreen(screen_pos);
// Get rect for anchor view
        Rect anchor_rect = new Rect(screen_pos[0], screen_pos[1], screen_pos[0]
                + anchor.getWidth(), screen_pos[1] + anchor.getHeight());
// Call view measure to calculate how big your view should be.
        contentView.measure(ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT);
        int contentViewHeight = contentView.getMeasuredHeight();
        int contentViewWidth = contentView.getMeasuredWidth();
// In this case , i dont need much calculation for x and y position of
// tooltip
// For cases if anchor is near screen border, you need to take care of
// direction as well
// to show left, right, above or below of anchor view
        int position_x = anchor_rect.centerX() - (contentViewWidth / 2);
        int position_y = anchor_rect.bottom - (anchor_rect.height() / 2);
        Log.d(TAG,"anchor:"+anchor+" NO_GRAVITY"+" position_x:"+position_x+" position_y:"+position_y);
        Rect rect = Util.locateView(anchor);

        //tipWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, position_x,position_y);
        tipWindow.showAtLocation(anchor, Gravity.TOP|Gravity.LEFT, rect.left, rect.bottom);

        //tipWindow.showAtLocation(anchor, Gravity.BOTTOM, 0,0);
        //tipWindow.showAsDropDown(anchor);
// send message to handler to dismiss tipWindow after X milliseconds
        //handler.sendEmptyMessageDelayed(MSG_DISMISS_TOOLTIP, 4000);
    }

    public boolean isTooltipShown() {
        if (tipWindow != null && tipWindow.isShowing()) {
            return true;
        }
        return false;
    }

    public void setAnimation(int style){
        tipWindow.setAnimationStyle(style);
    }

    public void dismissTooltip() {
        if (tipWindow != null && tipWindow.isShowing()) {
            tipWindow.dismiss();
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_DISMISS_TOOLTIP:
                    if (tipWindow != null && tipWindow.isShowing())
                        //tipWindow.dismiss();
                    break;
            }
        };
    };

    public interface callbackWindow{
        void onSelected(int position);
    }
}
