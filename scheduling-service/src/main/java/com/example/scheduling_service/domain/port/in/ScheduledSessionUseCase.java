package com.example.scheduling_service.domain.port.in;

import com.example.scheduling_service.domain.model.Session;
import com.example.scheduling_service.domain.valueObject.SessionIdVO;

import java.util.List;
import java.util.Optional;

// essa classe interage com as entidades (sessões, objetos, etc), não com o scheduler!
public interface ScheduledSessionUseCase {

    /// Se `replace` for `false`, deixará uma lacuna onde era para ter a sessão
    /// removida. Se for `true`, as sessões posteriores preencherão o espaço em branco.
    /// No geral, a função serve para remover uma sessão específica das sessões.
    void removeAndReplaceScheduledSession(Boolean replace, List<SessionIdVO> sessionId);

    List<Session> findAllSessions(Integer limitDay);

    Optional<Session> insertNewSession(Session session);
    List<Session> findNowWorkingSessions();
}
