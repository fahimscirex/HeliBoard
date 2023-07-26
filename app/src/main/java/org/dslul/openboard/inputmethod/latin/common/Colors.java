package org.dslul.openboard.inputmethod.latin.common;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.ColorFilter;

import androidx.core.graphics.BlendModeColorFilterCompat;
import androidx.core.graphics.BlendModeCompat;

import org.dslul.openboard.inputmethod.keyboard.KeyboardTheme;
import org.dslul.openboard.inputmethod.latin.settings.Settings;

public class Colors {

    public final boolean isCustom; // todo: could be removed?
    public final int navBar;
    public final int accent;
    public final int background;
    public final int keyBackground;
    public final int functionalKey;
    public final int spaceBar;
    public final int keyText;
    public final int keyHintText;
    public ColorFilter backgroundFilter;
    public ColorFilter keyBackgroundFilter;
    public ColorFilter keyPressedBackgroundFilter;
    public ColorFilter functionalKeyBackgroundFilter;
    public ColorFilter functionalKeyPressedBackgroundFilter;
    public ColorFilter spaceBarFilter;
    public ColorFilter spaceBarPressedFilter;
    public ColorFilter keyTextFilter; // todo: really necessary?
    public ColorFilter keyHintTextFilter; // todo: really? color alone should be sufficient i think... test!
    public ColorFilter accentColorFilter; // todo: really necessary?
    public ColorFilter actionKeyIconColorFilter;

    public Colors(int acc, int bg, int k, int fun, int space, int kt, int kht) {
        isCustom = true;
        accent = acc;
        background = bg;
        keyBackground = k;
        functionalKey = fun;
        spaceBar = space;
        keyText = kt;
        keyHintText = kht;
        navBar = background;
    }

    public Colors(int themeId, int nightModeFlags) {
        isCustom = false;
        if (KeyboardTheme.getIsDayNight(themeId)) {
            if (nightModeFlags == Configuration.UI_MODE_NIGHT_NO)
                navBar = Color.rgb(236, 239, 241);
            else if (themeId == KeyboardTheme.THEME_ID_LXX_DARK)
                navBar = Color.rgb(38, 50, 56);
            else
                navBar = Color.BLACK;
        } else if (KeyboardTheme.THEME_VARIANT_LIGHT.equals(KeyboardTheme.getThemeVariant(themeId))) {
            navBar = Color.rgb(236, 239, 241);
        } else if (themeId == KeyboardTheme.THEME_ID_LXX_DARK) {
            navBar = Color.rgb(38, 50, 56);
        } else {
            // dark border is 13/13/13, but that's ok
            navBar = Color.BLACK;
        }
        accent = 0;
        background = 0;
        keyBackground = 0;
        functionalKey = 0;
        spaceBar = 0;
        keyText = 0;
        keyHintText = 0;
    }

    public void createColorFilters(final boolean hasKeyBorders) {
        backgroundFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(background, BlendModeCompat.MODULATE);
        if (hasKeyBorders) {
            keyBackgroundFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(keyBackground, BlendModeCompat.MODULATE);
            functionalKeyBackgroundFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(functionalKey, BlendModeCompat.MODULATE);
            spaceBarFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(spaceBar, BlendModeCompat.MODULATE);
            keyPressedBackgroundFilter = isDarkColor(keyBackground)
                    ? BlendModeColorFilterCompat.createBlendModeColorFilterCompat(keyBackground, BlendModeCompat.SCREEN)
                    : keyBackgroundFilter;
            functionalKeyPressedBackgroundFilter = isDarkColor(functionalKey)
                    ? BlendModeColorFilterCompat.createBlendModeColorFilterCompat(functionalKey, BlendModeCompat.SCREEN)
                    : functionalKeyBackgroundFilter;
            spaceBarPressedFilter = isDarkColor(spaceBar)
                    ? BlendModeColorFilterCompat.createBlendModeColorFilterCompat(spaceBar, BlendModeCompat.SCREEN)
                    : spaceBarFilter;
        } else {
            // need to set color to background if key borders are disabled, or there will be ugly keys
            keyBackgroundFilter = backgroundFilter;
            functionalKeyBackgroundFilter = backgroundFilter;
            spaceBarFilter = backgroundFilter;
            keyPressedBackgroundFilter = isDarkColor(background)
                    ? BlendModeColorFilterCompat.createBlendModeColorFilterCompat(background, BlendModeCompat.SCREEN)
                    : keyBackgroundFilter;
            functionalKeyPressedBackgroundFilter = keyBackgroundFilter;
            spaceBarPressedFilter = keyBackgroundFilter;
        }
        keyTextFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(keyText, BlendModeCompat.SRC_ATOP);
        keyHintTextFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(keyHintText, BlendModeCompat.SRC_ATOP);
        accentColorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(accent, BlendModeCompat.SRC_ATOP);
        actionKeyIconColorFilter = isBrightColor(accent) // the white icon may not have enough contrast, and can't be adjusted by the user
                ? BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.DKGRAY, BlendModeCompat.SRC_ATOP)
                : null;
    }

    public static boolean isBrightColor(int color) {
        if (android.R.color.transparent == color) {
            return true;
        }
        // See http://www.nbdtech.com/Blog/archive/2008/04/27/Calculating-the-Perceived-Brightness-of-a-Color.aspx
        int[] rgb = {Color.red(color), Color.green(color), Color.blue(color)};
        // we are only interested whether brightness is greater, so no need for sqrt
        int brightnessSquared = (int) (rgb[0] * rgb[0] * .241 + rgb[1] * rgb[1] * .691 + rgb[2] * rgb[2] * .068);
        return brightnessSquared >= 210*210;
    }

    public static boolean isDarkColor(int color) {
        if (android.R.color.transparent == color) {
            return true;
        }
        // See http://www.nbdtech.com/Blog/archive/2008/04/27/Calculating-the-Perceived-Brightness-of-a-Color.aspx
        int[] rgb = {Color.red(color), Color.green(color), Color.blue(color)};
        // we are only interested whether brightness is greater, so no need for sqrt
        int brightnessSquared = (int) (rgb[0] * rgb[0] * .241 + rgb[1] * rgb[1] * .691 + rgb[2] * rgb[2] * .068);
        return brightnessSquared < 50*50;
    }
}