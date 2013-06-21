package com.github.resource4j.files;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

/**
 *
 * @author Ivan Gammel
 * @since 1.0
 */
public class Icons {

    private static final int DEFAULT_ICON_SIZE = 16/* px */;

    public static Icon emptyIcon() {
        return emptyIcon(DEFAULT_ICON_SIZE, DEFAULT_ICON_SIZE);
    }

    public static Icon emptyIcon(int width, int height) {
        return new EmptyIcon(width, height);
    }

    private static class EmptyIcon implements Icon {
        private int iconWidth;
        private int iconHeight;

        public EmptyIcon(int iconWidth, int iconHeight) {
            if (iconWidth < 0) {
                throw new IllegalArgumentException("iconWidth must be >= 0");
            }

            if (iconHeight < 0) {
                throw new IllegalArgumentException("iconHeight must be >= 0");
            }

            this.iconWidth = iconWidth;
            this.iconHeight = iconHeight;
        }

        @Override
        public int getIconWidth() {
            return iconWidth;
        }

        @Override
        public int getIconHeight() {
            return iconHeight;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            //Do nothing
        }
    }

}
