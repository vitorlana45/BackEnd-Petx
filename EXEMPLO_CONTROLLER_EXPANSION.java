// Exemplo de expansão do Controller para dados reais

// No AuthWebController.java, método dashboard:

// 1. Para buscar lista de animais:
List<AnimalDTO> animais = animalService.listarAnimaisRecentes(10);
model.addAttribute("animais", animais);

// 2. Para alertas críticos:
List<AlertaDTO> alertas = Arrays.asList(
    new AlertaDTO("Estoque Baixo", "Ração para gatos está acabando", "warning", "exclamation-triangle"),
    new AlertaDTO("Consulta Pendente", "3 pets aguardam consulta veterinária", "info", "stethoscope")
);
model.addAttribute("alertasCriticos", alertas);

// 3. Para últimas adoções:
List<AdocaoDTO> ultimasAdocoes = adocaoService.buscarUltimasAdocoes(5);
model.addAttribute("ultimosAdotados", ultimasAdocoes);

// 4. DTO de exemplo para Animal:
public record AnimalDTO(
    Long id,
    String nome,
    String especie,
    String idade,
    String porte,
    String raca,
    String status,
    String tutor
) {}

// 5. DTO de exemplo para Alerta:
public record AlertaDTO(
    String titulo,
    String mensagem,
    String tipo,
    String icone
) {}

// 6. DTO de exemplo para Adoção:
public record AdocaoDTO(
    Long id,
    AnimalDTO animal,
    LocalDate dataAdocao
) {}
