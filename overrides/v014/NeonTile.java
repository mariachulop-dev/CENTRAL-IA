package com.juanma.centralia;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
        content.setPadding(p, Ui.dp(context, 7), p, Ui.dp(context, 7));
        addView(content, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        FrameLayout iconBox = new FrameLayout(context);
        LinearLayout.LayoutParams ibp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Ui.dp(context, 55));
        ibp.bottomMargin = Ui.dp(context, 2);
        content.addView(iconBox, ibp);

        icon = new ImageView(context);
        icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        FrameLayout.LayoutParams ip = new FrameLayout.LayoutParams(Ui.dp(context, 50), Ui.dp(context, 50), Gravity.CENTER);
        iconBox.addView(icon, ip);

        glyph = Ui.text(context, "", 36, accent, true);
        glyph.setGravity(Gravity.CENTER);
        glyph.setVisibility(View.GONE);
        iconBox.addView(glyph, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        title = Ui.text(context, "", 16.5f, Color.WHITE, true);
        title.setGravity(Gravity.CENTER);
        title.setMaxLines(2);
        title.setHorizontallyScrolling(false);
        LinearLayout.LayoutParams titleLp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        titleLp.topMargin = Ui.dp(context, 1);
        content.addView(title, titleLp);

        subtitle = Ui.text(context, "", 12.0f, Color.rgb(207, 221, 246), false);
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setMaxLines(2);
        subtitle.setHorizontallyScrolling(false);
        LinearLayout.LayoutParams subLp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        subLp.topMargin = Ui.dp(context, 2);
        content.addView(subtitle, subLp);

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
        icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        icon.setImageResource(resId);
    }

    public void setIconResourceCropped(int resId) {
        icon.setVisibility(View.VISIBLE);
        glyph.setVisibility(View.GONE);
        try {
            Bitmap src = BitmapFactory.decodeResource(getResources(), resId).copy(Bitmap.Config.ARGB_8888, true);
            int w = src.getWidth(), h = src.getHeight();
            int minX=w, minY=h, maxX=-1, maxY=-1;
            for (int y=0;y<h;y++) for (int x=0;x<w;x++) {
                int c=src.getPixel(x,y);
                int a=Color.alpha(c), r=Color.red(c), g=Color.green(c), b=Color.blue(c);
                if (a>0 && r>238 && g>238 && b>238) {
                    src.setPixel(x,y,Color.TRANSPARENT);
                } else if (Color.alpha(src.getPixel(x,y))>16) {
                    if(x<minX)minX=x; if(x>maxX)maxX=x; if(y<minY)minY=y; if(y>maxY)maxY=y;
                }
            }
            Bitmap out=src;
            if(maxX>=minX && maxY>=minY) {
                int pad=Math.max(2, Math.min(w,h)/30);
                int l=Math.max(0,minX-pad), t=Math.max(0,minY-pad);
                int rr=Math.min(w-1,maxX+pad), bb=Math.min(h-1,maxY+pad);
                out=Bitmap.createBitmap(src,l,t,rr-l+1,bb-t+1);
            }
            icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            icon.setImageDrawable(new BitmapDrawable(getResources(),out));
        } catch(Exception ex) {
            icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
            icon.setImageResource(resId);
        }
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
