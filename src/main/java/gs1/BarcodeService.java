package gs1;

import uk.org.okapibarcode.backend.Code128;
import uk.org.okapibarcode.backend.HumanReadableLocation;
import uk.org.okapibarcode.output.Java2DRenderer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class BarcodeService {

    public BufferedImage gs1_128(String content) { // npr. "[00]106141411234567897[10]LOT42"
        Code128 barcode = new Code128();
        barcode.setDataType(uk.org.okapibarcode.backend.Symbol.DataType.GS1);
        barcode.setHumanReadableLocation(HumanReadableLocation.BOTTOM);
        barcode.setContent(content);

        int mag = 3;
        BufferedImage img = new BufferedImage(
                barcode.getWidth() * mag, barcode.getHeight() * mag, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        new Java2DRenderer(g, mag,
                uk.org.okapibarcode.graphics.Color.WHITE,  // paper
                uk.org.okapibarcode.graphics.Color.BLACK)  // ink
                .render(barcode);
        g.dispose();
        return img;
    }
}
