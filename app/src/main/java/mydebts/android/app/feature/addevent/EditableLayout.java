package mydebts.android.app.feature.addevent;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import mydebts.android.app.R;

public class EditableLayout extends FrameLayout {

    private TextView text;
    private EditText edit;
    private ImageView clear;

    public EditableLayout(@NonNull Context context) {
        super(context);
    }

    public EditableLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EditableLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public EditableLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        text = (TextView)findViewById(R.id.text);
        edit = (EditText)findViewById(R.id.edit);
        clear = (ImageView)findViewById(R.id.clear);

        text.setText(edit.getHint());
        text.setTypeface(null, Typeface.ITALIC);
        edit.setVisibility(GONE);
        clear.setVisibility(GONE);

        text.setOnClickListener(v -> {
            text.setVisibility(GONE);
            edit.setVisibility(VISIBLE);
            edit.requestFocus();
            clear.setVisibility(VISIBLE);
        });

        edit.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                edit.setVisibility(GONE);
                clear.setVisibility(GONE);
                text.setVisibility(VISIBLE);
            }
        });

        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || s.length() == 0) {
                    text.setText(edit.getHint());
                    text.setTypeface(null, Typeface.ITALIC);
                } else {
                    text.setText(s.toString());
                    text.setTypeface(null, Typeface.NORMAL);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        clear.setOnClickListener(v -> edit.getText().clear());
    }

    public EditText getEdit() {
        return edit;
    }
}
