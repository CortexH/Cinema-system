package com.example.ticket_service.infrastructure.adapter.outbound.utilities;

import com.example.ticket_service.domain.port.out.QRCodeGeneratorPort;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Component
public class QRCodeGeneratorPortAdapter implements QRCodeGeneratorPort {

    @Override
    public String generateQR(String code, int width, int height) {
        try{
            BitMatrix bitMatrix = new MultiFormatWriter().encode(code, BarcodeFormat.QR_CODE, width, height);

            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", outputStream);

            byte[] qrBytes = outputStream.toByteArray();

            return Base64.getEncoder().encodeToString(qrBytes);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
