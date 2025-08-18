package org.ong.pet.pex.backendpetx.controllers;

import jakarta.validation.Valid;
import org.ong.pet.pex.backendpetx.dto.request.AnimalDTO;
import org.ong.pet.pex.backendpetx.dto.request.AnimalGenericoRequisicao;
import org.ong.pet.pex.backendpetx.dto.request.BoletimDTORequisicao;
import org.ong.pet.pex.backendpetx.dto.request.MaezinhaComFilhotesDTO;
import org.ong.pet.pex.backendpetx.dto.response.BoletimDTOResposta;
import org.ong.pet.pex.backendpetx.enums.*;
import org.ong.pet.pex.backendpetx.service.BoletimService;
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
import java.util.Arrays;
import java.util.Comparator;

@Controller
@RequestMapping("/boletins")
public class BoletimController {

    private final BoletimService boletimService;

    public BoletimController(BoletimService boletimService) {
        this.boletimService = boletimService;
    }

    @PreAuthorize("hasAnyRole('COLABORADOR','ADMIN')")
    @GetMapping("/novo")
    public String novo(Model model) {
        BoletimDTORequisicao req = new BoletimDTORequisicao();

        // evita NPE no binding aninhado
        if (req.getAnimal() == null) req.setAnimal(new AnimalDTO());
        if (req.getAnimal().getMaezinhaComFilhotes() == null) {
            req.getAnimal().setMaezinhaComFilhotes(new MaezinhaComFilhotesDTO());
        }

        model.addAttribute("boletim", req);
        model.addAttribute("currentPage", "/boletins/novo");
        carregarCombos(model);
        return "boletim/cadastro";
    }

    @PreAuthorize("hasAnyRole('COLABORADOR','ADMIN')")
    @PostMapping("/criar")
    public String createBoletim(@Valid @ModelAttribute("boletim") BoletimDTORequisicao dto,
                                BindingResult br,
                                RedirectAttributes redirectAttrs,
                                Model model) {
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

        if (br.hasErrors()) {
            carregarCombos(model);
            model.addAttribute("currentPage", "/boletins/novo");
            return "boletim/cadastro";
        }

        boletimService.createBoletim(dto);
        redirectAttrs.addFlashAttribute("cadastroSucesso", true);
        return "redirect:/animais"; // PRG
    }

    @PreAuthorize("hasAnyRole('COLABORADOR','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<BoletimDTOResposta> getBoletim(@PathVariable Long id) {
        return ResponseEntity.ok(boletimService.getBoletim(id));
    }

    @PreAuthorize("hasAnyRole('COLABORADOR','ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<BoletimDTOResposta> updateBoletim(@PathVariable Long id,
                                                            @RequestBody BoletimDTORequisicao dto) {
        return ResponseEntity.ok(boletimService.updateBoletim(id, dto));
    }

    @PreAuthorize("hasAnyRole('COLABORADOR','ADMIN')")
    @GetMapping
    public ResponseEntity<Page<BoletimDTOResposta>> findAllBoletins(
            @RequestParam(required = false) Long numeroOcorrencia,
            @RequestParam(required = false) Destino destino,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim,
            Pageable pageable) {

        Page<BoletimDTOResposta> boletins = boletimService.findAllBoletins(
                numeroOcorrencia, destino, pageable);
        return ResponseEntity.ok(boletins);
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
        model.addAttribute("statusEnum", Arrays.stream(StatusEnum.values())
                .sorted(Comparator.comparing(StatusEnum::getStatus))
                .toArray(StatusEnum[]::new));
    }
}
