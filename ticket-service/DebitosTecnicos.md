### Room gateway com falta de validação

Os dados retornados de Room Gateway estão sem validação alguma, que causa
o room gateway a não ser confiável.
Para resolver, basta apenas colocar validações de retorno de métodos http
(5xx ou algum retorno específico.)