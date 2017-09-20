package com.shanlinjinrong.views.listview.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by 丁 on 2017/9/14.
 * 缝隙分割线
 */
public class SpaceDecoration extends RecyclerView.ItemDecoration {

    private int space = 0;
    private int leftSpace = 0;
    private int rightSpace = 0;
    private int topSpace = 0;
    private int bottomSpace = 0;

    public SpaceDecoration(int space) {
        this.space = space;
    }

    public SpaceDecoration(int rightSpace, int leftSpace, int topSpace, int bottomSpace) {
        this.rightSpace = rightSpace;
        this.leftSpace = leftSpace;
        this.topSpace = topSpace;
        this.bottomSpace = bottomSpace;
    }

    public SpaceDecoration() {
        super();
    }

    public int getSpace() {
        return space;
    }

    public SpaceDecoration setSpace(int space) {
        this.space = space;
        return this;
    }

    public int getLeftSpace() {
        return leftSpace;
    }

    public SpaceDecoration setLeftSpace(int leftSpace) {
        this.leftSpace = leftSpace;
        return this;
    }

    public int getRightSpace() {
        return rightSpace;
    }

    public SpaceDecoration setRightSpace(int rightSpace) {
        this.rightSpace = rightSpace;
        return this;
    }

    public int getTopSpace() {
        return topSpace;
    }

    public SpaceDecoration setTopSpace(int topSpace) {
        this.topSpace = topSpace;
        return this;
    }

    public int getBottomSpace() {
        return bottomSpace;
    }

    public SpaceDecoration setBottomSpace(int bottomSpace) {
        this.bottomSpace = bottomSpace;
        return this;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (space == 0) {
            outRect.set(leftSpace, topSpace, rightSpace, bottomSpace);
        } else {
            outRect.set(space, space, space, space);
        }
    }

}
