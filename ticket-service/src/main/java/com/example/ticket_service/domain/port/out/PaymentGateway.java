package com.example.ticket_service.domain.port.out;

import com.example.ticket_service.application.dto.internal.RealizePaymentDTO;

public interface PaymentGateway {

    Boolean realizePayment(RealizePaymentDTO data);

}
