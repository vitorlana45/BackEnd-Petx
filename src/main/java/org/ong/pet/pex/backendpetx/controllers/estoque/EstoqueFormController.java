package org.ong.pet.pex.backendpetx.controllers.estoque;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ong.pet.pex.backendpetx.dto.produto.ProdutoDTO;
import org.ong.pet.pex.backendpetx.entities.Estoque;
import org.ong.pet.pex.backendpetx.entities.Ong;
import org.ong.pet.pex.backendpetx.enums.UnidadeDeMedidaEnum;
import org.ong.pet.pex.backendpetx.repositories.EstoqueRepository;
import org.ong.pet.pex.backendpetx.repositories.OngRepository;
import org.ong.pet.pex.backendpetx.service.CategoriaEstoqueService;
import org.ong.pet.pex.backendpetx.service.EstoqueService;
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
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/estoque")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
@Slf4j
public class EstoqueFormController {

    private final EstoqueService estoqueService;
    private final CategoriaEstoqueService categoriaEstoqueService;
    private final EstoqueRepository estoqueRepository;
    private final OngRepository ongRepository;

    @GetMapping("/novo")
    public String novoEstoque(Model model) {
        log.info("Acessando página de cadastro de estoque");

        EstoqueDTO estoqueDTO = new EstoqueDTO();
        model.addAttribute("estoque", estoqueDTO);
        model.addAttribute("ongs", ongRepository.findAll());

        return "estoque/form";
    }

    @PostMapping("/salvar")
    public String salvarEstoque(@Valid @ModelAttribute("estoque") EstoqueDTO estoqueDTO,
                              BindingResult result,
                              Model model,
                                RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            log.error("Erro ao salvar estoque: {}", result.getAllErrors());
            model.addAttribute("ongs", ongRepository.findAll());
            model.addAttribute("erro", "Erro ao salvar estoque. Verifique os campos.");
            return "estoque/form";
        }

        try {
            Estoque estoque;
            if (estoqueDTO.getId() != null) {
                log.info("Atualizando estoque id: {}", estoqueDTO.getId());
                estoque = estoqueService.atualizarEstoque(estoqueDTO);
                redirectAttributes.addFlashAttribute("sucesso", "Estoque atualizado com sucesso!");
            } else {
                log.info("Criando novo estoque: {}", estoqueDTO.getNome());
                estoque = estoqueService.criarEstoque(estoqueDTO);
                redirectAttributes.addFlashAttribute("sucesso", "Estoque criado com sucesso!");
            }

            return "redirect:/estoque/detalhes/" + estoque.getId();

        } catch (Exception e) {
            log.error("Erro ao salvar estoque", e);
            model.addAttribute("ongs", ongRepository.findAll());
            model.addAttribute("erro", "Erro ao salvar estoque: " + e.getMessage());
            return "estoque/form";
        }
    }

    @GetMapping("/{id}/editar")
    public String editarEstoque(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            log.info("Acessando edição de estoque id: {}", id);
            Estoque estoque = estoqueRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Estoque não encontrado"));

            EstoqueDTO estoqueDTO = new EstoqueDTO();
            estoqueDTO.setId(estoque.getId());
            estoqueDTO.setNome(estoque.getNome());

            model.addAttribute("estoque", estoqueDTO);
            model.addAttribute("ongs", ongRepository.findAll());

            return "estoque/form";

        } catch (Exception e) {
            log.error("Erro ao buscar estoque para edição", e);
            redirectAttributes.addFlashAttribute("erro", "Estoque não encontrado: " + e.getMessage());
            return "redirect:/estoque";
        }
    }

    @GetMapping("/detalhes/{id}")
    public String detalhesEstoque(@PathVariable Long id,
                               @RequestParam(required = false) String nome,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            log.info("Acessando detalhes do estoque id: {}", id);
            Estoque estoque = estoqueRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Estoque não encontrado"));

            // Buscar produtos do estoque
            Pageable pageable = PageRequest.of(page, size);
            var produtos = estoqueService.listarProdutosPorEstoque(id, nome, pageable);

            // Contadores para estatísticas
            long produtosCount = produtos.getTotalElements();
            long estoqueBaixoCount = produtos.getContent().stream()
                .filter(p -> p.quantidade() <= 50 && p.quantidade() > 10)
                .count();
            long estoqueZeradoCount = produtos.getContent().stream()
                .filter(p -> p.quantidade() <= 10)
                .count();

            // Dados para a seção de categorias
            var categoriasContagem = new java.util.HashMap<String, Long>();
            if (estoque.getCategorias() != null) {
                estoque.getCategorias().forEach(c -> {
                    categoriasContagem.put(c.getNome(),
                        produtos.getContent().stream()
                            .filter(p -> p.categoria() != null && p.categoria().equals(c.getNome()))
                            .count());
                });
            }

            model.addAttribute("estoque", estoque);
            model.addAttribute("produtos", produtos);
            model.addAttribute("produtosCount", produtosCount);
            model.addAttribute("estoqueBaixoCount", estoqueBaixoCount);
            model.addAttribute("estoqueZeradoCount", estoqueZeradoCount);
            model.addAttribute("categoriasContagem", categoriasContagem);

            return "estoque/detalhes";

        } catch (Exception e) {
            log.error("Erro ao buscar detalhes do estoque", e);
            redirectAttributes.addFlashAttribute("erro", "Erro ao buscar detalhes do estoque: " + e.getMessage());
            return "redirect:/estoque";
        }
    }

    @GetMapping("/{id}/produtos/novo")
    public String novoProdutoEstoque(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            log.info("Acessando página de cadastro de produto para estoque id: {}", id);
            Estoque estoque = estoqueRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Estoque não encontrado"));

            ProdutoDTO produtoDTO = new ProdutoDTO();

            model.addAttribute("estoque", estoque);
            model.addAttribute("produto", produtoDTO);
            model.addAttribute("categorias", estoque.getCategorias());
            model.addAttribute("unidadesDeMedida", UnidadeDeMedidaEnum.values());

            return "estoque/produto-form";

        } catch (Exception e) {
            log.error("Erro ao carregar formulário de produto", e);
            redirectAttributes.addFlashAttribute("erro", "Erro ao carregar formulário: " + e.getMessage());
            return "redirect:/estoque/detalhes/" + id;
        }
    }

    @PostMapping("/estoque/salvar")
    public String salvar(@Valid EstoqueDTO dto, BindingResult br, Model model, HttpServletResponse resp) {
        if (br.hasErrors()) {
            resp.setStatus(HttpStatus.BAD_REQUEST.value());
            model.addAttribute("erro", "Verifique os campos.");
            // você pode devolver um HTML simples de erro ou nada; o modal continua aberto
            return "estoque/fragmentos :: erro-inline";
        }

        estoqueService.criarEstoque(dto); // grava nome + descricao

//        model.addAttribute("estoques", estoqueService.listar());
        resp.setHeader("HX-Trigger", "estoque-criado"); // dica pro JS fechar modal
        return "estoque/fragmentos :: lista"; // SOMENTE a lista (fora do modal)
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
    @PostMapping("/salvar-ajax")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> salvarEstoqueAjax(@Valid @ModelAttribute EstoqueDTO estoqueDTO) {
        log.info("Recebendo requisição AJAX para salvar estoque: {}", estoqueDTO);
        Map<String, Object> response = new HashMap<>();

        try {

            Estoque estoque;
            if (estoqueDTO.getId() != null) {
                log.info("Atualizando estoque id: {}", estoqueDTO.getId());
                estoque = estoqueService.atualizarEstoque(estoqueDTO);
                response.put("mensagem", "Estoque atualizado com sucesso!");
            } else {
                log.info("Criando novo estoque: {}", estoqueDTO.getNome());
                estoque = estoqueService.criarEstoque(estoqueDTO);
                response.put("mensagem", "Estoque criado com sucesso!");
            }

            response.put("sucesso", true);
            response.put("estoqueId", estoque.getId());

        } catch (Exception e) {
            log.error("Erro ao salvar estoque via AJAX", e);
            response.put("sucesso", false);
            response.put("mensagem", "Erro ao salvar estoque: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/dados-modal")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getDadosModal() {
        Map<String, Object> response = new HashMap<>();

        try {
            // Buscar dados necessários para os modais
            response.put("ongs", ongRepository.findAll());
            response.put("unidadesDeMedida", UnidadeDeMedidaEnum.values());

            response.put("sucesso", true);
        } catch (Exception e) {
            log.error("Erro ao buscar dados para modais", e);
            response.put("sucesso", false);
            response.put("mensagem", "Erro ao carregar dados: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
}
