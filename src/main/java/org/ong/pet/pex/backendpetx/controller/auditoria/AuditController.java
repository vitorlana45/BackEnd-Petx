// AuditController.java
package org.ong.pet.pex.backendpetx.controller.auditoria;

import lombok.RequiredArgsConstructor;

import org.ong.pet.pex.backendpetx.controller.helper.SmartPageHelper;
import org.ong.pet.pex.backendpetx.service.AuditService;
import org.ong.pet.pex.backendpetx.dto.RevisaoLinhaDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auditoria")
@PreAuthorize("hasAnyRole('DIRETORIA','ADMIN')") // ajuste as roles
@RequiredArgsConstructor
public class AuditController {

  private final AuditService audit;

  // Timeline do animal
  @GetMapping("/animais/{id}")
  public String historicoAnimal(@PathVariable Long id,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "20") int size,
                                Model model) {

    Pageable pageable = PageRequest.of(Math.max(page,0), Math.min(size, 100));
    var timeline = audit.listAnimalHistory(id, pageable);

    // Header da página (reaproveitando seus helpers)
    SmartPageHelper.setupPageWithSingleButton(
      model,
      "Histórico do Animal",
      "Revisões, ações e alterações registradas",
      "fas fa-history",
      "Voltar",
      "/animais/" + id,
      "fas fa-arrow-left"
    );

    model.addAttribute("animalId", id);
    model.addAttribute("page", timeline);
    return "auditoria/animal-historico";
  }

  // Modal: detalhes da revisão (snapshot) + diff p/ anterior
  @GetMapping("/animais/{id}/rev/{rev}")
  public String detalheRevisao(@PathVariable Long id,
                               @PathVariable Long rev,
                               @RequestParam(required = false) Long prev, // opcional p/ diff
                               Model model) {
    var snap = audit.snapshotAt(id, rev);
    model.addAttribute("snap", snap);
    model.addAttribute("rev", rev);

    if (prev != null && prev > 0) {
      var diffs = audit.diffAnimal(id, prev, rev);
      model.addAttribute("diffs", diffs);
      model.addAttribute("prev", prev);
    }

    return "auditoria/fragments :: revisaoDetalhe"; // fragmento thymeleaf
  }
}
