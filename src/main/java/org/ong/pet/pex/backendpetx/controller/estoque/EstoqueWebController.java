package org.ong.pet.pex.backendpetx.controller.estoque;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ong.pet.pex.backendpetx.dto.categoria_estoque.CriarCategoriaEstoqueRequisicao;
import org.ong.pet.pex.backendpetx.dto.categoria_estoque.ListarCategoriaEstoqueRequisicao;
import org.ong.pet.pex.backendpetx.entity.Estoque;
import org.ong.pet.pex.backendpetx.repository.EstoqueRepository;
import org.ong.pet.pex.backendpetx.repository.ProdutoRepository;
import org.ong.pet.pex.backendpetx.service.CategoriaEstoqueService;
import org.ong.pet.pex.backendpetx.service.EstoqueService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/estoque")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
@Slf4j
public class EstoqueWebController {

    private final EstoqueService estoqueService;
    private final CategoriaEstoqueService categoriaEstoqueService;
    private final EstoqueRepository estoqueRepository;
    private final ProdutoRepository produtoRepository;

    @GetMapping
    public String listarEstoques(Model model,
                                @RequestParam(required = false) String nome) {
        log.info("Acessando lista de estoques - filtro nome: {}", nome);

        try {
            // Buscar estoques
            List<Estoque> estoques;
            if (nome != null && !nome.trim().isEmpty()) {
                // Temporariamente usar findAll e filtrar até o método ser criado
                estoques = estoqueRepository.findAll().stream()
                    .filter(e -> e.getNome().toLowerCase().contains(nome.toLowerCase()))
                    .toList();
            } else {
                estoques = estoqueRepository.findAll();
            }

            // Adicionar contador de produtos para cada estoque
            Map<Long, Long> contadorProdutos = new HashMap<>();
            Map<Long, Long> contadorCategorias = new HashMap<>();

            for (Estoque estoque : estoques) {
                // Contar produtos por estoque (temporário até criar o método específico)
                long produtosCount = produtoRepository.count();
                contadorProdutos.put(estoque.getId(), produtosCount);

                // Contar categorias do estoque
                long categoriasCount = estoque.getCategorias() != null ? estoque.getCategorias().size() : 0;
                contadorCategorias.put(estoque.getId(), categoriasCount);
            }

            model.addAttribute("estoques", estoques);
            model.addAttribute("contadorProdutos", contadorProdutos);
            model.addAttribute("contadorCategorias", contadorCategorias);
            model.addAttribute("filtroNome", nome);

            // Estatísticas gerais
            long totalEstoques = estoques.size();
            long totalProdutos = contadorProdutos.values().stream().mapToLong(Long::longValue).sum();
            long totalCategorias = contadorCategorias.values().stream().mapToLong(Long::longValue).sum();

            model.addAttribute("totalEstoques", totalEstoques);
            model.addAttribute("totalProdutos", totalProdutos);
            model.addAttribute("totalCategorias", totalCategorias);

        } catch (Exception e) {
            log.error("Erro ao carregar lista de estoques", e);
            model.addAttribute("erro", "Erro ao carregar estoques: " + e.getMessage());
            model.addAttribute("estoques", List.of());
        }

        return "estoque/lista";
    }

    @GetMapping("/categorias")
    public String listarCategorias(Model model,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(required = false) String nome) {

        log.info("Acessando página de categorias - page: {}, size: {}, nome: {}", page, size, nome);

        try {
            // Buscar categorias
            var request = new ListarCategoriaEstoqueRequisicao(
                nome, null, page, size, "nome", "asc");
            var categorias = categoriaEstoqueService.listCategoriaEstoque(request);

            model.addAttribute("categorias", categorias);
            model.addAttribute("nome", nome);

        } catch (Exception e) {
            log.error("Erro ao carregar categorias", e);
            model.addAttribute("erro", "Erro ao carregar categorias: " + e.getMessage());
            model.addAttribute("categorias", null);
        }

        return "categoria/lista";
    }

    @GetMapping("/nova-categoria")
    public String novaCategoria(Model model) {
        model.addAttribute("categoria", new CriarCategoriaEstoqueRequisicao());
        return "categoria/formulario";
    }

}
