## Sistema de gerenciamento de cinema (Cinema System)

## 🎬 Visão geral do projeto 
Este projeto tem como objetivo desenvolver um sistema de gerenciamento de cinema utilizando 
uma arquitetura de microsserviços. A ideia é simular diversas funcionalidades de um cinema
moderno, desde o catálogo de filmes e gerenciamento de sessões até a compra de ingressos,
autenticação de usuários e notificações.

O foco principal é o aprendizado e a aplicação de conceitos como:
- Arquitetura de microsserviços
- Domain-Driven Design (DDD)
- Arquitetura Hexagonal (Ports and Adapters)
- Princípios SOLID e DRY
- Comunicação Assíncrona com Apacha Kafka
- Spring Boot e Ecossistema Spring
- Testes (Por enquanto de integração, posteriormente unitários)
- Dockerização e Orquestração com Docker Compose (A ser implementado)

## 🛠️ Microsserviços planejados (e Status)

Atualmente, o sistema é composto pelos seguintes microsserviços:

1. **Room Service** - 🌟 **Protótipo Funcional**
   *  **Responsabilidade:** Gerenciar a disponibilidade de assentos para sessões de filmes agendadas. Mantém o estado de cada assento (Disponível, assegurado e comprado) para uma sessão específica.
   *  **Tecnologias:** Java, Spring Boot, Spring Data JPA (com H2 por enquanto), Spring Kafka (Produtor/Consumidor).

2. **Ticket Service** - 🌟 **Protótipo Funcional**
   *  **Responsabilidade:** Orquestrar o processo de compra de ingressos, gerenciar ciclo de vida dos tickets e a validação para entrada.
   *  **Tecnologias:** Java, Spring Boot, Spring Data JPA (com H2 por enquanto), Spring Kafka (Produtor/Consumidor), Chamadas API REST para outros serviços.

3. **Movie Service** - 🟢 **Em Desenvolvimento**
   *  **Responsabilidade:** Gerenciamento de catálogos de filmes (metadados, faixa etária, etc.).
   *  **Tecnologias planejadas:** Java, Spring Boot, Spring Security, Spring Data JPA, Spring Kafka (Produtor).

4. **User Service** - 🚧 A desenvolver
   *  **Responsabilidade:** Gerenciamento de usuários, autenticação, autorização (roles) e dados de perfil (incluindo verificação de idade).
   *  **Tecnologias planejadas:** Java, Spring Boot, Spring Security, Spring Data JPA, Spring Kafka (Produtor).

5. **Scheduling Service** - 🚧 A desenvolver
   *  **Responsabilidade:** Agendamento de sessões/exibições (conectando filme + sala + sessão + horário + preço).
   *  **Tecnologias planejadas:** Java, Spring Boot, Spring Data JPA, Spring Kafka (Produtor/Consumidor).

6. **Payment Service** - 🚧 A desenvolver
   *  **Responsabilidade:** Processar pagamentos (Integração com gateways fictícios) e gerenciar reembolsos.
   *  **Tecnologias planejadas:** Java, Spring Boot, Spring Kafka (Produtor/Consumidor).

7. **Notification Service** - 🚧 A desenvolver
   *  **Responsabilidade:** Enviar notificações aos usuários (via canais fictícios).
   *  **Tecnologias planejadas:** Java, Spring Boot, Spring Kafka (Consumidor).

8. **Analytics Service** - 🚧 A desenvolver
   *  **Responsabilidade:** Coletar dados de eventos para gerar relatórios e estatísticas.
   *  **Tecnologias planejadas:** java, Spring Boot, Spring Kafka (Consumidor), um banco de dados analítico.

9. **(Opcional) API Gateway** - 🔮 Futuro
   *  **Responsabilidade:** Ponto único de entrada para as requisições externas, lidando com roteamento, autenticação rate limiting, etc.

## 🏗️ Arquitetura e Design

*  **Comunicação síncrona:** Predominantemente via APIs REST para requisições que exisgem resposta imadiata (ex: `ticket-service` chamando `room-service` para reservar um assento).
*  **Comunicação assíncrona:** Utilização de Apache Kafka para a publicação e consumo de eventos de negócio, permitindo o desacoplamento e resiliência entre os serviços.
*  **Persistência:** Cada microsserviço terá o seu próprio banco de dados dedicado (Princípio "Database per Service"). A tecnologia de banco de dados pode variar por serviço conforme necessidade (ex: relacional para dados transacionais, NoSQL em alguns casos, etc).
*  **DDD & Hexagonal:** Busca por um domínio rico, separação clara entre o núcleo da aplicação e a infraestrutura (ports and adapters)

## 🎉 Como testar (Em breve)

1. **Necessário**
   *  **Aplicações:** Docker, Docker compose, Java 21 e alguma ferramenta de teste de API (e.g Postman)
2. **Como rodar**
   *  **Em andamento:** Ainda não é possível testar a aplicação, nenhuma atualização foi lançada ao docker hub, em breve estará lá!
