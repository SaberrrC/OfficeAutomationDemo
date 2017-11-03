package com.shanlinjinrong.views.listview;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

/**
 * Created by 丁 on 2017/9/15.
 */
public class SlideToDelete extends  ItemTouchHelper {

    /**
     * Creates an ItemTouchHelper that will work with the given Callback.
     * <p>
     * You can attach ItemTouchHelper to a RecyclerView via
     * {@link #attachToRecyclerView(RecyclerView)}. Upon attaching, it will add an item decoration,
     * an onItemTouchListener and a Child attach / detach listener to the RecyclerView.
     *
     * @param callback The Callback which controls the behavior of this touch helper.
     */
    public SlideToDelete(Callback callback) {
        super(callback);
    }

    @Override
    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) {
        super.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void onChildViewAttachedToWindow(View view) {
        super.onChildViewAttachedToWindow(view);
    }

    @Override
    public void onChildViewDetachedFromWindow(View view) {
        super.onChildViewDetachedFromWindow(view);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
    }

    @Override
    public void startDrag(RecyclerView.ViewHolder viewHolder) {
        super.startDrag(viewHolder);
    }

    @Override
    public void startSwipe(RecyclerView.ViewHolder viewHolder) {
        super.startSwipe(viewHolder);
    }
}
