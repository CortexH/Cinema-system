package com.example.ticket_service.domain.valueObject;

public record QRCodeVO(String code) {

    public static QRCodeVO generate(String info){
        return new QRCodeVO(info);
    }

    public static QRCodeVO from(String info){
        return new QRCodeVO(info);
    }

}
