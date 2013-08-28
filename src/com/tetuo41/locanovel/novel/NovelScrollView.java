package com.tetuo41.locanovel.novel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * ノベルデータのスクロール表示時の内部処理クラス
 * @author　HackathonG
 * @version 1.0
 * */
public class NovelScrollView extends ScrollView {

    public interface ScrollToBottomListener {
        void onScrollToBottom(NovelScrollView novelScrollView);
    }

    private ScrollToBottomListener scrollToBottomListener;
    private int scrollBottomMargin = 1000;

    public NovelScrollView(Context context) {
        super(context);
    }

    public NovelScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NovelScrollView(Context context, AttributeSet attrs, int defs) {
        super(context, attrs, defs);
    }

    public void setScrollToBottomListener(ScrollToBottomListener listener) {
        this.scrollToBottomListener = listener;
    }

    /**
     * 最下部より指定したピクセル数だけ手前まで
     * スクロールしたときにイベントを走らせる
     * */
    public void setScrollBottomMargin(int value) {
        this.scrollBottomMargin = value;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        View content = getChildAt(0);
        if (scrollToBottomListener == null) return;
        if (content == null) return;
        if (y + this.getHeight() >= content.getHeight() - scrollBottomMargin) {
            scrollToBottomListener.onScrollToBottom(this);
        }
    }
}
