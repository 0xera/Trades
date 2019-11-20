package ru.itbirds.trades.common;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import ru.itbirds.trades.R;

public class StockTextView extends AppCompatTextView {

    public StockTextView(Context context) {
        super(context);
    }

    public StockTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StockTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (!TextUtils.isEmpty(getText()) && !getText().equals("0.00")) {
            ObjectAnimator objectAnimator = null;
            if (Double.parseDouble(String.valueOf(getText())) > Double.parseDouble(String.valueOf(text))) {
                objectAnimator = ObjectAnimator.ofObject(this, "backgroundColor",
                        new ArgbEvaluator(),
                        ContextCompat.getColor(getContext(), R.color.red),
                        ContextCompat.getColor(getContext(), android.R.color.transparent));
            } else if (Double.parseDouble(String.valueOf(getText())) < Double.parseDouble(String.valueOf(text))) {
                objectAnimator = ObjectAnimator.ofObject(this, "backgroundColor",
                        new ArgbEvaluator(),
                        ContextCompat.getColor(getContext(), R.color.green),
                        ContextCompat.getColor(getContext(), android.R.color.transparent));
            }
            if (objectAnimator != null) {
                objectAnimator.setDuration(2000L);
                objectAnimator.start();
            }
        }

        super.setText(text, type);
    }

}
