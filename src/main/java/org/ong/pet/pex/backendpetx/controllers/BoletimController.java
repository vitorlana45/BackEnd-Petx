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
            if (a != null && a.getMaezinhaComFilhotes() != null) {
                a.getMaezinhaComFilhotes().setQuantidadeFemea(null);
                a.getMaezinhaComFilhotes().setQuantidadeMacho(null);
            }
        }

        if (br.hasErrors()) {
            carregarCombos(model);
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


    private String validacaoAnimalMaezinho(AnimalDTO animalDTO){

            if(animalDTO.getMaturidade().equals(MaturidadeEnum.FILHOTE)) {
                return "Filhote não pode ser maezinha.";
            }

            if(!animalDTO.getSexo().equals(SexoEnum.FEMEA)){
                return "Apenas fêmeas podem ser maezinhas.";
            }
            return null;
    }

    private void carregarCombos(Model model) {
        model.addAttribute("destinos", Destino.values());
        model.addAttribute("origens", OrigemAnimalEnum.values());
        model.addAttribute("especies", EspecieEnum.values());
        model.addAttribute("sexos", SexoEnum.values());
        model.addAttribute("maturidades", MaturidadeEnum.values());
        model.addAttribute("portes", PorteEnum.values());
        model.addAttribute("statusEnum", StatusEnum.values());
    }
}
