### Room gateway com falta de validação

Os dados retornados de Room Gateway estão sem validação alguma, que causa
o room gateway a não ser confiável.
Para resolver, basta apenas colocar validações de retorno de métodos http
(5xx ou algum retorno específico.)

em com.example.ticket_service.infrastructure.adapter.outbound.web.RoomGatewayAdapter

### Hold seats - problema

Ao invés de fazer uma requisição para validar se a poltrona pode ser ocupada,
faz mais sentido fazer uma requisição de ocupação (hold) da poltrona, e se não for
possível realizar a ocupação, retornará um erro.