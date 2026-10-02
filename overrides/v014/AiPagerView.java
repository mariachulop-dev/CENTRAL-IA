package com.juanma.centralia;

import android.content.Context;
import android.graphics.Color;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AiPagerView extends LinearLayout {
    public interface TileFactory { View make(AiEntry entry); }
    public interface ToolFactory { View make(int index); }

    private static final int AI_PER_PAGE = 6;
    private final FrameLayout pageHost;
    private final LinearLayout dots;
    private final TextView pageText;
    private final TextView left;
    private final TextView right;
    private final GestureDetector gestures;
    private List<AiEntry> entries = new ArrayList<>();
    private TileFactory factory;
    private ToolFactory toolFactory;
    private int currentPage = 0;

    public AiPagerView(Context context) {
        super(context);
        setOrientation(VERTICAL);

        pageHost = new FrameLayout(context);
        addView(pageHost, new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f));

        LinearLayout nav = new LinearLayout(context);
        nav.setGravity(Gravity.CENTER);
        nav.setPadding(Ui.dp(context, 26), 0, Ui.dp(context, 26), 0);
        addView(nav, new LayoutParams(LayoutParams.MATCH_PARENT, Ui.dp(context, 42)));

        left = navButton(context, "‹");
        left.setOnClickListener(v -> previous());
        nav.addView(left, new LayoutParams(Ui.dp(context, 40), Ui.dp(context, 40)));

        LinearLayout center = new LinearLayout(context);
        center.setOrientation(VERTICAL);
        center.setGravity(Gravity.CENTER);
        nav.addView(center, new LayoutParams(0, LayoutParams.MATCH_PARENT, 1f));

        dots = new LinearLayout(context);
        dots.setGravity(Gravity.CENTER);
        dots.setOrientation(HORIZONTAL);
        center.addView(dots, new LayoutParams(LayoutParams.MATCH_PARENT, Ui.dp(context, 18)));

        pageText = Ui.text(context, "PÁGINA 1 DE 1", 10.5f, Color.rgb(205, 218, 242), false);
        pageText.setGravity(Gravity.CENTER);
        center.addView(pageText, new LayoutParams(LayoutParams.MATCH_PARENT, Ui.dp(context, 22)));

        right = navButton(context, "›");
        right.setOnClickListener(v -> next());
        nav.addView(right, new LayoutParams(Ui.dp(context, 40), Ui.dp(context, 40)));

        gestures = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onDown(MotionEvent e) { return true; }
            @Override public boolean onFling(MotionEvent e1, MotionEvent e2, float vx, float vy) {
                if (e1 == null || e2 == null) return false;
                float dx = e2.getX() - e1.getX();
                if (Math.abs(dx) < Ui.dp(getContext(), 45)) return false;
                if (dx < 0) next(); else previous();
                return true;
            }
        });
        pageHost.setOnTouchListener((v, event) -> gestures.onTouchEvent(event));
    }

    private TextView navButton(Context c, String glyph) {
        TextView t = Ui.text(c, glyph, 30, Color.rgb(0, 232, 255), true);
        t.setGravity(Gravity.CENTER);
        t.setBackground(Ui.rounded(Color.argb(218, 3, 15, 31), Color.rgb(0, 232, 255), 22, 1, c));
        return t;
    }

    public void setData(List<AiEntry> entries, TileFactory factory, ToolFactory tools) {
        this.entries = entries == null ? new ArrayList<>() : entries;
        this.factory = factory;
        this.toolFactory = tools;
        if (currentPage >= pageCount()) currentPage = Math.max(0, pageCount() - 1);
        render();
    }

    public void firstPage() { currentPage = 0; render(); }
    public void next() { if (currentPage < pageCount() - 1) { currentPage++; render(); } }
    public void previous() { if (currentPage > 0) { currentPage--; render(); } }

    private int pageCount() {
        return Math.max(1, (int) Math.ceil(entries.size() / (double) AI_PER_PAGE));
    }

    private void render() {
        if (factory == null || toolFactory == null) return;
        pageHost.removeAllViews();
        GridLayout grid = new GridLayout(getContext());
        grid.setColumnCount(3);
        grid.setRowCount(3);
        grid.setUseDefaultMargins(false);
        pageHost.addView(grid, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        int start = currentPage * AI_PER_PAGE;
        for (int slot = 0; slot < AI_PER_PAGE; slot++) {
            int idx = start + slot;
            View v = idx < entries.size() ? factory.make(entries.get(idx)) : new Space(getContext());
            addCell(grid, v, slot / 3, slot % 3);
        }

        addCell(grid, toolFactory.make(0), 2, 0);
        addCell(grid, toolFactory.make(1), 2, 1);
        addCell(grid, toolFactory.make(2), 2, 2);

        dots.removeAllViews();
        int pages = pageCount();
        for (int i = 0; i < pages; i++) {
            TextView dot = Ui.text(getContext(), "●", 11.5f,
                    i == currentPage ? Color.rgb(0,232,255) : Color.rgb(50,78,120), false);
            dot.setGravity(Gravity.CENTER);
            dots.addView(dot, new LayoutParams(Ui.dp(getContext(), 18), Ui.dp(getContext(), 18)));
        }
        pageText.setText("PÁGINA " + (currentPage + 1) + " DE " + pages);
        left.setAlpha(currentPage == 0 ? .28f : 1f);
        right.setAlpha(currentPage >= pages - 1 ? .28f : 1f);
    }

    private void addCell(GridLayout grid, View v, int row, int col) {
        GridLayout.LayoutParams gp = new GridLayout.LayoutParams();
        gp.width = 0;
        gp.height = 0;
        gp.columnSpec = GridLayout.spec(col, 1, 1f);
        gp.rowSpec = GridLayout.spec(row, 1, 1f);
        int m = Ui.dp(getContext(), 4);
        gp.setMargins(m, m, m, m);
        grid.addView(v, gp);
    }
}
