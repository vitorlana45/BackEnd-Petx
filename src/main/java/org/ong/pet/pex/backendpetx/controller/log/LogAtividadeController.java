package org.ong.pet.pex.backendpetx.controller.log;

import lombok.RequiredArgsConstructor;
import org.ong.pet.pex.backendpetx.entity.LogAtividade;
import org.ong.pet.pex.backendpetx.service.impl.LogAtividadeService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class LogAtividadeController {

  private final LogAtividadeService service;

  // Para widget na home (render Thymeleaf)
  @GetMapping("/_widgets/atividades/recentes")
  public String widgetAtividades(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 Model model) {
    Page<LogAtividade> pagina = service.listarRecentes(page, size);
    model.addAttribute("atividades", pagina);
    return "widgets/atividades-recentes :: root"; // fragmento abaixo
  }

  // Endpoint JSON para Postman (opcional)
  @GetMapping("/api/atividades/recentes")
  @ResponseBody
  public ResponseEntity<Page<LogAtividade>> apiAtividades(@RequestParam(defaultValue="0") int page,
                                                          @RequestParam(defaultValue="10") int size) {
    return ResponseEntity.ok(service.listarRecentes(page, size));
  }
}
