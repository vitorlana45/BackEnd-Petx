/**
 * GUIA DE USO DO SISTEMA DE NAVEGAÇÃO INTELIGENTE
 * ===============================================
 * 
 * O sistema consiste em:
 * 1. PageInfoBean/PageInfoDTO - dados da página (título, ícone, breadcrumbs)
 * 2. ActionButtonDTO - botões de ação (Novo, Editar, etc)
 * 3. StatsDTO - estatísticas (cards com números)
 * 4. SmartPageHelper - helper para montagem rápida
 * 5. page-header.html (fragment) - renderização no Thymeleaf
 */

// === EXEMPLO 1: DASHBOARD (ANTES vs DEPOIS) ===

// ANTES - Método verboso:
@GetMapping("/dashboard")
public String dashboard(Model model, Principal principal) {
    // ... lógica do usuário ...
    
    // Configuração manual da página
    PageInfoBean pageInfo = PageInfoBean.builder()
            .title("Dashboard")
            .subtitle("Bem-vindo ao sistema de gestão da ONG PetX")
            .icon("fas fa-tachometer-alt");
    model.addAttribute("pageInfo", pageInfo);

    // Estatísticas manuais
    StatsDTO[] statsData = {
        StatsDTO.primary("25", "Total de Animais", "fas fa-paw"),
        StatsDTO.success("15", "Adoções Realizadas", "fas fa-heart"),
        StatsDTO.info("40", "Tutores Cadastrados", "fas fa-users"),
        StatsDTO.warning("5", "Boletins de Resgate", "fas fa-clipboard-list")
    };
    model.addAttribute("statsData", Arrays.asList(statsData));
    
    return "dashboard/index";
}

// DEPOIS - Método com Helper (LIMPO!):
@GetMapping("/dashboard")
public String dashboard(Model model, Principal principal) {
    // ... lógica do usuário ...
    
    // Uma linha apenas! 🎉
    SmartPageHelper.setupDashboard(model, totalAnimais, totalTutores, totalConsumo);
    
    return "dashboard/index";
}

// === EXEMPLO 2: PÁGINA DE ANIMAIS ===

// ANTES - Código repetitivo:
@GetMapping("/animais")
public String listarAnimais(Model model, /* ... filtros ... */) {
    PageInfoDTO pageInfo = PageInfoDTO.builder()
            .title("Gestão de Animais")
            .subtitle("Administre e acompanhe todos os animais da ONG")
            .icon("fas fa-paw")
            .addBreadcrumb("Animais");
    model.addAttribute("pageInfo", pageInfo);

    ActionButtonDTO actionButton = ActionButtonDTO.primary(
            "Novo Animal", "/animais/novo", "fas fa-plus");
    model.addAttribute("actionButton", actionButton);
    
    // ... resto da lógica ...
    return "animais/lista";
}

// DEPOIS - Uma linha! 🚀
@GetMapping("/animais")
public String listarAnimais(Model model, /* ... filtros ... */) {
    SmartPageHelper.setupAnimalsPage(model);
    
    // ... resto da lógica ...
    return "animais/lista";
}

// === EXEMPLO 3: PÁGINA CUSTOMIZADA ===

@GetMapping("/relatorios")
public String relatorios(Model model) {
    SmartPageHelper.customPage("Relatórios", "Dados e métricas do sistema", "fas fa-chart-bar")
            .addBreadcrumb("Relatórios")
            .build(model);
    
    return "relatorios/index";
}

// === TEMPLATE HTML (animais/lista.html) ===
/*
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org"
      th:replace="~{layouts/base :: layout(~{::title}, ~{::section})}">
<head>
    <title>Animais - ONG PetX</title>
</head>
<body>
<section>
    <!-- UMA LINHA APENAS! Substitui todo o cabeçalho complexo -->
    <div th:replace="~{fragmentos/page-header :: smart-header(${pageInfo}, ${actionButton})}"></div>
    
    <!-- Resto do conteúdo... -->
    <div class="filtros">...</div>
    <div class="cards-animais">...</div>
</section>
</body>
</html>
*/

// === VANTAGENS DO SISTEMA INTELIGENTE ===
/*
✅ CONSISTÊNCIA: Todos os cabeçalhos têm a mesma aparência
✅ PRODUTIVIDADE: De 15+ linhas para 1 linha
✅ MANUTENIBILIDADE: Mudança no helper reflete em todas as páginas
✅ FLEXIBILIDADE: Ainda permite customização quando necessário
✅ BREADCRUMBS AUTOMÁTICOS: Navegação inteligente
✅ RESPONSIVIDADE: CSS otimizado para mobile
✅ ACESSIBILIDADE: Estrutura semântica correta
*/

// === COMO IMPLEMENTAR ===
/*
1. Copie o SmartPageHelper.java para seu projeto
2. Atualize seus controllers para usar o helper
3. Substitua os cabeçalhos nos templates pelo fragment smart-header
4. Configure o navigation.css no layout base
5. Teste e customize conforme necessário
*/
