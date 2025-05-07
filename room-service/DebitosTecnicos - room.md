### Problema de queda

A ideia é criar uma forma de evitar problemas quando o sistema cair
no mesmo mento que o usuário estiver realizando a reserva de um assento.
No cenário atual, se o sistema de pagamento ou ticket cair enquanto
o usuário estiver realizando a reserva de um assento, há probabilidade
do assento ficar como reservado para sempre. E isso é um problema.

Para isso, foi pensado um sistema de 'hold', que é um estado semelhante
ao de reservado e livre. Se o sistema cair enquanto existe algum
assento em 'hold', quando o sistema voltar, ele deixa o assento como livre.

Enquanto o assento estiver como 'hold', para todos os outros services e
para o usuário em si, será apresentado como 'ocupado'.

### Ideia das salas

A ideia inicial era do 'admin' poder criar uma sala conforme as salas do cinema.
Mas, visto que alguém poderia alugar poltronas de uma sessão, as salas
não serão 'salas', e sim, sessões, que precisam do número da sala em si.

Dito isso, as rooms precisam ter a sua respectiva sala.