package org.ong.pet.pex.backendpetx.controller;

import jakarta.validation.Valid;
import org.ong.pet.pex.backendpetx.dto.file.FotosBean;
import org.ong.pet.pex.backendpetx.dto.request.AnimalDTO;
import org.ong.pet.pex.backendpetx.dto.request.AnimalGenericoRequisicao;
import org.ong.pet.pex.backendpetx.dto.request.BoletimRequisicao;
import org.ong.pet.pex.backendpetx.dto.request.MaezinhaComFilhotesDTO;
import org.ong.pet.pex.backendpetx.dto.response.BoletimResposta;
import org.ong.pet.pex.backendpetx.entity.media.MediaTargetType;
import org.ong.pet.pex.backendpetx.entity.media.MediaUsage;
import org.ong.pet.pex.backendpetx.entity.Usuario;
import org.ong.pet.pex.backendpetx.enums.*;
import org.ong.pet.pex.backendpetx.controller.bean.ActionButtonDTO;
import org.ong.pet.pex.backendpetx.controller.bean.PageInfoBean;
import org.ong.pet.pex.backendpetx.security.utils.SecurityUtils;
import org.ong.pet.pex.backendpetx.service.BoletimService;
import org.ong.pet.pex.backendpetx.service.impl.MediaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/boletins")
public class BoletimController {

    private final BoletimService boletimService;
    private final MediaService mediaService;

    public BoletimController(BoletimService boletimService, MediaService mediaService) {
        this.boletimService = boletimService;
        this.mediaService = mediaService;
    }

    @PreAuthorize("hasAnyRole('COLABORADOR','ADMIN')")
    @GetMapping(value = "/form")
    public String novo(Model model) {
        BoletimRequisicao req = new BoletimRequisicao();

        // evita NPE no
        // binding aninhado
        if (req.getAnimal() == null) req.setAnimal(new AnimalDTO());
        if (req.getAnimal().getMaezinhaComFilhotes() == null) {
            req.getAnimal().setMaezinhaComFilhotes(new MaezinhaComFilhotesDTO());
        }

        FotosBean FotosBean = new FotosBean(List.of());
        model.addAttribute("fotos", FotosBean);
        model.addAttribute("boletim", req);
        model.addAttribute("modoOcorrencia", "nova");
        model.addAttribute("activeTab", "ocorrencia");
        model.addAttribute("currentPage", "/boletins/form");
        carregarCombos(model);
        return "boletim/cadastro";
    }

    @PreAuthorize("hasAnyRole('COLABORADOR','ADMIN')")
    @PostMapping(value = "/salvar", params = "nav=tab1")
    public String voltarParaOcorrencia(@ModelAttribute("boletim") BoletimRequisicao dto,
                                      BindingResult br,
                                      @ModelAttribute("fotos") FotosBean fotos,
                                      Model model,
                                      @RequestParam(name = "modoOcorrencia", defaultValue = "nova") String modoOcorrencia) {
        prepararTelaCadastro(model, dto, fotos, modoOcorrencia, "ocorrencia");
        return "boletim/cadastro";
    }

    @PreAuthorize("hasAnyRole('COLABORADOR','ADMIN')")
    @PostMapping(value = "/salvar", params = "nav=tab2")
    public String irParaAnimal(@ModelAttribute("boletim") BoletimRequisicao dto,
                              BindingResult br,
                              @ModelAttribute("fotos") FotosBean fotos,
                              Model model,
                              @RequestParam(name = "modoOcorrencia", defaultValue = "nova") String modoOcorrencia) {
        validarTab1(dto, br, modoOcorrencia, getOngIdLogada());
        String nextTab = br.hasErrors() ? "ocorrencia" : "animal";
        prepararTelaCadastro(model, dto, fotos, modoOcorrencia, nextTab);
        return "boletim/cadastro";
    }

    @PreAuthorize("hasAnyRole('COLABORADOR','ADMIN')")
    @PostMapping(value = "/salvar", params = "acao=buscarOcorrencias")
    public String buscarOcorrencias(@ModelAttribute("boletim") BoletimRequisicao dto,
                                   BindingResult br,
                                   @ModelAttribute("fotos") FotosBean fotos,
                                   Model model,
                                   @RequestParam(name = "modoOcorrencia", defaultValue = "existente") String modoOcorrencia,
                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate buscarDataInicio,
                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate buscarDataFim,
                                   @RequestParam(required = false) OrigemAnimalEnum buscarOrigem,
                                   @RequestParam(required = false) Destino buscarDestino) {

        Long ongId = getOngIdLogada();
        LocalDateTime inicio = buscarDataInicio != null ? buscarDataInicio.atStartOfDay() : null;
        LocalDateTime fim = buscarDataFim != null ? buscarDataFim.atTime(LocalTime.MAX) : null;

        var ocorrencias = boletimService.buscarOcorrenciasParaVinculo(ongId, inicio, fim, buscarOrigem, buscarDestino, 50);

        prepararTelaCadastro(model, dto, fotos, modoOcorrencia, "ocorrencia");
        model.addAttribute("ocorrenciasEncontradas", ocorrencias);
        model.addAttribute("buscarDataInicio", buscarDataInicio);
        model.addAttribute("buscarDataFim", buscarDataFim);
        model.addAttribute("buscarOrigem", buscarOrigem);
        model.addAttribute("buscarDestino", buscarDestino);
        return "boletim/cadastro";
    }

    @PreAuthorize("hasAnyRole('COLABORADOR','ADMIN')")
    @GetMapping(value = "/selector")
    public String selectorOcorrencias(Model model,
                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate buscarDataInicio,
                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate buscarDataFim,
                                      @RequestParam(required = false) OrigemAnimalEnum buscarOrigem,
                                      @RequestParam(required = false) Destino buscarDestino,
                                      @RequestParam(required = false) Long numeroOcorrenciaSelecionada) {

        Long ongId = getOngIdLogada();
        LocalDateTime inicio = buscarDataInicio != null ? buscarDataInicio.atStartOfDay() : null;
        LocalDateTime fim = buscarDataFim != null ? buscarDataFim.atTime(LocalTime.MAX) : null;

        var ocorrencias = boletimService.buscarOcorrenciasParaVinculo(ongId, inicio, fim, buscarOrigem, buscarDestino, 50);
        model.addAttribute("ocorrenciasEncontradas", ocorrencias);
        model.addAttribute("numeroOcorrenciaSelecionada", numeroOcorrenciaSelecionada);
        return "boletim/selector :: selector";
    }

    @PreAuthorize("hasAnyRole('COLABORADOR','ADMIN')")
    @PostMapping(value = "/salvar", params = "acao=selecionarOcorrencia")
    public String selecionarOcorrencia(@ModelAttribute("boletim") BoletimRequisicao dto,
                                      BindingResult br,
                                      @ModelAttribute("fotos") FotosBean fotos,
                                      Model model,
                                      @RequestParam(name = "modoOcorrencia", defaultValue = "existente") String modoOcorrencia,
                                      @RequestParam(required = false) Long numeroOcorrenciaSelecionada,
                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate buscarDataInicio,
                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate buscarDataFim,
                                      @RequestParam(required = false) OrigemAnimalEnum buscarOrigem,
                                      @RequestParam(required = false) Destino buscarDestino) {

        Long ongId = getOngIdLogada();
        if (numeroOcorrenciaSelecionada == null) {
            model.addAttribute("mensagemErro", "Selecione uma ocorrência para vincular.");
        } else if (!boletimService.existsNumeroOcorrenciaNaOng(numeroOcorrenciaSelecionada, ongId)) {
            model.addAttribute("mensagemErro", "A ocorrência selecionada não existe (ou não pertence à sua ONG).");
        } else {
            dto.setNumeroOcorrencia(numeroOcorrenciaSelecionada);
        }

        // Recarrega a lista com os mesmos filtros (para não "sumir" após selecionar)
        LocalDateTime inicio = buscarDataInicio != null ? buscarDataInicio.atStartOfDay() : null;
        LocalDateTime fim = buscarDataFim != null ? buscarDataFim.atTime(LocalTime.MAX) : null;
        var ocorrencias = boletimService.buscarOcorrenciasParaVinculo(ongId, inicio, fim, buscarOrigem, buscarDestino, 50);

        prepararTelaCadastro(model, dto, fotos, modoOcorrencia, "ocorrencia");
        model.addAttribute("ocorrenciasEncontradas", ocorrencias);
        model.addAttribute("buscarDataInicio", buscarDataInicio);
        model.addAttribute("buscarDataFim", buscarDataFim);
        model.addAttribute("buscarOrigem", buscarOrigem);
        model.addAttribute("buscarDestino", buscarDestino);
        return "boletim/cadastro";
    }

    @PreAuthorize("hasAnyRole('COLABORADOR','ADMIN')")
    @PostMapping(value = "/salvar", params = {"!nav", "!acao"})
    public String createBoletim(@Valid @ModelAttribute("boletim") BoletimRequisicao dto,
                                BindingResult br,
                                RedirectAttributes redirectAttrs,
                                @ModelAttribute("fotos") FotosBean fotos,
                                Model model,
                                @RequestParam(name = "modoOcorrencia", defaultValue = "nova") String modoOcorrencia) {
        // validação server-side do TAB 1 (condicional por modo)
        validarTab1(dto, br, modoOcorrencia, getOngIdLogada());

        // Regras de negócio de "mãezinha"

        AnimalGenericoRequisicao a = dto.getAnimal();
        if (a != null && a.isAnimalEMaezinha()) {
            // Só FEMEA e não FILHOTE
            if (a.getSexo() != SexoEnum.FEMEA) {
                br.rejectValue("animal.animalEMaezinha", "invalid", "Apenas fêmeas podem ser mãezinhas.");
            }
            if (a.getMaturidade() == MaturidadeEnum.FILHOTE) {
                br.rejectValue("animal.animalEMaezinha", "invalid", "Filhote não pode ser mãezinha.");
            }
        } else {
            // Se não é mãezinha, zere quantidades para evitar sujeira
            if (a != null) {
                a.setMaezinhaComFilhotes(null);
            }
        }
        boolean vincularExistente = "existente".equalsIgnoreCase(modoOcorrencia);

        if (br.hasErrors()) {
            // Se houver erro no TAB 1, fica nele; senão, mostra TAB 2 para corrigir o Animal.
            String activeTab = hasTab1Errors(br) ? "ocorrencia" : "animal";
            prepararTelaCadastro(model, dto, fotos, modoOcorrencia, activeTab);
            return "boletim/cadastro";
        }

        BoletimResposta boletim;
        try {
            if (vincularExistente) {
                boletim = boletimService.adicionarAnimalEmOcorrencia(dto.getNumeroOcorrencia(), dto.getAnimal());
            } else {
                boletim = boletimService.createBoletim(dto);
            }
        } catch (Exception e) {
            prepararTelaCadastro(model, dto, fotos, modoOcorrencia, "animal");
            model.addAttribute("mensagemErro", e.getMessage());
            return "boletim/cadastro";
        }

        if (fotos != null && fotos.getArquivos() != null) {
            fotos.getArquivos().stream()
                    .filter(f -> f != null && !f.isEmpty())
                    .forEach(file -> {
                        mediaService.uploadAndLink(
                                file,
                                MediaTargetType.ANIMAL,
                                boletim.getAnimal().getId(),
                                MediaUsage.RESGATE,
                                0
                        );
                    });
        }

        redirectAttrs.addFlashAttribute("cadastroSucesso", true);
        return "redirect:/animais/" + boletim.getAnimal().getId();
    }

    private void prepararTelaCadastro(Model model,
                                     BoletimRequisicao dto,
                                     FotosBean fotos,
                                     String modoOcorrencia,
                                     String activeTab) {
        garantirBindingAninhado(dto);
        model.addAttribute("boletim", dto);
        model.addAttribute("fotos", fotos != null ? fotos : new FotosBean(List.of()));
        model.addAttribute("modoOcorrencia", modoOcorrencia);
        model.addAttribute("activeTab", activeTab);
        model.addAttribute("currentPage", "/boletins/form");
        carregarCombos(model);
    }

    private void validarTab1(BoletimRequisicao dto, BindingResult br, String modoOcorrencia, Long ongId) {
        boolean vincularExistente = "existente".equalsIgnoreCase(modoOcorrencia);
        if (vincularExistente) {
            if (dto.getNumeroOcorrencia() == null) {
                br.rejectValue("numeroOcorrencia", "required", "Informe o número da ocorrência existente.");
            } else if (ongId != null && !boletimService.existsNumeroOcorrenciaNaOng(dto.getNumeroOcorrencia(), ongId)) {
                br.rejectValue("numeroOcorrencia", "notFound", "Ocorrência não encontrada para esta ONG.");
            }
            return;
        }

        if (dto.getDataAtendimento() == null) {
            br.rejectValue("dataAtendimento", "required", "Data de Atendimento é obrigatória.");
        }
        if (dto.getDestino() == null) {
            br.rejectValue("destino", "required", "Destino é obrigatório.");
        }
    }

    private boolean hasTab1Errors(BindingResult br) {
        return br.hasFieldErrors("numeroOcorrencia")
                || br.hasFieldErrors("dataAtendimento")
                || br.hasFieldErrors("destino")
                || br.hasFieldErrors("origem")
                || br.hasFieldErrors("motivoRecolhimento")
                || br.hasFieldErrors("nomeDenuncianteOuTutor")
                || br.hasFieldErrors("cpfDenuncianteOuTutor")
                || br.hasFieldErrors("telefoneDenuncianteOuTutor")
                || br.hasFieldErrors("observacaoClinica")
                || br.hasFieldErrors("ruaAvenida")
                || br.hasFieldErrors("bairro")
                || br.hasFieldErrors("cidade")
                || br.hasFieldErrors("estado")
                || br.hasFieldErrors("municipio");
    }

    private Long getOngIdLogada() {
        Usuario u = SecurityUtils.requirePrincipal(Usuario.class);
        return (u.getOng() != null) ? u.getOng().getId() : null;
    }

    private void garantirBindingAninhado(BoletimRequisicao req) {
        if (req == null) return;
        if (req.getAnimal() == null) req.setAnimal(new AnimalDTO());
        if (req.getAnimal().getMaezinhaComFilhotes() == null) {
            req.getAnimal().setMaezinhaComFilhotes(new MaezinhaComFilhotesDTO());
        }
    }

    /**
     * API para anexar mais um animal ao mesmo boletim/ocorrência.
     * Útil quando um resgate teve múltiplos animais (1 ocorrência, N animais).
     */
    @PreAuthorize("hasAnyRole('COLABORADOR','ADMIN')")
    @PostMapping(value = "/{numeroOcorrencia}/animais")
    public ResponseEntity<BoletimResposta> adicionarAnimal(@PathVariable Long numeroOcorrencia,
                                                             @RequestBody AnimalGenericoRequisicao animal) {
        Long ongId = getOngIdLogada();
        if (ongId != null && !boletimService.existsNumeroOcorrenciaNaOng(numeroOcorrencia, ongId)) {
            throw new org.ong.pet.pex.backendpetx.service.exceptions.PetXException("Ocorrência não encontrada para esta ONG");
        }
        return ResponseEntity.ok(boletimService.adicionarAnimalEmOcorrencia(numeroOcorrencia, animal));
    }

    @PreAuthorize("hasAnyRole('COLABORADOR','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<BoletimResposta> getBoletim(@PathVariable Long id) {
        return ResponseEntity.ok(boletimService.getBoletim(id));
    }

    @PreAuthorize("hasAnyRole('COLABORADOR','ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<BoletimResposta> updateBoletim(@PathVariable Long id,
                                                            @RequestBody BoletimRequisicao dto) {
        return ResponseEntity.ok(boletimService.updateBoletim(id, dto));
    }

    @PreAuthorize("hasAnyRole('COLABORADOR','ADMIN')")
    @GetMapping
    public String listarBoletins(
            @RequestParam(required = false) Long numeroOcorrencia,
            @RequestParam(required = false) Destino destino,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            Model model) {

        Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size,
                org.springframework.data.domain.Sort.by("dataAtendimento").descending());

        Page<BoletimResposta> boletins = boletimService.findAllBoletins(
                numeroOcorrencia, destino, pageable);

        model.addAttribute("boletins", boletins);
        model.addAttribute("filtroNumero", numeroOcorrencia);
        model.addAttribute("filtroDestino", destino);
        model.addAttribute("destinos", Destino.values());
        model.addAttribute("currentPage", "/boletins");

        PageInfoBean pageInfo = PageInfoBean.builder()
                .title("Boletins de Resgate")
                .subtitle("Registros de resgates e atendimentos da ONG")
                .icon("fas fa-file-medical")
                .addBreadcrumb("Boletins", "/boletins");
        model.addAttribute("pageInfo", pageInfo);

        List<ActionButtonDTO> buttons = List.of(
                ActionButtonDTO.primary("Novo Boletim", "/boletins/form", "fas fa-plus")
        );
        model.addAttribute("actionButtons", buttons);

        return "boletim/lista";
    }

    @PreAuthorize("hasAnyRole('COLABORADOR','ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoletim(@PathVariable Long id) {
        boletimService.deleteBoletim(id);
        return ResponseEntity.noContent().build();
    }

    private void carregarCombos(Model model) {
        model.addAttribute("destinos", Destino.values());
        
        // Sort OrigemAnimalEnum values alphabetically by origemAnimal property
        model.addAttribute("origens", Arrays.stream(OrigemAnimalEnum.values())
                .sorted(Comparator.comparing(OrigemAnimalEnum::getOrigemAnimal))
                .toArray(OrigemAnimalEnum[]::new));
        
        // Sort EspecieEnum values alphabetically by especie property
        model.addAttribute("especies", Arrays.stream(EspecieEnum.values())
                .sorted(Comparator.comparing(EspecieEnum::getEspecie))
                .toArray(EspecieEnum[]::new));
        
        // Sort SexoEnum values alphabetically by sexo property
        model.addAttribute("sexos", Arrays.stream(SexoEnum.values())
                .sorted(Comparator.comparing(SexoEnum::getSexo))
                .toArray(SexoEnum[]::new));
        
        // Sort MaturidadeEnum values alphabetically by maturidade property
        model.addAttribute("maturidades", Arrays.stream(MaturidadeEnum.values())
                .sorted(Comparator.comparing(MaturidadeEnum::getMaturidade))
                .toArray(MaturidadeEnum[]::new));
        
        // Sort PorteEnum values alphabetically by porte property
        model.addAttribute("portes", Arrays.stream(PorteEnum.values())
                .sorted(Comparator.comparing(PorteEnum::getPorte))
                .toArray(PorteEnum[]::new));
        
        // Sort StatusEnum values alphabetically by status property
        model.addAttribute("statusEnum", Arrays.stream(SaudeEnum.values())
                .sorted(Comparator.comparing(SaudeEnum::getStatus))
                .toArray(SaudeEnum[]::new));
    }
}
