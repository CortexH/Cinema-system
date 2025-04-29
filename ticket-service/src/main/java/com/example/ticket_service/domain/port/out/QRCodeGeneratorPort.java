package com.example.ticket_service.domain.port.out;

public interface QRCodeGeneratorPort {

    String generateQR(String code, int width, int height);
}
