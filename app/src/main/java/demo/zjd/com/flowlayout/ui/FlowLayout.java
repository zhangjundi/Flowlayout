package demo.zjd.com.flowlayout.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangjd on 2017/5/8.
 */

public class FlowLayout extends ViewGroup{
    private List<List<View>> listView=new ArrayList<>();
    private List<Integer> listLineHeight=new ArrayList<>();
    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        listView.clear();
        listLineHeight.clear();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth=0;
        int measuredHeight=0;
        int iWidthMode=MeasureSpec.getMode(widthMeasureSpec);
        int iWidthSize=MeasureSpec.getSize(widthMeasureSpec);
        int iHeightMode=MeasureSpec.getMode(heightMeasureSpec);
        int iHeightSize=MeasureSpec.getSize(heightMeasureSpec);
        if(iWidthMode==MeasureSpec.EXACTLY&&iHeightMode==MeasureSpec.EXACTLY){
            measuredWidth=iWidthSize;
            measuredHeight=iHeightSize;
        }else{
            int childCount = getChildCount();
            int iLineWidth=0;
            int iLineheight=0;
            int childMeasuredWidth;
            int childMeasuredHeight;
            List<View> list=new ArrayList<>();

            for(int i=0;i<childCount;i++){
                View childView = getChildAt(i);
                measureChild(childView,widthMeasureSpec,heightMeasureSpec);
                MarginLayoutParams params= (MarginLayoutParams) childView.getLayoutParams();
                childMeasuredWidth=childView.getMeasuredWidth()+params.leftMargin+params.rightMargin;
                childMeasuredHeight=childView.getMeasuredHeight()+params.topMargin+params.bottomMargin;
                int size=iLineWidth+childMeasuredWidth;
                if(size>iWidthSize||i==(childCount-1)){//进行换行
                    measuredWidth=Math.max(measuredWidth,iLineWidth);
                    measuredHeight=measuredHeight+iLineheight;
                    iLineWidth=childMeasuredWidth;
                    iLineheight=Math.max(iLineheight,childMeasuredHeight);
                    listLineHeight.add(iLineheight);
                    listView.add(list);
                    if(size>iWidthSize) {
                        list = new ArrayList<>();
                    }
                    list.add(childView);
                }else{
                    iLineWidth+=childMeasuredWidth;
                    iLineheight=Math.max(iLineheight,childMeasuredHeight);
                    list.add(childView);
                }
            }
        }
        setMeasuredDimension(measuredWidth,measuredHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int curleft=0;
        int curtop=0;
        for(int i=0;i<listView.size();i++){
            List<View> views = listView.get(i);
            for(int j=0;j<views.size();j++){
                View view = views.get(j);
                MarginLayoutParams params=(MarginLayoutParams)view.getLayoutParams();
                int left=curleft+params.leftMargin;
                int top=curtop+params.topMargin;
                int right=left+view.getMeasuredWidth()+params.rightMargin;
                int bottom=top+view.getMeasuredHeight()+params.bottomMargin;
                view.layout(left,top,right,bottom);
                curleft=right;
            }
            curleft=0;
            curtop+=listLineHeight.get(i);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
