package ru.itbirds.trades.common;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {
    private final int mColumnCount;

    public ItemOffsetDecoration(int columnCount) {
        this.mColumnCount = columnCount;

    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        int column = position % mColumnCount;
        int itemWidth = view.getLayoutParams().width;
        int parentWidth = parent.getWidth();
        int offset = (parentWidth - (itemWidth * mColumnCount)) / (mColumnCount + 1);
        outRect.left = offset - column * offset / mColumnCount;
        outRect.right = (column + 1) * offset / mColumnCount;
        outRect.bottom = offset;


    }
}