# Padrão de Exceções Implementado - PetX

## Resumo da Implementação

Foi implementado um sistema padronizado de tratamento de exceções que funciona tanto para requisições normais quanto para modais HTMX, com foco na atualização de animais.

## Componentes Implementados

### 1. Fragmento de Mensagens Duplo (`fragmentos/messages.html`)

**Fragmentos disponíveis:**
- `messages` - Para páginas normais
- `modalMessages` - Para modais (com IDs únicos para evitar conflitos)

**Tipos de mensagens suportadas:**
- `mensagemSucesso` - Mensagens de sucesso
- `mensagemErro` - Mensagens de erro
- `mensagemAviso` - Mensagens de aviso
- `mensagemInfo` - Mensagens informativas
- `mensagemErroDetalhe` - Detalhes técnicos (colapsáveis)

### 2. GlobalExceptionHandler Atualizado

**Funcionalidades:**
- Detecta automaticamente se é requisição HTMX
- Retorna fragmentos específicos para modais
- Redireciona inteligentemente para páginas de origem
- Usa reflection para acessar campos do Lombok (workaround para problemas de compilação)

**Tratamento de exceções:**
- `BaseApplicationError` - Exceções de negócio
- `MethodArgumentNotValidException` - Validação de formulários
- `Exception` - Fallback para erros inesperados

### 3. Modal de Edição Atualizado

**Localização:** `animais/perfil.html`

**Características:**
- Container específico para mensagens: `#modalAlerts-editarPerfil`
- Configuração HTMX: `hx-target="#modalAlerts-editarPerfil"`
- Fragmento de mensagens integrado

### 4. Controller de Animais Atualizado

**Método:** `atualizarAnimal()`

**Funcionalidades:**
- Detecta requisições HTMX
- Trata validações de formulário
- Captura exceções do service
- Retorna fragmentos apropriados

## Como Usar

### 1. Em Modais

```html
<!-- No modal -->
<div id="modalAlerts-seuModal">
    <div th:replace="~{fragmentos/messages :: modalMessages}"></div>
</div>

<!-- No formulário -->
<form hx-target="#modalAlerts-seuModal" hx-swap="innerHTML">
    <!-- campos do formulário -->
</form>
```

### 2. Em Páginas Normais

```html
<!-- Na página -->
<div th:replace="~{fragmentos/messages :: messages}"></div>
```

### 3. Lançando Exceções

```java
// No service
if (chipDuplicado) {
    throw AppException.chipDuplicado(chipId);
}
```

### 4. No Controller

```java
@PostMapping("/atualizar")
public String atualizar(@Valid @ModelAttribute("objeto") ObjetoDTO objeto,
                       BindingResult result,
                       Model model,
                       HttpServletRequest req,
                       HttpServletResponse res) {
    
    if (isHtmx(req)) {
        if (result.hasErrors()) {
            res.setStatus(422);
            model.addAttribute("mensagemErro", "Corrija os campos indicados.");
            return "fragmentos/messages :: modalMessages";
        }

        try {
            service.atualizar(objeto);
            res.setStatus(200);
            model.addAttribute("mensagemSucesso", "Atualizado com sucesso!");
            return "fragmentos/messages :: modalMessages";
        } catch (Exception ex) {
            throw ex; // Será capturado pelo GlobalExceptionHandler
        }
    }

    // Lógica para requisições normais
    return "redirect:/pagina";
}
```

## Variáveis de Mensagem (em português)

- `mensagemSucesso` - Mensagem de sucesso
- `mensagemErro` - Mensagem de erro principal
- `mensagemErroDetalhe` - Detalhes técnicos do erro
- `mensagemAviso` - Avisos importantes
- `mensagemInfo` - Informações gerais

## Arquivos de Teste Criados

### TesteController
- `/teste/excecao` - Testa exceção normal
- `/teste/excecao-htmx` - Testa exceção via HTMX
- `/teste/pagina-teste` - Página de teste

### Página de Teste
- `teste/pagina-teste.html` - Interface para testar o sistema

## Mensagens Configuradas

No arquivo `messages.properties`:
```properties
animal.chip.duplicado=Já existe um animal com o chip {0}.
animal.nao.encontrado=Animal com ID {0} não foi encontrado.
```

## Benefícios do Padrão

1. **Consistência** - Mesmo padrão para todas as mensagens
2. **Flexibilidade** - Funciona em modais e páginas
3. **Manutenibilidade** - Centralizado e reutilizável
4. **UX Melhorada** - Mensagens claras e organizadas
5. **Acessibilidade** - Suporte a ARIA labels

## Próximos Passos

1. Aplicar o padrão em outros modais do sistema
2. Criar mais tipos de exceções específicas
3. Implementar toast notifications para ações rápidas
4. Adicionar animações nas mensagens
5. Implementar persistência de mensagens entre sessões

## Como Testar

1. Acesse `/teste/pagina-teste`
2. Teste a exceção normal clicando em "Testar Exceção"
3. Teste a exceção HTMX clicando em "Testar Exceção HTMX"
4. Vá para `/animais/{id}` e teste o modal de edição
5. Tente editar um animal com chip duplicado para ver o erro

## Solução de Problemas

### Erro de Template auth/login
O erro indica que o template `auth/login` não está sendo encontrado. Verificar:
1. Se o arquivo existe em `src/main/resources/templates/auth/login.html`
2. Se não há erros de sintaxe no template
3. Se a configuração do Thymeleaf está correta
4. Se há conflitos de configuração do Spring Security

### Problemas com Lombok
Se houver problemas com getters do Lombok:
1. Verificar se o Lombok está configurado corretamente
2. Usar reflection como workaround (já implementado)
3. Considerar usar getters manuais se necessário
