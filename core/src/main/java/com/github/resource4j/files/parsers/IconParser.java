package com.github.resource4j.files.parsers;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.github.resource4j.files.ResourceFile;

public class IconParser extends AbstractValueParser<Icon> {

    private static final IconParser INSTANCE = new IconParser();

    public static IconParser getInstance() {
        return INSTANCE;
    }

    @Override
    public Icon parse(ResourceFile file) throws IOException, ResourceFileFormatException {
        BufferedImage image = ImageIO.read(file.asStream());
        if (image == null) {
        	throw new ResourceFileFormatException(file, "Unknown image format in file {0}");
        }
        return new ImageIcon(image);
    }

}
