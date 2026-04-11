package org.ong.pet.pex.backendpetx.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ong.pet.pex.backendpetx.dto.request.DespesaRequisicaoDTO;
import org.ong.pet.pex.backendpetx.dto.response.DespesaDTORespota;
import org.ong.pet.pex.backendpetx.dto.response.ListarDespesaResposta;
import org.ong.pet.pex.backendpetx.entities.Despesa;
import org.ong.pet.pex.backendpetx.enums.CategoriaDespesaEnum;
import org.ong.pet.pex.backendpetx.enums.FormaPagamentoEnum;
import org.ong.pet.pex.backendpetx.enums.StatusDespesaEnum;
import org.ong.pet.pex.backendpetx.repositories.DespesaRepository;
import org.ong.pet.pex.backendpetx.repositories.OngRepository;
import org.ong.pet.pex.backendpetx.service.DespesaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/financeiro/despesas")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
@Slf4j
public class FinanceiroWebController {

    private final DespesaService despesaService;
    private final DespesaRepository despesaRepository;
    private final OngRepository ongRepository;

    @GetMapping
    public String listarDespesas(
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) CategoriaDespesaEnum categoria,
            @RequestParam(required = false) StatusDespesaEnum status,
            @RequestParam(required = false) FormaPagamentoEnum formaPagamento,
            @RequestParam(required = false) LocalDate dataPagamento,
            @RequestParam(required = false) LocalDate dataPrevistaPagamento,
            @RequestParam(required = false) BigDecimal valor,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);

        // Usando o service existente que retorna DTOs
        Page<ListarDespesaResposta> despesas = despesaService.paginarDespesa(
                descricao, categoria, status, formaPagamento, dataPagamento, dataPrevistaPagamento, valor, pageable
        );

        // Cálculos de Totais (usando repository diretamente para agilidade, ideal é mover para service)
        LocalDate hoje = LocalDate.now();
        LocalDate inicioMes = hoje.withDayOfMonth(1);
        LocalDate fimMes = hoje.withDayOfMonth(hoje.lengthOfMonth());

        BigDecimal totalMes = despesaRepository.somarDespesasPorPeriodo(inicioMes, fimMes);

        model.addAttribute("despesas", despesas);
        model.addAttribute("totalMes", totalMes != null ? totalMes : BigDecimal.ZERO);

        // Atributos para preencher os selects de filtro
        model.addAttribute("categorias", CategoriaDespesaEnum.values());
        model.addAttribute("statusList", StatusDespesaEnum.values());
        model.addAttribute("formasPagamento", FormaPagamentoEnum.values());

        return "financeiro/despesas/lista";
    }

    @GetMapping("/nova")
    public String novaDespesa(Model model) {
        Despesa despesa = new Despesa();
        despesa.setValor(BigDecimal.ZERO);
        model.addAttribute("despesa", despesa);
        carregarCombos(model);
        return "financeiro/despesas/form";
    }

    @PostMapping("/salvar")
    public String salvarDespesa(@Valid @ModelAttribute("despesa") Despesa despesa,
                              BindingResult result,
                              Model model,
                              RedirectAttributes redirectAttributes) {

        // Nota: O método recebe Entity 'Despesa' do form Thymeleaf, mas o service espera DTO.
        // Vamos adaptar manualmente ou ajustar o service.
        // Para simplificar a integração com o código existente, vou converter para DTO.

        if (result.hasErrors()) {
            carregarCombos(model);
            return "financeiro/despesas/form";
        }

        try {
            DespesaRequisicaoDTO dto = new DespesaRequisicaoDTO(
                despesa.getDescricao(),
                despesa.getValor(),
                despesa.getCategoria(),
                despesa.getDataPrevistaPagamento(),
                despesa.getFormaPagamento(),
                despesa.getStatusDespesa(),
                despesa.getDataPagamento()
            );

            if (despesa.getId() != null) {
                // Atualização
                Despesa despesaExistente = despesaService.buscarDespesaPorId(despesa.getId());
                // Atualiza campos extras que não estão no DTO padrão
                despesaExistente.setObservacoes(despesa.getObservacoes());
                despesaRepository.save(despesaExistente);

                despesaService.atualizarDespesa(despesa.getId(), dto);
                redirectAttributes.addFlashAttribute("sucesso", "Despesa atualizada com sucesso!");
            } else {
                // Criação
                DespesaDTORespota resposta = despesaService.cadastrarDespesa(dto);
                // Salvar observações
                if (despesa.getObservacoes() != null && !despesa.getObservacoes().isEmpty()) {
                   Despesa d = despesaRepository.findById(resposta.id()).orElseThrow();
                   d.setObservacoes(despesa.getObservacoes());
                   despesaRepository.save(d);
                }

                redirectAttributes.addFlashAttribute("sucesso", "Despesa criada com sucesso!");
            }

            return "redirect:/financeiro/despesas";

        } catch (Exception e) {
            log.error("Erro ao salvar despesa", e);
            carregarCombos(model);
            model.addAttribute("erro", "Erro ao salvar: " + e.getMessage());
            return "financeiro/despesas/form";
        }
    }

    @GetMapping("/{id}/editar")
    public String editarDespesa(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
             Despesa despesa = despesaService.buscarDespesaPorId(id); // Preciso adicionar esse método no service ou usar repository
             model.addAttribute("despesa", despesa);
             carregarCombos(model);
             return "financeiro/despesas/form";
        } catch (Exception e) {
             // Fallback se o service só retorna DTO
             var despesaOpt = despesaRepository.findById(id);
             if (despesaOpt.isPresent()) {
                 model.addAttribute("despesa", despesaOpt.get());
                 carregarCombos(model);
                 return "financeiro/despesas/form";
             }

             redirectAttributes.addFlashAttribute("erro", "Despesa não encontrada.");
             return "redirect:/financeiro/despesas";
        }
    }

    @PostMapping("/{id}/excluir")
    public String excluirDespesa(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            despesaService.deletarDespesa(id);
            redirectAttributes.addFlashAttribute("sucesso", "Despesa excluída com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao excluir despesa.");
        }
        return "redirect:/financeiro/despesas";
    }

    private void carregarCombos(Model model) {
        model.addAttribute("categorias", CategoriaDespesaEnum.values());
        model.addAttribute("statusList", StatusDespesaEnum.values());
        model.addAttribute("formasPagamento", FormaPagamentoEnum.values());
    }
}
