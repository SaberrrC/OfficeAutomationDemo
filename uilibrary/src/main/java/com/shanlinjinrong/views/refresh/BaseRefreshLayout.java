package com.shanlinjinrong.views.refresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.widget.TextView;

import com.shanlinjinrong.uilibrary.R;
import com.shanlinjinrong.utils.ScrollUtils;
import com.shanlinjinrong.views.refresh.indicator.RefreshIndicator;

public class BaseRefreshLayout extends ViewGroup {
    // 滑动状态
    public final static byte PTR_STATUS_INIT = 1; // 初始状态
    private byte mStatus = PTR_STATUS_INIT;
    public final static byte PTR_STATUS_PREPARE = 2;// 开始状态
    public final static byte PTR_STATUS_LOADING = 3;// 刷新状态
    public final static byte PTR_STATUS_COMPLETE = 4;// 完成状态


    // auto refresh status
    private final static byte FLAG_AUTO_REFRESH_AT_ONCE = 0x01;
    private final static byte FLAG_AUTO_REFRESH_BUT_LATER = 0x01 << 1;
    private final static byte FLAG_ENABLE_NEXT_PTR_AT_ONCE = 0x01 << 2;
    private final static byte FLAG_PIN_CONTENT = 0x01 << 3;
    private final static byte MASK_AUTO_REFRESH = 0x03;
    protected View mContent;
    // optional config for define header and content in xml file
    private int mHeaderId = 0;
    private int mContainerId = 0;
    // config
    private int mDurationToClose = 200;
    private int mDurationToCloseHeader = 1000;
    private boolean mKeepHeaderWhenRefresh = true;
    private boolean mPullToRefresh = false;
    private View mHeaderView;


    private HeaderRefreshListener mHeaderRefreshListener;
    private OnRefreshListener mRefreshListener;
    // working parameters
    private ScrollChecker mScrollChecker;
    private int mPagingTouchSlop;
    private int mHeaderHeight;
    private boolean mDisableWhenHorizontalMove = false;
    private int mFlag = 0x00;

    // disable when detect moving horizontally
    private boolean mPreventForHorizontal = false;

    private MotionEvent mLastMoveEvent;

    private int mLoadingMinTime = 500;
    private long mLoadingStartTime = 0;
    private RefreshIndicator mIndicator;
    private boolean mHasSendCancelEvent = false;

    private int mFingerCount = 0;//按下的手指数
    private boolean mFingerMode = false;//多手指状态


    private Runnable mPerformRefreshCompleteDelay = new Runnable() {
        @Override
        public void run() {
            performRefreshComplete();
        }
    };

    public BaseRefreshLayout(Context context) {
        this(context, null);
    }

    public BaseRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mIndicator = new RefreshIndicator();

        initAttr(context, attrs);

        mScrollChecker = new ScrollChecker();

        final ViewConfiguration conf = ViewConfiguration.get(getContext());
        mPagingTouchSlop = conf.getScaledTouchSlop() * 2;

    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.BaseRefreshLayout, 0, 0);
        if (arr != null) {
            mHeaderId = arr.getResourceId(R.styleable.BaseRefreshLayout_header, mHeaderId);
            mContainerId = arr.getResourceId(R.styleable.BaseRefreshLayout_content, mContainerId);

            mIndicator.setResistance(
                    arr.getFloat(R.styleable.BaseRefreshLayout_resistance, mIndicator.getResistance()));

            mDurationToClose = arr.getInt(R.styleable.BaseRefreshLayout_duration_to_close, mDurationToClose);
            mDurationToCloseHeader = arr.getInt(R.styleable.BaseRefreshLayout_duration_to_close_header, mDurationToCloseHeader);

            float ratio = mIndicator.getRatioOfHeaderToHeightRefresh();
            ratio = arr.getFloat(R.styleable.BaseRefreshLayout_ratio_of_header_height_to_refresh, ratio);
            mIndicator.setRatioOfHeaderHeightToRefresh(ratio);

            mKeepHeaderWhenRefresh = arr.getBoolean(R.styleable.BaseRefreshLayout_keep_header_when_refresh, mKeepHeaderWhenRefresh);

            mPullToRefresh = arr.getBoolean(R.styleable.BaseRefreshLayout_pull_to_fresh, mPullToRefresh);
            arr.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        final int childCount = getChildCount();
        if (childCount > 2) {
            throw new IllegalStateException("PtrFrameLayout can only contains 2 children");
        } else if (childCount == 2) {
            if (mHeaderId != 0 && mHeaderView == null) {
                mHeaderView = findViewById(mHeaderId);
            }
            if (mContainerId != 0 && mContent == null) {
                mContent = findViewById(mContainerId);
            }

            // not specify header or content
            if (mContent == null || mHeaderView == null) {

                View child1 = getChildAt(0);
                View child2 = getChildAt(1);
                if (child1 instanceof HeaderRefreshListener) {
                    mHeaderView = child1;
                    mContent = child2;
                } else if (child2 instanceof HeaderRefreshListener) {
                    mHeaderView = child2;
                    mContent = child1;
                } else {
                    // both are not specified
                    if (mContent == null && mHeaderView == null) {
                        mHeaderView = child1;
                        mContent = child2;
                    }
                    // only one is specified
                    else {
                        if (mHeaderView == null) {
                            mHeaderView = mContent == child1 ? child2 : child1;
                        } else {
                            mContent = mHeaderView == child1 ? child2 : child1;
                        }
                    }
                }
            }
        } else if (childCount == 1) {
            mContent = getChildAt(0);
        } else {
            TextView errorView = new TextView(getContext());
            errorView.setClickable(true);
            errorView.setTextColor(0xffff6600);
            errorView.setGravity(Gravity.CENTER);
            errorView.setTextSize(20);
            errorView.setText("The content view in PtrFrameLayout is empty. Do you forget to specify its id in xml layout file?");
            mContent = errorView;
            addView(mContent);
        }
        if (mHeaderView != null) {
            mHeaderView.bringToFront();
        }
        super.onFinishInflate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mScrollChecker != null) {
            mScrollChecker.destroy();
        }

        if (mPerformRefreshCompleteDelay != null) {
            removeCallbacks(mPerformRefreshCompleteDelay);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mHeaderView != null) {
            measureChildWithMargins(mHeaderView, widthMeasureSpec, 0, heightMeasureSpec, 0);
            MarginLayoutParams lp = (MarginLayoutParams) mHeaderView.getLayoutParams();
            mHeaderHeight = mHeaderView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            mIndicator.setHeaderHeight(mHeaderHeight);
        }

        if (mContent != null) {
            measureContentView(mContent, widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void measureContentView(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
                getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin, lp.width);
        int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
                getPaddingTop() + getPaddingBottom() + lp.topMargin, lp.height);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean flag, int i, int j, int k, int l) {
        layoutChildren();
    }

    private void layoutChildren() {
        int offset = mIndicator.getCurrentPosY();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        if (mHeaderView != null) {
            MarginLayoutParams lp = (MarginLayoutParams) mHeaderView.getLayoutParams();
            int left = paddingLeft + lp.leftMargin;
            // enhance readability(header is layout above screen when first init)
            int top = -(mHeaderHeight - paddingTop - lp.topMargin - offset);
            int right = left + mHeaderView.getMeasuredWidth();
            int bottom = top + mHeaderView.getMeasuredHeight();
            mHeaderView.layout(left, top, right, bottom);
        }
        if (mContent != null) {
            if (isPinContent()) {
                offset = 0;
            }
            MarginLayoutParams lp = (MarginLayoutParams) mContent.getLayoutParams();
            int left = paddingLeft + lp.leftMargin;
            int top = paddingTop + lp.topMargin + offset;
            int right = left + mContent.getMeasuredWidth();
            int bottom = top + mContent.getMeasuredHeight();
            mContent.layout(left, top, right, bottom);
        }
    }


    public boolean dispatchTouchEventSupper(MotionEvent e) {
        return super.dispatchTouchEvent(e);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        if (!isEnabled() || mContent == null || mHeaderView == null) {
            return dispatchTouchEventSupper(e);
        }
        int action = e.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mFingerCount = 0;
                mIndicator.onRelease();
                if (mIndicator.hasLeftStartPosition()) {
                    onRelease(false);
                    if (mIndicator.hasMovedAfterPressedDown()) {
                        sendCancelEvent();
                        return true;
                    }
                    return dispatchTouchEventSupper(e);
                } else {
                    return dispatchTouchEventSupper(e);
                }
            case MotionEvent.ACTION_DOWN:
                mFingerCount = 1;
                mHasSendCancelEvent = false;
                mIndicator.onPressDown(e.getX(), e.getY());
                mScrollChecker.abortIfWorking();
                mPreventForHorizontal = false;
                // The cancel event will be sent once the position is moved.
                // So let the event pass to children.
                // fix #93, #102
                dispatchTouchEventSupper(e);
                return true;
            case MotionEvent.ACTION_POINTER_DOWN:
                mFingerCount += 1;
                mFingerMode = true;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mFingerCount -= 1;
                break;
            case MotionEvent.ACTION_MOVE:
                //多手指的时候不处理滑动操作
                if (mFingerCount > 1) {
                    break;
                }
                //多手指，抬起只剩下最后一个的时候，只保存位置，便于下次继续从次高度计算开始滑动
                if (mFingerCount == 1 && mFingerMode) {
                    mFingerMode = false;
                    mIndicator.setPtLastMove(e.getX(), e.getY());
                    break;
                }
                mLastMoveEvent = e;
                mIndicator.onMove(e.getX(), e.getY());

                float offsetX = mIndicator.getOffsetX();
                float offsetY = mIndicator.getOffsetY();
                if (mDisableWhenHorizontalMove && !mPreventForHorizontal && (Math.abs(offsetX) > mPagingTouchSlop && Math.abs(offsetX) > Math.abs(offsetY))) {
                    if (mIndicator.isInStartPosition()) {
                        mPreventForHorizontal = true;
                    }
                }
                if (mPreventForHorizontal) {
                    return dispatchTouchEventSupper(e);
                }
                boolean moveDown = offsetY > 0;
                boolean moveUp = !moveDown;
                boolean canMoveUp = mIndicator.hasLeftStartPosition();

                // disable move when header not reach top
                if (moveDown && ScrollUtils.canChildScrollUp(mContent)) {
                    return dispatchTouchEventSupper(e);
                }

                if ((moveUp && canMoveUp) || moveDown) {
                    movePos(offsetY);
                    return true;
                }
        }
        return dispatchTouchEventSupper(e);
    }

    /**
     * if deltaY > 0, move the content down
     *
     * @param deltaY 偏移量
     */
    private void movePos(float deltaY) {
        // has reached the top
        if ((deltaY < 0 && mIndicator.isInStartPosition())) {
            return;
        }

        int to = mIndicator.getCurrentPosY() + (int) deltaY;

        // over top
        if (mIndicator.willOverTop(to)) {
            to = RefreshIndicator.POS_START;
        }

        mIndicator.setCurrentPos(to);
        int change = to - mIndicator.getLastPosY();
        updatePos(change);
    }

    private void updatePos(int change) {
        if (change == 0) {
            return;
        }

        boolean isUnderTouch = mIndicator.isUnderTouch();

        // once moved, cancel event will be sent to child
        if (isUnderTouch && !mHasSendCancelEvent && mIndicator.hasMovedAfterPressedDown()) {
            mHasSendCancelEvent = true;
            sendCancelEvent();
        }

        // leave initiated position or just refresh complete
        if ((mIndicator.hasJustLeftStartPosition() && mStatus == PTR_STATUS_INIT) ||
                (mIndicator.goDownCrossFinishPosition() && mStatus == PTR_STATUS_COMPLETE)) {

            mStatus = PTR_STATUS_PREPARE;
            mHeaderRefreshListener.onRefreshPrepare(this);
        }

        // back to initiated position
        if (mIndicator.hasJustBackToStartPosition()) {
            tryToNotifyReset();

            // recover event to children
            if (isUnderTouch) {
                sendDownEvent();
            }
        }

        // Pull to Refresh
        if (mStatus == PTR_STATUS_PREPARE) {
            // reach fresh height while moving from top to bottom
            if (isUnderTouch && !isAutoRefresh() && mPullToRefresh
                    && mIndicator.crossRefreshLineFromTopToBottom()) {
                tryToPerformRefresh();
            }
            // reach header height while auto refresh
            if (performAutoRefreshButLater() && mIndicator.hasJustReachedHeaderHeightFromTopToBottom()) {
                tryToPerformRefresh();
            }
        }


        mHeaderView.offsetTopAndBottom(change);
        if (!isPinContent()) {
            mContent.offsetTopAndBottom(change);
        }
        invalidate();

        if (mHeaderRefreshListener != null) {
            mHeaderRefreshListener.onPositionChange(this, isUnderTouch, mStatus, mIndicator);
        }
    }


    @SuppressWarnings("unused")
    public int getHeaderHeight() {
        return mHeaderHeight;
    }

    private void onRelease(boolean stayForLoading) {
        tryToPerformRefresh();
        if (mStatus == PTR_STATUS_LOADING) {
            // keep header for fresh
            if (mKeepHeaderWhenRefresh) {
                // scroll header back
                if (mIndicator.isOverOffsetToKeepHeaderWhileLoading() && !stayForLoading) {
                    mScrollChecker.tryToScrollTo(mIndicator.getOffsetToKeepHeaderWhileLoading(), mDurationToClose);
                }
            } else {
                tryScrollBackToTopWhileLoading();
            }
        } else {
            if (mStatus == PTR_STATUS_COMPLETE) {
                notifyUIRefreshComplete();
            } else {
                tryScrollBackToTopAbortRefresh();
            }
        }
    }


    /**
     * Scroll back to to if is not under touch
     */
    private void tryScrollBackToTop() {
        if (!mIndicator.isUnderTouch()) {
            mScrollChecker.tryToScrollTo(RefreshIndicator.POS_START, mDurationToCloseHeader);
        }
    }

    /**
     * just make easier to understand
     */
    private void tryScrollBackToTopWhileLoading() {
        tryScrollBackToTop();
    }

    /**
     * just make easier to understand
     */
    private void tryScrollBackToTopAfterComplete() {
        tryScrollBackToTop();
    }

    /**
     * just make easier to understand
     */
    private void tryScrollBackToTopAbortRefresh() {
        tryScrollBackToTop();
    }

    private boolean tryToPerformRefresh() {
        if (mStatus != PTR_STATUS_PREPARE) {
            return false;
        }

        if ((mIndicator.isOverOffsetToKeepHeaderWhileLoading() && isAutoRefresh()) || mIndicator.isOverOffsetToRefresh()) {
            mStatus = PTR_STATUS_LOADING;
            performRefresh();
        }
        return false;
    }

    private void performRefresh() {
        mLoadingStartTime = System.currentTimeMillis();
        if (mHeaderRefreshListener != null) {
            //头部显示加载中样式
            mHeaderRefreshListener.onRefreshBegin(this);
            if (mRefreshListener != null) {
                //通知ui开始刷新数据
                mRefreshListener.onStartRefresh(this);
            }
        }

    }

    /**
     * If at the top and not in loading, reset
     */
    private boolean tryToNotifyReset() {
        if ((mStatus == PTR_STATUS_COMPLETE || mStatus == PTR_STATUS_PREPARE) && mIndicator.isInStartPosition()) {
            if (mHeaderRefreshListener != null) {
                mHeaderRefreshListener.onHeaderReset(this);
            }
            mStatus = PTR_STATUS_INIT;
            clearFlag();
            return true;
        }
        return false;
    }

    protected void onPtrScrollAbort() {
        if (mIndicator.hasLeftStartPosition() && isAutoRefresh()) {
            onRelease(true);
        }
    }

    protected void onPtrScrollFinish() {
        if (mIndicator.hasLeftStartPosition() && isAutoRefresh()) {
            onRelease(true);
        }
    }

    /**
     * Detect whether is refreshing.
     */
    public boolean isRefreshing() {
        return mStatus == PTR_STATUS_LOADING;
    }

    /**
     * Call this when data is loaded.
     * The UI will perform complete at once or after a delay, depends on the time elapsed is greater then {@link #mLoadingMinTime} or not.
     */
    final public void refreshComplete() {

        int delay = (int) (mLoadingMinTime - (System.currentTimeMillis() - mLoadingStartTime));
        if (delay <= 0) {
            performRefreshComplete();
        } else {
            postDelayed(mPerformRefreshCompleteDelay, delay);
        }
    }

    /**
     * Do refresh complete work when time elapsed is greater than {@link #mLoadingMinTime}
     */
    private void performRefreshComplete() {
        mStatus = PTR_STATUS_COMPLETE;

        // if is auto refresh do nothing, wait scroller stop
        if (mScrollChecker.mIsRunning && isAutoRefresh()) {
            // do nothing
            return;
        }
        notifyUIRefreshComplete();
    }

    /**
     * Do real refresh work. If there is a hook, execute the hook first.
     **/
    private void notifyUIRefreshComplete() {
        if (mHeaderRefreshListener != null) {
            mHeaderRefreshListener.onRefreshComplete(this);
        }
        mIndicator.onUIRefreshComplete();
        tryScrollBackToTopAfterComplete();
        tryToNotifyReset();
    }

    public void autoRefresh() {
        autoRefresh(true, mDurationToCloseHeader);
    }

    public void autoRefresh(boolean atOnce) {
        autoRefresh(atOnce, mDurationToCloseHeader);
    }

    private void clearFlag() {
        // remove auto fresh flag
        mFlag = mFlag & ~MASK_AUTO_REFRESH;
    }

    public void autoRefresh(boolean atOnce, int duration) {

        if (mStatus != PTR_STATUS_INIT) {
            return;
        }

        mFlag |= atOnce ? FLAG_AUTO_REFRESH_AT_ONCE : FLAG_AUTO_REFRESH_BUT_LATER;

        mStatus = PTR_STATUS_PREPARE;
        if (mHeaderRefreshListener != null) {
            mHeaderRefreshListener.onRefreshPrepare(this);
        }
        mScrollChecker.tryToScrollTo(mIndicator.getOffsetToRefresh(), duration);
        if (atOnce) {
            mStatus = PTR_STATUS_LOADING;
            performRefresh();
        }
    }

    public boolean isAutoRefresh() {
        return (mFlag & MASK_AUTO_REFRESH) > 0;
    }

    private boolean performAutoRefreshButLater() {
        return (mFlag & MASK_AUTO_REFRESH) == FLAG_AUTO_REFRESH_BUT_LATER;
    }

    public boolean isPinContent() {
        return (mFlag & FLAG_PIN_CONTENT) > 0;
    }

    /**
     * The content view will now move when {@param pinContent} set to true.
     **/
    public void setPinContent(boolean pinContent) {
        if (pinContent) {
            mFlag = mFlag | FLAG_PIN_CONTENT;
        } else {
            mFlag = mFlag & ~FLAG_PIN_CONTENT;
        }
    }

    /**
     * It's useful when working with viewpager.
     **/
    public void disableWhenHorizontalMove(boolean disable) {
        mDisableWhenHorizontalMove = disable;
    }

    /**
     * loading will last at least for so long
     **/
    public void setLoadingMinTime(int time) {
        mLoadingMinTime = time;
    }


    @SuppressWarnings({"unused"})
    public View getContentView() {
        return mContent;
    }

    public void setCheckRefresh(OnRefreshListener mCheckRefresh) {
        this.mRefreshListener = mCheckRefresh;
    }

    public void setIndicator(RefreshIndicator slider) {
        if (mIndicator != null && mIndicator != slider) {
            slider.convertFrom(mIndicator);
        }
        mIndicator = slider;
    }

    @SuppressWarnings({"unused"})
    public float getResistance() {
        return mIndicator.getResistance();
    }

    public void setResistance(float resistance) {
        mIndicator.setResistance(resistance);
    }

    @SuppressWarnings({"unused"})
    public float getDurationToClose() {
        return mDurationToClose;
    }

    /**
     * The duration to return back to the refresh position
     */
    public void setDurationToClose(int duration) {
        mDurationToClose = duration;
    }

    @SuppressWarnings({"unused"})
    public long getDurationToCloseHeader() {
        return mDurationToCloseHeader;
    }

    /**
     * The duration to close time
     */
    public void setDurationToCloseHeader(int duration) {
        mDurationToCloseHeader = duration;
    }

    public void setRatioOfHeaderHeightToRefresh(float ratio) {
        mIndicator.setRatioOfHeaderHeightToRefresh(ratio);
    }

    public int getOffsetToRefresh() {
        return mIndicator.getOffsetToRefresh();
    }

    @SuppressWarnings({"unused"})
    public void setOffsetToRefresh(int offset) {
        mIndicator.setOffsetToRefresh(offset);
    }

    @SuppressWarnings({"unused"})
    public float getRatioOfHeaderToHeightRefresh() {
        return mIndicator.getRatioOfHeaderToHeightRefresh();
    }

    @SuppressWarnings({"unused"})
    public int getOffsetToKeepHeaderWhileLoading() {
        return mIndicator.getOffsetToKeepHeaderWhileLoading();
    }

    @SuppressWarnings({"unused"})
    public void setOffsetToKeepHeaderWhileLoading(int offset) {
        mIndicator.setOffsetToKeepHeaderWhileLoading(offset);
    }

    @SuppressWarnings({"unused"})
    public boolean isKeepHeaderWhenRefresh() {
        return mKeepHeaderWhenRefresh;
    }

    public void setKeepHeaderWhenRefresh(boolean keepOrNot) {
        mKeepHeaderWhenRefresh = keepOrNot;
    }

    public boolean isPullToRefresh() {
        return mPullToRefresh;
    }

    public void setPullToRefresh(boolean pullToRefresh) {
        mPullToRefresh = pullToRefresh;
    }

    @SuppressWarnings({"unused"})
    public View getHeaderView() {
        return mHeaderView;
    }

    public void setHeaderView(View header) {
        if (header == null)
            return;
        if (mHeaderView != null && mHeaderView != header) {
            removeView(mHeaderView);
        }
        ViewGroup.LayoutParams lp = header.getLayoutParams();
        if (lp == null) {
            lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            header.setLayoutParams(lp);
        }
        mHeaderView = header;
        addView(header);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p != null && p instanceof LayoutParams;
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    private void sendCancelEvent() {
        // The ScrollChecker will update position and lead to send cancel event when mLastMoveEvent is null.
        if (mLastMoveEvent == null) {
            return;
        }
        MotionEvent last = mLastMoveEvent;
        MotionEvent e = MotionEvent.obtain(last.getDownTime(), last.getEventTime() + ViewConfiguration.getLongPressTimeout(), MotionEvent.ACTION_CANCEL, last.getX(), last.getY(), last.getMetaState());
        dispatchTouchEventSupper(e);
    }

    private void sendDownEvent() {
        final MotionEvent last = mLastMoveEvent;
        MotionEvent e = MotionEvent.obtain(last.getDownTime(), last.getEventTime(), MotionEvent.ACTION_DOWN, last.getX(), last.getY(), last.getMetaState());
        dispatchTouchEventSupper(e);
    }

    public static class LayoutParams extends MarginLayoutParams {

        LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        LayoutParams(int width, int height) {
            super(width, height);
        }

        @SuppressWarnings({"unused"})
        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

    class ScrollChecker implements Runnable {

        private int mLastFlingY;
        private Scroller mScroller;
        private boolean mIsRunning = false;
        private int mStart;

        ScrollChecker() {
            mScroller = new Scroller(getContext());
        }

        public void run() {
            boolean finish = !mScroller.computeScrollOffset() || mScroller.isFinished();
            int curY = mScroller.getCurrY();
            int deltaY = curY - mLastFlingY;
            if (!finish) {
                mLastFlingY = curY;
                movePos(deltaY);
                post(this);
            } else {
                finish();
            }
        }

        private void finish() {
            reset();
            onPtrScrollFinish();
        }

        private void reset() {
            mIsRunning = false;
            mLastFlingY = 0;
            removeCallbacks(this);
        }

        private void destroy() {
            reset();
            if (!mScroller.isFinished()) {
                mScroller.forceFinished(true);
            }
        }

        void abortIfWorking() {
            if (mIsRunning) {
                if (!mScroller.isFinished()) {
                    mScroller.forceFinished(true);
                }
                onPtrScrollAbort();
                reset();
            }
        }

        void tryToScrollTo(int to, int duration) {
            if (mIndicator.isAlreadyHere(to)) {
                return;
            }
            mStart = mIndicator.getCurrentPosY();
            int distance = to - mStart;
            removeCallbacks(this);

            mLastFlingY = 0;

            if (!mScroller.isFinished()) {
                mScroller.forceFinished(true);
            }
            mScroller.startScroll(0, 0, 0, distance, duration);
            post(this);
            mIsRunning = true;
        }
    }

    public BaseRefreshLayout setRefreshUiListener(HeaderRefreshListener mRefreshUiListener) {
        this.mHeaderRefreshListener = mRefreshUiListener;
        return this;
    }
}
