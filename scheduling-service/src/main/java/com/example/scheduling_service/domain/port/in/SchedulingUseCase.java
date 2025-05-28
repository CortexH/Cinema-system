package com.example.scheduling_service.domain.port.in;

import com.example.scheduling_service.domain.valueObject.SessionQueueIdVO;

import java.util.List;
import java.util.UUID;

public interface SchedulingUseCase {

    /// Adiciona novas sessões conforme a quantidade de dias e as informações
    /// dos filmes, como a data para finalização, quantidades, limite de exibição, etc.
    void scheduleNextSessions(Integer days);

    /// Reestrutura as sessões conforme
    void rescheduleActualSessions();

    /// Realiza a função agendada de atualização nos status atuais do banco de dados,
    /// funções, etc. No geral, apaga ou atualiza sessões conforme o tempo na vida real.
    void runScheduledCheckout();

    /// Se 'replace' for false, deixará uma lacuna onde era para ter a sessão
    /// removida. Se for true, as sessões posteriores preencherão o espaço em branco.
    /// No geral, a função serve para remover uma sessão específica das sessões.
    void removeAndReplaceScheduledSession(Boolean replace, List<SessionQueueIdVO> sessionId);

}
