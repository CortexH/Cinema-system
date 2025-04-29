package com.example.ticket_service.infrastructure.adapter.outbound.web;

import com.example.ticket_service.application.dto.internal.RealizePaymentDTO;
import com.example.ticket_service.domain.port.out.PaymentGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PaymentGatewayAdapter implements PaymentGateway {

    @Override
    public Boolean realizePayment(RealizePaymentDTO data) {
        return true;
    }

}
