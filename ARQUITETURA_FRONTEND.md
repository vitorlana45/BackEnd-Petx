# Guia da Nova Arquitetura Frontend - PetX

## 📋 Visão Geral

Esta documentação descreve a nova arquitetura frontend implementada no projeto PetX, seguindo as melhores práticas da comunidade Thymeleaf para criação de templates modulares, reutilizáveis e de fácil manutenção.

## 🏗️ Estrutura de Arquivos

```
templates/
├── layouts/                     # Layouts base
│   ├── master.html             # Layout principal (base para todas as páginas)
│   ├── dashboard.html          # Layout específico para dashboards
│   ├── form.html              # Layout específico para formulários
│   ├── list.html              # Layout específico para listagens
│   └── fragments/             # Fragmentos de layout
│       ├── head.html          # Meta tags, CSS e scripts do <head>
│       ├── navigation.html    # Barra de navegação
│       ├── scripts.html       # Scripts JavaScript globais
│       ├── breadcrumb.html    # Navegação estrutural
│       └── alerts.html        # Sistema de alertas/notificações
├── components/                 # Componentes reutilizáveis
│   ├── cards.html             # Componentes de cards (stats, ações, etc.)
│   ├── forms.html             # Componentes de formulário
│   └── badges.html            # Badges de status, prioridade, etc.
├── dashboard/                  # Páginas do dashboard
│   └── index.html             # Dashboard principal (refatorado)
└── animais/                   # Páginas de animais
    ├── lista-refatorada.html   # Exemplo de listagem
    ├── formulario-refatorado.html # Exemplo de formulário
    └── detalhes-refatorado.html   # Exemplo de detalhes
```

## 🎯 Conceitos Fundamentais

### 1. Hierarquia de Layouts

**Master Layout (`layouts/master.html`)**
- Layout base para todas as páginas
- Contém estrutura HTML básica, navegação, alertas e scripts globais
- Define blocos de substituição para title e content

**Layouts Especializados**
- `dashboard.html`: Para páginas de dashboard com métricas e widgets
- `form.html`: Para páginas de formulários com validação
- `list.html`: Para páginas de listagem com filtros e tabelas

### 2. Sistema de Fragmentos

**Fragments de Layout** (`layouts/fragments/`)
- Componentes estruturais da aplicação
- Incluem head, navegação, scripts, breadcrumb e alertas
- Centralizados para facilitar manutenção

**Fragments de Componentes** (`components/`)
- Componentes reutilizáveis específicos
- Cards, formulários, badges, etc.
- Parametrizáveis e modulares

## 🚀 Como Usar

### Criando uma Nova Página

1. **Escolha o layout apropriado:**
   ```html
   <!-- Para dashboard -->
   <html th:replace="~{layouts/dashboard :: layout(~{::title}, ~{::content})}">
   
   <!-- Para formulário -->
   <html th:replace="~{layouts/form :: layout(~{::title}, ~{::formContent})}">
   
   <!-- Para listagem -->
   <html th:replace="~{layouts/list :: layout(~{::title}, ~{::content})}">
   
   <!-- Para página simples -->
   <html th:replace="~{layouts/master :: layout(~{::title}, ~{::content})}">
   ```

2. **Defina o título:**
   ```html
   <title>Título da Página</title>
   ```

3. **Crie o conteúdo específico:**
   ```html
   <div th:fragment="content">
       <!-- Seu conteúdo aqui -->
   </div>
   ```

### Usando Componentes

**Cards:**
```html
<!-- Card de estatística -->
<div th:replace="~{components/cards :: stat-card('Animais Cadastrados', ${totalAnimais}, 'fas fa-paw', 'primary')}"></div>

<!-- Card de ação -->
<div th:replace="~{components/cards :: action-card('Cadastrar Animal', 'Adicionar novo animal ao sistema', '/animais/novo', 'btn-primary', 'fas fa-plus')}"></div>

<!-- Card de animal -->
<div th:replace="~{components/cards :: animal-card(${animal})}"></div>
```

**Formulários:**
```html
<!-- Input de texto -->
<div th:replace="~{components/forms :: text-input('nome', 'Nome do Animal', 'Digite o nome...', true, 'Nome como o animal é conhecido')}"></div>

<!-- Select -->
<div th:replace="~{components/forms :: select('especie', 'Espécie', ${especies}, 'codigo', 'nome', true, 'Tipo de animal')}"></div>

<!-- Checkbox/Switch -->
<div th:replace="~{components/forms :: switch('vacinado', 'Animal vacinado', 'Marque se o animal possui vacinas em dia')}"></div>

<!-- Botões do formulário -->
<div th:replace="~{components/forms :: form-buttons('Salvar Animal', '/animais', true)}"></div>
```

**Badges:**
```html
<!-- Badge de status -->
<div th:replace="~{components/badges :: status-badge(${animal.status})}"></div>

<!-- Badge de prioridade -->
<div th:replace="~{components/badges :: priority-badge(${urgencia})}"></div>

<!-- Badge personalizado -->
<div th:replace="~{components/badges :: custom-badge('Adotado', 'success')}"></div>
```

## 🎨 Sistema de CSS

### Ordem de Carregamento
1. **CSS Framework**: Bootstrap 5
2. **CSS Variables**: Variáveis CSS customizadas
3. **Base**: Estilos base e resets
4. **Components**: Estilos dos componentes
5. **Layouts**: Estilos dos layouts
6. **Pages**: Estilos específicos de páginas
7. **Utilities**: Classes utilitárias

### Estrutura Recomendada
```css
/* static/css/variables.css */
:root {
    --primary-color: #0d6efd;
    --secondary-color: #6c757d;
    /* ... outras variáveis */
}

/* static/css/components.css */
.stat-card { /* estilos do componente */ }
.animal-card { /* estilos do componente */ }

/* static/css/layouts.css */
.dashboard-layout { /* estilos do layout */ }
.form-layout { /* estilos do layout */ }
```

## 📱 Responsividade

Todos os componentes são desenvolvidos com **mobile-first** usando Bootstrap 5:

- **xs**: < 576px (padrão)
- **sm**: ≥ 576px
- **md**: ≥ 768px
- **lg**: ≥ 992px
- **xl**: ≥ 1200px
- **xxl**: ≥ 1400px

## ♿ Acessibilidade

### Recursos Implementados
- **ARIA labels** em todos os componentes
- **Navegação por teclado** funcional
- **Alto contraste** nos elementos
- **Textos alternativos** em imagens
- **Estrutura semântica** HTML5

### Exemplo de Uso
```html
<!-- Input com acessibilidade -->
<input type="text" 
       class="form-control" 
       th:field="*{nome}" 
       th:aria-describedby="nomeHelp"
       aria-label="Nome do animal">
<div id="nomeHelp" class="form-text">Nome como o animal é conhecido</div>
```

## 🔧 Validação de Formulários

### Bootstrap Validation
```html
<form class="needs-validation" novalidate>
    <input type="text" class="form-control" required>
    <div class="invalid-feedback">
        Por favor, preencha este campo.
    </div>
</form>
```

### Thymeleaf Validation
```html
<input type="text" 
       th:field="*{nome}" 
       th:classappend="${#fields.hasErrors('nome')} ? 'is-invalid' : ''">
<div th:if="${#fields.hasErrors('nome')}" class="invalid-feedback">
    <span th:errors="*{nome}">Erro de validação</span>
</div>
```

## 🧩 Componentes Disponíveis

### Cards
- `stat-card`: Exibe estatísticas numéricas
- `action-card`: Cards com call-to-action
- `animal-card`: Card específico para animais
- `user-card`: Card para usuários/tutores
- `activity-card`: Card para atividades recentes

### Formulários
- `text-input`: Input de texto
- `select`: Dropdown/select
- `checkbox`: Checkbox individual
- `radio-group`: Grupo de radio buttons
- `switch`: Toggle switch
- `file-upload`: Upload de arquivos
- `date-input`: Input de data
- `textarea`: Área de texto
- `form-buttons`: Botões do formulário

### Badges
- `status-badge`: Status do animal (disponível, adotado, etc.)
- `priority-badge`: Prioridade (alta, média, baixa)
- `animal-type-badge`: Tipo de animal (cão, gato, etc.)
- `vaccination-badge`: Status de vacinação
- `custom-badge`: Badge customizável

## 🔄 Migração de Páginas Existentes

### Passo a Passo

1. **Identifique o tipo da página** (dashboard, form, list, detalhes)

2. **Escolha o layout apropriado:**
   ```html
   <!-- Antes -->
   <!DOCTYPE html>
   <html xmlns:th="http://www.thymeleaf.org">
   
   <!-- Depois -->
   <html th:replace="~{layouts/form :: layout(~{::title}, ~{::formContent})}">
   ```

3. **Mova o conteúdo para o fragment correto:**
   ```html
   <!-- Antes -->
   <body>
       <h1>Título</h1>
       <div>Conteúdo...</div>
   </body>
   
   <!-- Depois -->
   <div th:fragment="formContent">
       <h1>Título</h1>
       <div>Conteúdo...</div>
   </div>
   ```

4. **Substitua elementos por componentes:**
   ```html
   <!-- Antes -->
   <div class="card">
       <div class="card-body">
           <h5>Total de Animais</h5>
           <h2 th:text="${total}">0</h2>
       </div>
   </div>
   
   <!-- Depois -->
   <div th:replace="~{components/cards :: stat-card('Total de Animais', ${total}, 'fas fa-paw', 'primary')}"></div>
   ```

## 🎉 Exemplos Práticos

### Dashboard Simples
```html
<html th:replace="~{layouts/dashboard :: layout(~{::title}, ~{::content})}">
<title>Dashboard</title>
<div th:fragment="content">
    <div class="row">
        <div class="col-md-3">
            <div th:replace="~{components/cards :: stat-card('Animais', ${totalAnimais}, 'fas fa-paw', 'primary')}"></div>
        </div>
        <div class="col-md-3">
            <div th:replace="~{components/cards :: stat-card('Adoções', ${totalAdocoes}, 'fas fa-home', 'success')}"></div>
        </div>
    </div>
</div>
</html>
```

### Formulário Completo
```html
<html th:replace="~{layouts/form :: layout(~{::title}, ~{::formContent})}">
<title>Cadastro de Animal</title>
<div th:fragment="formContent">
    <form th:action="@{/animais}" th:object="${animal}" method="post">
        <div th:replace="~{components/forms :: text-input('nome', 'Nome', 'Digite o nome...', true, 'Nome do animal')}"></div>
        <div th:replace="~{components/forms :: select('especie', 'Espécie', ${especies}, 'id', 'nome', true, 'Selecione a espécie')}"></div>
        <div th:replace="~{components/forms :: form-buttons('Salvar', '/animais', true)}"></div>
    </form>
</div>
</html>
```

## 🛠️ Manutenção

### Adicionando Novos Componentes

1. **Crie o fragment no arquivo apropriado** (`components/`)
2. **Documente os parâmetros** necessários
3. **Adicione exemplo de uso** nesta documentação
4. **Teste em diferentes contextos**

### Modificando Layouts

1. **Avalie o impacto** em páginas existentes
2. **Mantenha retrocompatibilidade** quando possível
3. **Atualize a documentação**
4. **Teste todas as páginas** afetadas

## 📚 Recursos Adicionais

- [Documentação Thymeleaf](https://www.thymeleaf.org/documentation.html)
- [Bootstrap 5 Documentation](https://getbootstrap.com/docs/5.3/)
- [FontAwesome Icons](https://fontawesome.com/icons)
- [Web Accessibility Guidelines](https://www.w3.org/WAI/WCAG21/quickref/)

---

**Desenvolvido com ❤️ para o projeto PetX**
