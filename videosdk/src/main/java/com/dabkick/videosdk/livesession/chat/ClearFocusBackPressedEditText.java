package com.dabkick.videosdk.livesession.chat;


import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

public class ClearFocusBackPressedEditText extends android.support.v7.widget.AppCompatEditText {

    public ClearFocusBackPressedEditText(Context context ) {
        super(context);
    }

    public ClearFocusBackPressedEditText(Context context, AttributeSet attribute_set ) {
        super( context, attribute_set );
    }

    public ClearFocusBackPressedEditText(Context context, AttributeSet attribute_set, int def_style_attribute ) {
        super( context, attribute_set, def_style_attribute );
    }

    @Override
    public boolean onKeyPreIme( int key_code, KeyEvent event ) {
        if ( event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP ) {
            this.clearFocus();
        }
        return super.onKeyPreIme( key_code, event );
    }

}