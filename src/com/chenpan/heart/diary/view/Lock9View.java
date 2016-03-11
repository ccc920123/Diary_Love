/*
 * Copyright 2015-2016 TakWolf
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chenpan.heart.diary.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.chenpan.heart.diary.R;

/**
 * <p>Android-Lock9View</p>
 * <p>�Ź���������ͼ������ṩһ���Ź�������������ͼ���ܡ�</p>
 * <p>���ƴ�������߻��ƶ�������ڲ���ɣ���ֻ��Ҫ��������������������ɺ�Ļص����󼴿ɡ�</p>
 * <p>��ʹ�� setCallBack(CallBack callBack) �������ûص�����</p>
 * <p>��Ҫע����ǣ�����������ã�����ĸ߶���Զ���ڿ�ȡ�</p>
 * @author TakWolf (<a href="mailto:takwolf@foxmail.com">takwolf@foxmail.com</a>)
 */
public class Lock9View extends ViewGroup {

    private Paint paint;
    private Bitmap bitmap;
    private Canvas canvas;

    private List<Pair<NodeView, NodeView>> lineList;
    private NodeView currentNode;

    private StringBuilder pwdSb;
    private CallBack callBack;

    private Drawable nodeSrc;
    private Drawable nodeOnSrc;

    public Lock9View(Context context) {
        this(context, null);
    }

    public Lock9View(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Lock9View(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public Lock9View(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr); // TODO api 21
        initFromAttributes(attrs, defStyleAttr);
    }

    private void initFromAttributes(AttributeSet attrs, int defStyleAttr) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Lock9View, defStyleAttr, 0);

        nodeSrc = a.getDrawable(R.styleable.Lock9View_nodeSrc);
        nodeOnSrc = a.getDrawable(R.styleable.Lock9View_nodeOnSrc);
        int lineColor = Color.argb(0, 0, 0, 0);
        lineColor = a.getColor(R.styleable.Lock9View_lineColor, lineColor);
        float lineWidth = 20.0f;
        lineWidth = a.getDimension(R.styleable.Lock9View_lineWidth, lineWidth);

        a.recycle();


        paint = new Paint(Paint.DITHER_FLAG);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(lineWidth);
        paint.setColor(lineColor);
        paint.setAntiAlias(true);

        DisplayMetrics dm = getResources().getDisplayMetrics(); // bitmap�Ŀ������Ļ��ȣ��㹻ʹ��
        bitmap = Bitmap.createBitmap(dm.widthPixels, dm.widthPixels, Bitmap.Config.ARGB_8888);
        canvas = new Canvas();
        canvas.setBitmap(bitmap);

        for (int n = 0; n < 9; n++) {
            NodeView node = new NodeView(getContext(), n + 1);
            addView(node);
        }
        lineList = new ArrayList<Pair<NodeView,NodeView>>();
        pwdSb = new StringBuilder();

        // ���FLAG������ onDraw() ������ã�ԭ���� ViewGroup Ĭ��͸����������Ҫ���� onDraw()
        setWillNotDraw(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec); // �����ø߶ȵ��ڿ��
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (!changed) {
            return;
        }
        int width = right - left;
        int nodeWidth = width / 3;
        int nodePadding = nodeWidth / 6;
        for (int n = 0; n < 9; n++) {
            NodeView node = (NodeView) getChildAt(n);
            int row = n / 3;
            int col = n % 3;
            int l = col * nodeWidth + nodePadding;
            int t = row * nodeWidth + nodePadding;
            int r = col * nodeWidth + nodeWidth - nodePadding;
            int b = row * nodeWidth + nodeWidth - nodePadding;
            node.layout(l, t, r, b);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                NodeView nodeAt = getNodeAt(event.getX(), event.getY());
                if (nodeAt == null && currentNode == null) { // ����Ҫ���ߣ�֮ǰû�Ӵ��㣬��ǰҲû�Ӵ���
                    return true;
                } else { // ��Ҫ����
                    clearScreenAndDrawList(); // �������ͼ����������ߣ������»���
                    if (currentNode == null) { // ��һ���� nodeAt��Ϊnull
                        currentNode = nodeAt;
                        currentNode.setHighLighted(true);
                        pwdSb.append(currentNode.getNum());
                    }
                    else if (nodeAt == null || nodeAt.isHighLighted()) { // �Ѿ��е��ˣ���ǰ��δ�����µ�
                        // ��currentNode���ĺ͵�ǰ�����㿪ʼ����
                        canvas.drawLine(currentNode.getCenterX(), currentNode.getCenterY(), event.getX(), event.getY(), paint);
                    } else { // �ƶ����µ�
                        canvas.drawLine(currentNode.getCenterX(), currentNode.getCenterY(), nodeAt.getCenterX(), nodeAt.getCenterY(), paint);// ����
                        nodeAt.setHighLighted(true);
                        Pair<NodeView, NodeView> pair = new Pair<NodeView, NodeView>(currentNode, nodeAt);
                        lineList.add(pair);
                        // ��ֵ��ǰ��node
                        currentNode = nodeAt;
                        pwdSb.append(currentNode.getNum());
                    }
                    // ֪ͨonDraw�ػ�
                    invalidate();
                }
                return true;
            case MotionEvent.ACTION_UP:
                // ��û�д�������
                if (pwdSb.length() <= 0) {
                    return super.onTouchEvent(event);
                }
                // �ص����
                if (callBack != null) {
                    callBack.onFinish(pwdSb.toString());
                    pwdSb.setLength(0); // ���
                }
                // ��ձ����ļ���
                currentNode = null;
                lineList.clear();
                clearScreenAndDrawList();
                // �������
                for (int n = 0; n < getChildCount(); n++) {
                    NodeView node = (NodeView) getChildAt(n);
                    node.setHighLighted(false);
                }
                // ֪ͨonDraw�ػ�
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }

    /**
     * �����Ļ�����е��ߣ�Ȼ�󻭳������������
     */
    private void clearScreenAndDrawList() {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        for (Pair<NodeView, NodeView> pair : lineList) {
            canvas.drawLine(pair.first.getCenterX(), pair.first.getCenterY(), pair.second.getCenterX(), pair.second.getCenterY(), paint);
        }
    }

    /**
     * ��ȡNode������null��ʾ��ǰ��ָ������Node֮��
     */
    private NodeView getNodeAt(float x, float y) {
        for (int n = 0; n < getChildCount(); n++) {
            NodeView node = (NodeView) getChildAt(n);
            if (!(x >= node.getLeft() && x < node.getRight())) {
                continue;
            }
            if (!(y >= node.getTop() && y < node.getBottom())) {
                continue;
            }
            return node;
        }
        return null;
    }

    /**
     * �������ƽ���Ļص�������
     * @param callBack
     */
    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    /**
     * �ڵ�������
     */
    public class NodeView extends View {

        private int num;
        private boolean highLighted;

        private NodeView(Context context) {
            super(context);
        }

        @SuppressWarnings("deprecation")
		public NodeView(Context context, int num) {
            this(context);
            this.num = num;
            highLighted = false;
            if (nodeSrc == null) {
                setBackgroundResource(0);
            } else {
                setBackgroundDrawable(nodeSrc);
            }
        }

        public boolean isHighLighted() {
            return highLighted;
        }

        @SuppressWarnings("deprecation")
		public void setHighLighted(boolean highLighted) {
            this.highLighted = highLighted;
            if (highLighted) {
                if (nodeOnSrc == null) {
                    setBackgroundResource(0);
                } else {
                    setBackgroundDrawable(nodeOnSrc);
                }
            } else {
                if (nodeSrc == null) {
                    setBackgroundResource(0);
                } else {
                    setBackgroundDrawable(nodeSrc);
                }
            }
        }

        public int getCenterX() {
            return (getLeft() + getRight()) / 2;
        }

        public int getCenterY() {
            return (getTop() + getBottom()) / 2;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

    }

    /**
     * ����ص��������ӿ�
     */
    public interface CallBack {

        public void onFinish(String password);

    }

}
