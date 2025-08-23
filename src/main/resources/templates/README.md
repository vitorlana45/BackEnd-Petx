# Anotações

- O ${...} -> variavel (model/contexto)
  - lê atributos que voce coloca no Model(animal, statsCard) etc...
  - ex de uso:
  - th:text="${animal.nome}, th:each"s : ${statsCard}}


passanod o #{ ... } → mensagens i18n (messages.properties)
Busca uma chave no bundle de mensagens, com ou sem parâmetros.

Ex.: th:text="#{titulo.pagina}", th:text="#{saudacao(${usuario.nome})}".



@{ ... } → URLs (links com path/params/context)

Constrói URLs respeitando o contexto da app e adicionando params.
th:href="@{/animais/novo}"
th:href="@{/animais/{id}(id=${animal.id})}"
th:href="@{/animais(search=${filtro},page=${page.number})}


~{ ... } → fragmentos (incluir/substituir pedaços de template)

Ex.: th:replace="~{fragmentos/stats-card :: stat-card(${icon},${label},...)}"


#... → objetos utilitários e variáveis especiais

Utilitários: #dates, #temporals, #strings, #numbers, #lists, #maps…

Ex.: ${#lists.isEmpty(statsCard)}

Objetos do ambiente: #ctx, #vars, #locale, #httpServletRequest, etc.

Spring MVC helper: #mvc.url('ALIAS')... (se você usa nomes de mapping).

Ex.: th:href="${#mvc.url('ANIMAIS#NOVO').build()}"



@bean (dentro de ${...}) → Spring Bean

Acessa um bean do contexto Spring dentro de uma expressão.

Ex.: ${@animalService.contarQuantidadeAnimais()}

| ... | → template string (substituição fácil)

Útil para montar strings/URLs sem concatenar.

Ex.: th:href="|/animais/${animal.id}|", th:text="|Total: ${total} itens|"




quando usar o quê (regra prática)

Vai ler algo do Model? → ${...}

Está dentro de um <form th:object="..."> e quer campos desse objeto? → *{...}

Vai buscar texto em messages.properties? → #{...}

Vai montar um link/URL? → @{...}

Vai incluir um componente/fragmento? → ~{...}

Precisa de funções auxiliares (strings, datas, listas) ou objetos do ambiente? → #...

Precisa chamar um bean do Spring em uma expressão? → ${@meuBean.algo()}