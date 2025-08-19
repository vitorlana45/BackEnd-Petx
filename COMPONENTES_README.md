# Dashboard Componentizado - PetX

## Visão Geral

O dashboard foi refatorado para usar o padrão de componentização com fragments do Thymeleaf, seguindo uma abordagem similar ao React onde a página é composta apenas através de componentes reutilizáveis.

## Estrutura

```
templates/
├── layouts/
│   └── base.html              # Layout base do sistema
├── fragmentos/
│   └── ui.html                # Componentes de UI reutilizáveis  
└── pages/
    └── index.html             # Dashboard - apenas composição
static/
└── js/
    └── dashboard.js           # JavaScript para funcionalidades
```

## Componentes Disponíveis

### 1. Cards de Estatísticas

#### `stat-card(icon, value, label, color)`
Card básico de estatística.

#### `enhanced-stat-card(icon, value, label, color, trend, trendDirection, progress)`
Card melhorado com contador animado, trend e mini gauge.

### 2. Cards de Ação

#### `action-card(title, description, link, icon)`
Card para ações principais com link.

#### `mini-action-card(title, link, icon, color)`
Versão compacta do card de ação.

### 3. Alertas

#### `alert-card(title, message, type, icon)`
Card de alerta/notificação.

### 4. Tabelas

#### `styled-table(headers, id)`
Tabela estilizada básica.

#### `styled-table-slotted(headers, id, rowsFragment)`
Tabela com slot para injeção de linhas customizadas.

### 5. Cards Específicos

#### `pet-status-card(pet)`
Card especializado para exibir informações de pets.

#### `compact-stat-card(icon, value, label, color)`
Versão compacta do card de estatística.

## Uso no Controller

O controller deve enviar os seguintes atributos para o modelo:

```java
// Estatísticas básicas
model.addAttribute("totalAnimais", totalAnimais);
model.addAttribute("totalTutores", totalTutores); 
model.addAttribute("totalAdocoes", totalAdocoes);
model.addAttribute("totalDoacoes", totalDoacoes);

// Listas para componentes
model.addAttribute("animais", listaAnimais);
model.addAttribute("ultimosAdotados", listaAdocoes);
model.addAttribute("alertasCriticos", listaAlertas);
model.addAttribute("voluntariosStats", mapStats);
```

## Funcionalidades JavaScript

### Filtro de Tabela
Implementado automaticamente para inputs com `data-table-filter="#idTabela"`.

### Contador Animado
Ativado para elementos com classe `.counter-number` e atributo `data-target`.

### Mini Gauge
Implementado para elementos com classe `.progress-circle` e atributo `data-progress`.

## Exemplo de Uso

```html
<!-- KPI com contador animado e gauge -->
<div th:replace="~{fragmentos/ui :: enhanced-stat-card('paw', ${totalAnimais}, 'Animais', 'primary', '+12%', 'up', 85)}"></div>

<!-- Tabela com slot para linhas customizadas -->
<th:block th:fragment="linhasAnimais">
  <tr th:each="a : ${animais}">
    <td th:text="${a.nome}">Nome</td>
    <td th:text="${a.especie}">Espécie</td>
  </tr>
</th:block>
<div th:replace="~{fragmentos/ui :: styled-table-slotted(${headers}, 'tAnimais', ~{::linhasAnimais})}"></div>

<!-- Card de pet -->
<div th:replace="~{fragmentos/ui :: pet-status-card(${pet})}"></div>
```

## Critérios de Aceite Atendidos

✅ `pages/index.html` não possui markup UI hardcoded - apenas `th:replace`  
✅ Tabela com slot renderiza linhas de animais e filtro funciona  
✅ KPIs mostram contadores animados e mini gauge quando há progress  
✅ Fallbacks para listas vazias implementados  
✅ Atributos `data-aos` mantidos nos componentes  
✅ JavaScript funcional para filtro, contadores e gauges  

## Migração

Para migrar páginas existentes:

1. Identifique componentes reutilizáveis no HTML atual
2. Mova o markup para fragments em `fragmentos/ui.html`
3. Substitua o HTML na página por chamadas `th:replace`
4. Ajuste o controller para enviar atributos necessários
5. Teste funcionalidades JavaScript se aplicável
