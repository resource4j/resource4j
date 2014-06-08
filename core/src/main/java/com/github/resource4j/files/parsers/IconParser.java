package com.github.resource4j.files.parsers;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class IconParser extends AbstractValueParser<Icon> {

    private static final IconParser INSTANCE = new IconParser();

    public static IconParser getInstance() {
        return INSTANCE;
    }

    @Override
    public Icon parse(InputStream stream) throws IOException {
        BufferedImage image = ImageIO.read(stream);
        return new ImageIcon(image);
    }

}
