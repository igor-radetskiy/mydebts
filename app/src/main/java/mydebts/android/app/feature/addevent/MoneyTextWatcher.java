package mydebts.android.app.feature.addevent;

import android.text.Editable;
import android.text.TextWatcher;

public class MoneyTextWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s != null && s.length() > 0 && s.charAt(0) != '$') {
            s.insert(0, "$");
        }
    }
}
