package org.ong.pet.pex.backendpetx.controller.categoria;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.ong.pet.pex.backendpetx.dto.EstoqueDTO;
import org.ong.pet.pex.backendpetx.dto.categoria_estoque.*;
import org.ong.pet.pex.backendpetx.service.CategoriaEstoqueService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/categorias")
@RequiredArgsConstructor
@Tag(name = "Categoria Estoque", description = "Operações relacionadas às categorias de estoque")
public class CategoriaWebController {

    private final CategoriaEstoqueService categoriaEstoqueService;

    @GetMapping
    public String listarCategorias(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Long estoqueId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome") String sort,
            @RequestParam(defaultValue = "asc") String direction,
            Model model) {

        ListarCategoriaEstoqueRequisicao request = new ListarCategoriaEstoqueRequisicao(
                nome, estoqueId, page, size, sort, direction);

        Page<ListarCategoriaEstoqueResposta> categorias = categoriaEstoqueService.listCategoriaEstoque(request);

        model.addAttribute("categorias", categorias);
        model.addAttribute("filtroNome", nome);
        model.addAttribute("filtroEstoqueId", estoqueId);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);

        return "estoque/categorias/lista";
    }

    @GetMapping("/novo")
    public String novaCategoria(Model model) {
        model.addAttribute("categoria", new CriarCategoriaEstoqueRequisicao());
        List<EstoqueDTO> listaEstoques = categoriaEstoqueService.listAllEstoques();
        model.addAttribute("estoques", listaEstoques);


        return "categoria/formulario";
    }

    @PostMapping("/novo")
    public String criarCategoria(
            @Valid @ModelAttribute("categoria") CriarCategoriaEstoqueRequisicao request,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (result.hasErrors()) {
            // Em caso de erro, voltar para o formulário com os dados e estoques
            List<EstoqueDTO> listaEstoques = categoriaEstoqueService.listAllEstoques();
            model.addAttribute("estoques", listaEstoques);
            model.addAttribute("categoria", request);

            redirectAttributes.addFlashAttribute("erro", "Por favor, corrija os erros no formulário.");
            return "categoria/formulario";
        }

        try {
            CriarCategoriaEstoqueResposta response = categoriaEstoqueService.createCategoriaEstoque(request);
            redirectAttributes.addFlashAttribute("sucesso", response.getMensagem());
            // Redirecionar para a lista de categorias ao invés de detalhes inexistente
            return "redirect:/estoque/novo";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/categorias/novo";
        }
    }

    @GetMapping("/{id}/editar")
    public String editarCategoria(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            BuscarCategoriaEstoqueRequisicao request = new BuscarCategoriaEstoqueRequisicao(id);
            BuscarCategoriaEstoqueResposta categoria = categoriaEstoqueService.getCategoriaEstoque(request);

            AtualizarCategoriaEstoqueRequisicao updateRequest = new AtualizarCategoriaEstoqueRequisicao();
            updateRequest.setId(categoria.getId());
            updateRequest.setNome(categoria.getNome());
            updateRequest.setDescricao(categoria.getDescricao());
            updateRequest.setEstoqueId(categoria.getEstoqueId());

            model.addAttribute("categoria", updateRequest);
            model.addAttribute("categoriaId", id);

            // Carregar lista de estoques para o select
            List<EstoqueDTO> listaEstoques = categoriaEstoqueService.listAllEstoques();
            model.addAttribute("estoques", listaEstoques);

            return "categoria/formulario";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", "Categoria não encontrada");
            return "redirect:/estoque/categorias";
        }
    }

    @PostMapping("/{id}/editar")
    public String atualizarCategoria(
            @PathVariable Long id,
            @Valid @ModelAttribute("categoria") AtualizarCategoriaEstoqueRequisicao request,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (result.hasErrors()) {
            // Em caso de erro, recarregar a página com os dados
            List<EstoqueDTO> listaEstoques = categoriaEstoqueService.listAllEstoques();
            model.addAttribute("estoques", listaEstoques);
            model.addAttribute("categoriaId", id);
            return "categoria/formulario";
        }

        try {
            request.setId(id);
            AtualizarCategoriaEstoqueResposta response = categoriaEstoqueService.updateCategoriaEstoque(request);
            redirectAttributes.addFlashAttribute("sucesso", response.getMensagem());
            return "redirect:/estoque/categorias";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/categorias/" + id + "/editar";
        }
    }

    @PostMapping("/{id}/excluir")
    public String excluirCategoria(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            ExcluirCategoriaEstoqueRequisicao request = new ExcluirCategoriaEstoqueRequisicao(id);
            ExcluirCategoriaEstoqueResposta response = categoriaEstoqueService.deleteCategoriaEstoque(request);
            redirectAttributes.addFlashAttribute("mensagemSucesso", response.getMensagem());
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir categoria: " + e.getMessage());
        }
        return "redirect:/estoque/categorias";
    }

    @GetMapping("/{id}")
    public String visualizarCategoria(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            BuscarCategoriaEstoqueRequisicao request = new BuscarCategoriaEstoqueRequisicao(id);
            BuscarCategoriaEstoqueResposta categoria = categoriaEstoqueService.getCategoriaEstoque(request);
            model.addAttribute("categoria", categoria);
            return "estoque/categorias/detalhes";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Categoria não encontrada");
            return "redirect:/estoque/categorias";
        }
    }

    // ========== ENDPOINTS PARA API REST ==========

    @PostMapping("/api")
    @ResponseBody
    @Operation(summary = "Criar nova categoria de estoque via API")
    public ResponseEntity<CriarCategoriaEstoqueResposta> createCategoriaEstoqueAPI(
            @Valid @RequestBody CriarCategoriaEstoqueRequisicao request) {
        try {
            CriarCategoriaEstoqueResposta response = categoriaEstoqueService.createCategoriaEstoque(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro ao criar categoria de estoque: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/api")
    @ResponseBody
    @Operation(summary = "Buscar categoria de estoque por ID via API")
    public ResponseEntity<BuscarCategoriaEstoqueResposta> getCategoriaEstoqueAPI(@PathVariable Long id) {
        try {
            BuscarCategoriaEstoqueRequisicao request = new BuscarCategoriaEstoqueRequisicao(id);
            BuscarCategoriaEstoqueResposta response = categoriaEstoqueService.getCategoriaEstoque(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/api")
    @ResponseBody
    @Operation(summary = "Atualizar categoria de estoque via API")
    public ResponseEntity<AtualizarCategoriaEstoqueResposta> updateCategoriaEstoqueAPI(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarCategoriaEstoqueRequisicao request) {
        try {
            request.setId(id);
            AtualizarCategoriaEstoqueResposta response = categoriaEstoqueService.updateCategoriaEstoque(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro ao atualizar categoria de estoque: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/api")
    @ResponseBody
    @Operation(summary = "Excluir categoria de estoque via API")
    public ResponseEntity<ExcluirCategoriaEstoqueResposta> deleteCategoriaEstoqueAPI(@PathVariable Long id) {
        try {
            ExcluirCategoriaEstoqueRequisicao request = new ExcluirCategoriaEstoqueRequisicao(id);
            ExcluirCategoriaEstoqueResposta response = categoriaEstoqueService.deleteCategoriaEstoque(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api")
    @ResponseBody
    @Operation(summary = "Listar categorias de estoque com filtros e paginação via API")
    public ResponseEntity<Page<ListarCategoriaEstoqueResposta>> listCategoriaEstoqueAPI(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Long estoqueId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome") String sort,
            @RequestParam(defaultValue = "asc") String direction) {

        ListarCategoriaEstoqueRequisicao request = new ListarCategoriaEstoqueRequisicao(
                nome, estoqueId, page, size, sort, direction);

        Page<ListarCategoriaEstoqueResposta> response = categoriaEstoqueService.listCategoriaEstoque(request);
        return ResponseEntity.ok(response);
    }
}
