package com.example.scheduling_service.domain.port.in;

// essa classe interage apenas com o scheduler, não com as sessões em si!
public interface SessionSchedulerUseCase {

    /// Adiciona novas sessões conforme a quantidade de dias e as informações
    /// dos filmes, como a data para finalização, quantidades, limite de exibição, etc.
    void scheduleNextSessions(Integer days);

    /// Reestrutura as sessões conforme a quantidade de sessões atuais e outros dados
    void rescheduleActualSessions();

    /// Realiza a função agendada de atualização nos status atuais do banco de dados,
    /// funções, etc. No geral, apaga ou atualiza sessões conforme o tempo na vida real.
    void runScheduledCheckout();


}
