package org.ong.pet.pex.backendpetx.controller;

import lombok.RequiredArgsConstructor;
import org.ong.pet.pex.backendpetx.repository.DespesaRepository;
import org.ong.pet.pex.backendpetx.repository.ReceitaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Controller
@RequestMapping("/financeiro")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
public class FinanceiroDashboardController {

    private final DespesaRepository despesaRepository;
    private final ReceitaRepository receitaRepository;

    @GetMapping
    public String dashboard(Model model) {
        LocalDate hoje = LocalDate.now();
        LocalDate inicioMes = hoje.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate fimMes = hoje.with(TemporalAdjusters.lastDayOfMonth());

        // Totais do Mês
        BigDecimal receitasMes = receitaRepository.somarReceitasPorPeriodo(inicioMes, fimMes);
        BigDecimal despesasMes = despesaRepository.somarDespesasPorPeriodo(inicioMes, fimMes);

        receitasMes = receitasMes != null ? receitasMes : BigDecimal.ZERO;
        despesasMes = despesasMes != null ? despesasMes : BigDecimal.ZERO;
        BigDecimal saldoMes = receitasMes.subtract(despesasMes);

        // Totais Gerais (Acumulado)
        BigDecimal totalReceitas = receitaRepository.somarTotalReceitas();
        BigDecimal totalDespesas = despesaRepository.somarTotalDespesas();

        totalReceitas = totalReceitas != null ? totalReceitas : BigDecimal.ZERO;
        totalDespesas = totalDespesas != null ? totalDespesas : BigDecimal.ZERO;

        model.addAttribute("receitasMes", receitasMes);
        model.addAttribute("despesasMes", despesasMes);
        model.addAttribute("saldoMes", saldoMes);

        // Saldo Acumulado (Caixa Atual)
        model.addAttribute("saldoAcumulado", totalReceitas.subtract(totalDespesas));

        return "financeiro/dashboard";
    }
}
