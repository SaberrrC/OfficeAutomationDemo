package com.shanlinjinrong.views.baserecycler.base;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import com.shanlinjinrong.views.baserecycler.cell.RVBasicCell;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用的RecyclerView.Adapter基类
 * 我们所有的列表只需要这么一个Adapter 就OK了，只需要添加Cell就行
 * Created by k.huang on 2017/2/20.
 */
// 2017/2/22 实现一个可以快速开发列表样式的fragment
public class RVBasicAdapter<C extends RVBasicCell> extends RecyclerView.Adapter<RVBasicViewHolder> {
    public static final String TAG = RVBasicAdapter.class.getSimpleName();
    protected List<C> mData; // 这里请注意，是Cell的集合，并不是数据源的集合

    public RVBasicAdapter(){
        mData = new ArrayList<>();
    }


    public void setDataCells(List<C> datacells){
        addAll(datacells);
        notifyDataSetChanged();
    }

    public List<C> getData() {
        return mData;
    }


    @Override
    public RVBasicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        for (int i = 0 ; i < getItemCount(); i++){
            if (viewType == mData.get(i).getItemType()){
                return mData.get(i).onCreateViewHolder(parent,viewType);
            }
        }
        throw new RuntimeException("Wrong viewType");
    }

    @Override
    public void onBindViewHolder(RVBasicViewHolder holder, int position) {
        mData.get(position).onBindViewHolder(holder,position);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getItemType();
    }

    @Override
    public void onViewDetachedFromWindow(RVBasicViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        Log.e(TAG,"onViewDetachedFromWindow invoke...");
        //释放资源
        int position = holder.getAdapterPosition();
        //越界检查
        if(position<0 || position>=mData.size()){
            return;
        }
        mData.get(position).releaseResource();
        holder.clear();
    }

    /**
     * add on cell
     * @param cell
     */
    public void add(C cell){
        mData.add(cell);
        int index = mData.indexOf(cell);
        notifyItemChanged(index);
    }

    public void add(int index,C cell){
        mData.add(index,cell);
        notifyItemChanged(index);
    }

    /**
     * remove a cell
     * @param cell
     */
    public void remove(C cell){
        int indexOfCell = mData.indexOf(cell);
        remove(indexOfCell);
    }

    public void remove(int index){
        mData.remove(index);
        notifyItemRemoved(index);
    }

    /**
     *
     * @param start
     * @param count
     */
    public void remove(int start,int count){
        if((start +count) > mData.size()){
            return;
        }
        int size = getItemCount();
        for(int i =start;i<size;i++){
            mData.remove(i);
        }

        notifyItemRangeRemoved(start,count);
    }

    /**
     * add a cell list
     * @param cells
     */
    public void addAll(List<C> cells){
        if(cells == null || cells.size() == 0){
            return;
        }
        Log.e(TAG,"addAll cell size:"+cells.size());
        mData.addAll(cells);
        notifyItemRangeChanged(mData.size()-cells.size(),mData.size());
    }

    public void addAll(int index,List<C> cells){
        if(cells == null || cells.size() == 0){
            return;
        }
        mData.addAll(index,cells);
        notifyItemRangeChanged(index,index+cells.size());
    }

    public void clear(){
        mData.clear();
        notifyDataSetChanged();
    }
}
