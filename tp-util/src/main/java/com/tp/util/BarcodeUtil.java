package com.tp.util;

import net.sourceforge.jbarcodebean.JBarcodeBean;
import net.sourceforge.jbarcodebean.model.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by ldr on 2016/11/19.
 */
public class BarcodeUtil {

    public static ByteArrayOutputStream genBarcode(String val){

        JBarcodeBean barCodeBean = new JBarcodeBean();
        barCodeBean.setCodeType(new Code128());
        barCodeBean.setNarrowestBarWidth(4);
        barCodeBean.setBarcodeHeight(160);
       // barCodeBean.setHorizontalAlignment(1);
        barCodeBean.setCode(val);
        barCodeBean.setShowText(false);
        Dimension dimension = new Dimension();
        dimension.setSize(560D,170D);
        barCodeBean.setPreferredSize(dimension);
        BufferedImage image = new BufferedImage(570,170,2);
        barCodeBean.draw(image);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image,"png",outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    return outputStream;
    }



}
