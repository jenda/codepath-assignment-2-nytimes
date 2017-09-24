package com.codepath.nytimesseach.viewmodels;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.airbnb.epoxy.ModelView;
import com.codepath.nytimesseach.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jan_spidlen on 9/23/17.
 */

@ModelView(defaultLayout = R.layout.model_checkbox)
public class EpoxyCheckBox extends CheckBox//EpoxyModelWithHolder<EpoxyCheckBox.CheckBoxViewHolder> {
{
    public EpoxyCheckBox(@NonNull Context context) {
        super(context);
    }

    public EpoxyCheckBox(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EpoxyCheckBox(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


//    @Override
//    protected CheckBoxViewHolder createNewHolder() {
//        return null;
//    }
//
//    @Override
//    protected int getDefaultLayout() {
//        return 0;
//    }

    static class CheckBoxViewHolder extends EpoxyHolder {
        @BindView(R.id.checkBox)

        CheckBox checkBox;
        @CallSuper
        @Override
        protected void bindView(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
