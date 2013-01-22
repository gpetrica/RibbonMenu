package com.darvds.ribbonmenu;


import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

public class RibbonMenuView<T> extends LinearLayout {

    private ListView rbmListView;
    private View rbmOutsideView;

    private RibbonMenuCallback<T> callback;

    public RibbonMenuView(Context context) {
        super(context);
        load();
    }

    public RibbonMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        load();
    }

    private void load() {
        if (isInEditMode()) return;
        inflateLayout();
        initUi();
    }


    private void inflateLayout() {
        try {
            LayoutInflater.from(getContext()).inflate(R.layout.rbm_menu, this, true);
        } catch (Exception e) { }
    }

    private void initUi() {
        rbmListView = (ListView) findViewById(R.id.rbm_listview);
        rbmOutsideView = (View) findViewById(R.id.rbm_outside_view);

        rbmOutsideView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideMenu();
            }
        });


        rbmListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (callback != null)
                    callback.RibbonMenuItemClick((T) rbmListView.getAdapter().getItem(position));
                hideMenu();
            }

        });
    }


    public void setMenuClickCallback(RibbonMenuCallback callback) {
        this.callback = callback;
    }

    public void setMenuItems(ListAdapter adapter) {
        rbmListView.setAdapter(adapter);
    }


    public void setBackgroundResource(int resource) {
        rbmListView.setBackgroundResource(resource);
    }


    public void showMenu() {
        rbmOutsideView.setVisibility(View.VISIBLE);

        rbmListView.setVisibility(View.VISIBLE);
        rbmListView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.rbm_in_from_left));
    }


    public void hideMenu() {
        rbmOutsideView.setVisibility(View.GONE);
        rbmListView.setVisibility(View.GONE);

        rbmListView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.rbm_out_to_left));
    }


    public void toggleMenu() {
        if (rbmOutsideView.getVisibility() == View.GONE) {
            showMenu();
        } else {
            hideMenu();
        }
    }

    private String resourceIdToString(String text) {
        if (!text.contains("@")) {
            return text;
        } else {
            String id = text.replace("@", "");
            return getResources().getString(Integer.valueOf(id));
        }
    }


    public boolean isMenuVisible() {
        return rbmOutsideView.getVisibility() == View.VISIBLE;
    }


    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        if (ss.bShowMenu)
            showMenu();
        else
            hideMenu();
    }


    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.bShowMenu = isMenuVisible();
        return ss;
    }

    static class SavedState extends BaseSavedState {
        boolean bShowMenu;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            bShowMenu = (in.readInt() == 1);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(bShowMenu ? 1 : 0);
        }

        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
