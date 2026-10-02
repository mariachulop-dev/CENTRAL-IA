package com.juanma.centralia;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NeonTile extends FrameLayout {
    private final ImageView icon;
    private final TextView glyph;
    private final TextView title;
    private final TextView subtitle;
    private final TextView menu;

    public NeonTile(Context context, int accent) {
        super(context);
        setBackground(Ui.rounded(Color.argb(224, 3, 15, 32), accent, 17, 1.4f, context));
        setClickable(true);
        setFocusable(true);
        setForeground(new android.graphics.drawable.RippleDrawable(
                android.content.res.ColorStateList.valueOf(0x33FFFFFF), null, null));

        LinearLayout content = new LinearLayout(context);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setGravity(Gravity.CENTER);
        int p = Ui.dp(context, 7);
        content.setPadding(p, Ui.dp(context, 7), p, Ui.dp(context, 6));
        addView(content, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        FrameLayout iconBox = new FrameLayout(context);
        LinearLayout.LayoutParams ibp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 1.24f);
        content.addView(iconBox, ibp);

        icon = new ImageView(context);
        icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        FrameLayout.LayoutParams ip = new FrameLayout.LayoutParams(Ui.dp(context, 58), Ui.dp(context, 58), Gravity.CENTER);
        iconBox.addView(icon, ip);

        glyph = Ui.text(context, "", 36, accent, true);
        glyph.setGravity(Gravity.CENTER);
        glyph.setVisibility(View.GONE);
        iconBox.addView(glyph, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        title = Ui.text(context, "", 15.5f, Color.WHITE, true);
        title.setGravity(Gravity.CENTER);
        title.setMaxLines(2);
        title.setHorizontallyScrolling(false);
        content.addView(title, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, .64f));

        subtitle = Ui.text(context, "", 10.8f, Color.rgb(207, 221, 246), false);
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setMaxLines(3);
        subtitle.setHorizontallyScrolling(false);
        content.addView(subtitle, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, .78f));

        menu = Ui.text(context, "⋮", 18, Color.rgb(176, 236, 255), true);
        menu.setGravity(Gravity.CENTER);
        menu.setVisibility(View.GONE);
        addView(menu, new FrameLayout.LayoutParams(0, 0));
    }

    public void setIcon(Drawable d) {
        icon.setVisibility(View.VISIBLE);
        glyph.setVisibility(View.GONE);
        icon.setImageDrawable(d);
    }

    public void setIconResource(int resId) {
        icon.setVisibility(View.VISIBLE);
        glyph.setVisibility(View.GONE);
        icon.setImageResource(resId);
    }

    public void setGlyph(String value, int color) {
        icon.setVisibility(View.GONE);
        glyph.setVisibility(View.VISIBLE);
        glyph.setText(value);
        glyph.setTextColor(color);
    }

    public void setTitle(String s) { title.setText(s); }
    public void setSubtitle(String s) { subtitle.setText(s); }
    public void setMenuVisible(boolean visible) { menu.setVisibility(View.GONE); }
    public void setMenuClickListener(View.OnClickListener l) { menu.setOnClickListener(l); }
}
