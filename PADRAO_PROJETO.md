# Padrão de Projeto — PetX Backend

> **Este documento é a fonte de verdade para toda decisão arquitetural e de código no projeto PetX.**
> Antes de implementar qualquer feature ou refatoração, consulte e siga estas diretrizes.
> Ao identificar divergências, registre e planeje a correção.

---

## Índice

1. [Stack & Tecnologias](#stack--tecnologias)
2. [Estrutura de Pacotes](#estrutura-de-pacotes)
3. [Convenções de Nomenclatura](#convenções-de-nomenclatura)
4. [Camada de Entidades](#camada-de-entidades)
5. [Camada de Repositórios](#camada-de-repositórios)
6. [Camada de Serviços](#camada-de-serviços)
7. [Camada de Controllers](#camada-de-controllers)
8. [DTOs — Data Transfer Objects](#dtos--data-transfer-objects)
9. [Tratamento de Exceções](#tratamento-de-exceções)
10. [Templates Thymeleaf](#templates-thymeleaf)
11. [Segurança e Autorização](#segurança-e-autorização)
12. [Transações e Leitura](#transações-e-leitura)
13. [Roadmap de Refatoração por Escopo](#roadmap-de-refatoração-por-escopo)

---

## Stack & Tecnologias

| Camada | Tecnologia | Versão |
|--------|-----------|--------|
| Runtime | Java | 21 |
| Framework | Spring Boot | 3.3.5 |
| ORM | Hibernate / Spring Data JPA | Spring Boot managed |
| Banco | PostgreSQL | latest |
| Segurança | Spring Security 6 + JWT (Auth0) | 4.4.0 |
| Frontend | Thymeleaf + HTMX | Spring Boot managed |
| Upload | MinIO | 8.5.16 |
| Cache | Caffeine | Spring Boot managed |
| Auditoria | Hibernate Envers | Spring Boot managed |
| Build | Maven | 3 |
| Utilitários | Lombok | Spring Boot managed |
| Documentação | SpringDoc OpenAPI (Swagger) | 2.2.0 |

---

## Estrutura de Pacotes

```
org.ong.pet.pex.backendpetx/
├── config/                         # Beans de configuração Spring (@Configuration)
├── controllers/                    # TODOS os controllers (web + rest)
│   ├── {dominio}/                  # Subpacote por domínio (animal, tutor, estoque...)
│   │   ├── {Dominio}Controller.java    # Controller MVC (Thymeleaf)
│   │   └── {Dominio}RestController.java # Controller REST (API JSON)
│   ├── admin/
│   ├── auth/
│   └── exceptions/                 # GlobalExceptionHandler
├── dto/
│   ├── request/                    # Todos os DTOs de entrada (formulários/API)
│   └── response/                   # Todos os DTOs de saída
├── entities/                       # Entidades JPA
├── enums/                          # Enums do domínio
├── repositories/                   # Interfaces JPA Repository
│   └── specs/                      # Specifications para queries dinâmicas
├── service/                        # Interfaces de serviço
│   ├── impl/                       # Implementações de serviço
│   ├── mappers/                    # Classes de mapeamento DTO <-> Entity
│   └── exceptions/                 # Exceções de domínio/negócio
├── infra/                          # Infraestrutura (MinIO, email, cache, etc.)
└── security/                       # Configuração de segurança + filtros JWT
```

### Regras do pacote

- **Nunca** misturar controllers REST e MVC no mesmo arquivo.
- **Nunca** ter mais de uma pasta raiz para controllers (`controller/` e `controllers/` é proibido).
- Cada domínio tem seu próprio subpacote dentro de `controllers/`.
- Especificações JPA ficam em `repositories/specs/`, não embutidas no repositório.

---

## Convenções de Nomenclatura

### Idioma

> **Regra geral:** Código em **inglês**, mensagens ao usuário em **português**.

| Elemento | Idioma | Exemplo |
|----------|--------|---------|
| Classes, métodos, campos | Inglês | `Animal`, `findById`, `chipId` |
| Nomes de tabelas/colunas | Português (snake_case) | `animal_tb`, `chip_id` |
| Variáveis de template Thymeleaf | Português | `animais`, `mensagemErro` |
| Mensagens de erro/sucesso | Português | `"Animal não encontrado"` |
| Nomes de enum (constantes) | Inglês (UPPER_CASE) | `DOG`, `CAT`, `SMALL` |

### Classes por Camada

| Camada | Sufixo | Exemplo |
|--------|--------|---------|
| Controller MVC | `Controller` | `AnimalController` |
| Controller REST | `RestController` | `AnimalRestController` |
| Service (interface) | `Service` | `AnimalService` |
| Service (impl) | `ServiceImpl` | `AnimalServiceImpl` |
| Repository | `Repository` | `AnimalRepository` (singular, nunca plural) |
| Entidade | sem sufixo | `Animal`, `Tutor`, `Estoque` |
| DTO entrada | `Request` | `CreateAnimalRequest`, `UpdateAnimalRequest` |
| DTO saída | `Response` | `AnimalResponse`, `AnimalPageResponse` |
| Form DTO (MVC binding) | `Form` | `AnimalForm` (apenas se diferente de Request) |
| Mapper | `Mapper` | `AnimalMapper` |
| Enum | `Type` ou sem sufixo | `SpeciesType`, `HealthStatus` |
| Exception de domínio | `Exception` | `AnimalNotFoundException` |
| Specification | `Spec` | `AnimalSpec` |

### Métodos de Serviço

Usar verbos em inglês, descritivos e consistentes:

| Operação | Padrão | Exemplo |
|----------|--------|---------|
| Buscar por ID | `findById` | `findById(Long id)` |
| Buscar lista/página | `findAll` / `findAllPaged` | `findAllPaged(Pageable p)` |
| Criar | `create` | `create(CreateAnimalRequest req)` |
| Atualizar | `update` | `update(Long id, UpdateAnimalRequest req)` |
| Deletar | `delete` | `delete(Long id)` |
| Verificar existência | `existsBy...` | `existsByChipId(String chipId)` |
| Operações de negócio | verbo descritivo em inglês | `declareObito(Long id, ObitoRequest req)` |

---

## Camada de Entidades

### Regras

1. Toda entidade herda de `EntidadeBase` (campos `criadoEm`, `atualizadoEm`).
2. Usar `@Audited` em todas as entidades que necessitam de histórico.
3. Soft delete via `@SQLDelete` + `@Where` — **nunca** deletar fisicamente em produção.
4. Relacionamentos `@ManyToOne` sempre `FetchType.LAZY`.
5. `@ElementCollection` sempre `FetchType.LAZY`.
6. Não adicionar lógica de negócio em entidades (apenas setters/getters + validações simples de invariantes).
7. Usar `@Builder` + `@NoArgsConstructor` + `@AllArgsConstructor` com Lombok.
8. Evitar `@Data` em entidades (causa problemas com Hibernate lazy loading e equals/hashCode) — usar `@Getter` + `@Setter` explícitos.

### Template de Entidade

```java
@Entity
@Table(name = "animal_tb")
@Audited
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE animal_tb SET archived = true WHERE id = ?")
@Where(clause = "archived = false")
public class Animal extends EntidadeBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ong_id", nullable = false)
    private Ong ong;

    @Builder.Default
    private boolean archived = false;
}
```

---

## Camada de Repositórios

### Regras

1. Todas as interfaces extendem `JpaRepository<E, Long>`.
2. Adicionar `JpaSpecificationExecutor<E>` apenas quando houver filtros dinâmicos.
3. Nomear o arquivo sempre no singular: `AnimalRepository`, **não** `AnimaisRepository`.
4. Queries complexas: preferir `@Query` JPQL. Usar `nativeQuery = true` apenas quando JPQL não for suficiente.
5. Projections (interfaces de leitura parcial) ficam no pacote `dto/response/` como interfaces.
6. Specifications ficam em `repositories/specs/{Dominio}Spec.java`.

### Template de Repositório

```java
@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long>, JpaSpecificationExecutor<Animal> {

    @EntityGraph(attributePaths = {"tutors", "diseases", "ong"})
    Optional<Animal> findWithDetailsById(Long id);

    @Query("SELECT a FROM Animal a WHERE a.chipId = :chipId")
    Optional<Animal> findByChipId(@Param("chipId") String chipId);

    boolean existsByChipId(String chipId);
}
```

---

## Camada de Serviços

### Regras

1. Toda funcionalidade exposta ao controller passa por uma interface de serviço.
2. Implementação **sempre** no pacote `service/impl/`.
3. `@Transactional` no nível da classe (para métodos de escrita). Métodos de leitura devem ter `@Transactional(readOnly = true)` explicitamente.
4. O serviço recebe e retorna **DTOs**, nunca entidades diretamente ao controller.
5. Validações de negócio ficam no serviço, **nunca** no controller.
6. Mapeamento DTO <-> Entity delega para classe estática `{Dominio}Mapper`.
7. Lançar sempre exceções específicas de domínio (ver seção de exceções).

### Template de Serviço

```java
// Interface
public interface AnimalService {
    AnimalResponse findById(Long id);
    Page<AnimalResponse> findAllPaged(AnimalFilterRequest filter, Pageable pageable);
    AnimalResponse create(CreateAnimalRequest request);
    AnimalResponse update(Long id, UpdateAnimalRequest request);
    void delete(Long id);
}

// Implementação
@Service
@RequiredArgsConstructor
public class AnimalServiceImpl implements AnimalService {

    private final AnimalRepository animalRepository;

    @Override
    @Transactional(readOnly = true)
    public AnimalResponse findById(Long id) {
        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new AnimalNotFoundException(id));
        return AnimalMapper.toResponse(animal);
    }

    @Override
    @Transactional
    public AnimalResponse create(CreateAnimalRequest request) {
        // validação de negócio aqui
        Animal animal = AnimalMapper.toEntity(request);
        return AnimalMapper.toResponse(animalRepository.save(animal));
    }
}
```

---

## Camada de Controllers

### Separação MVC × REST

> **Regra obrigatória:** Um controller nunca mistura retorno de views Thymeleaf com retorno de JSON.

| Tipo | Anotação | Retorna | URL pattern |
|------|----------|---------|-------------|
| MVC Web | `@Controller` | `String` (nome da view) ou `redirect:` | `/animais/**` |
| REST API | `@RestController` | `ResponseEntity<DTO>` | `/api/animais/**` |

### Responsabilidades do Controller MVC

- Receber input do formulário (`@ModelAttribute`)
- Validar via `@Valid` + `BindingResult`
- Delegar lógica ao `@Service`
- Adicionar atributos ao `Model`
- Redirecionar ou renderizar a view correta
- Tratar requisições HTMX (detectar via `HX-Request` header)

### Template Controller MVC

```java
@Controller
@RequestMapping("/animais")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'COLLABORATOR')")
public class AnimalController {

    private final AnimalService animalService;

    @GetMapping
    public String listAnimals(@ModelAttribute AnimalFilterRequest filter,
                               Pageable pageable,
                               Model model) {
        model.addAttribute("animais", animalService.findAllPaged(filter, pageable));
        return "animais/lista";
    }

    @PostMapping("/{id}/update")
    public String updateAnimal(@PathVariable Long id,
                                @Valid @ModelAttribute UpdateAnimalRequest request,
                                BindingResult result,
                                Model model,
                                HttpServletRequest httpRequest,
                                HttpServletResponse httpResponse) {
        if (result.hasErrors()) {
            if (isHtmxRequest(httpRequest)) {
                httpResponse.setStatus(422);
                model.addAttribute("mensagemErro", "Corrija os campos indicados.");
                return "fragmentos/messages :: modalMessages";
            }
            return "animais/formulario";
        }
        animalService.update(id, request);
        return "redirect:/animais/" + id;
    }

    private boolean isHtmxRequest(HttpServletRequest request) {
        return "true".equals(request.getHeader("HX-Request"));
    }
}
```

### Template Controller REST

```java
@RestController
@RequestMapping("/api/animais")
@RequiredArgsConstructor
@Tag(name = "Animals", description = "Animal management API")
public class AnimalRestController {

    private final AnimalService animalService;

    @GetMapping("/{id}")
    @Operation(summary = "Get animal by ID")
    public ResponseEntity<AnimalResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(animalService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<AnimalResponse>> getAll(AnimalFilterRequest filter, Pageable pageable) {
        return ResponseEntity.ok(animalService.findAllPaged(filter, pageable));
    }
}
```

---

## DTOs — Data Transfer Objects

### Organização de Pacotes

```
dto/
├── request/    # Todos os DTOs de entrada (formulário ou API)
└── response/   # Todos os DTOs de saída
```

> **Nenhum DTO fica fora desses dois pacotes.** Sub-pastas por domínio dentro de `request/` e `response/` são permitidas para domínios com muitos DTOs.

### Nomenclatura

| Situação | Padrão | Exemplo |
|----------|--------|---------|
| Criar recurso | `Create{Dominio}Request` | `CreateAnimalRequest` |
| Atualizar recurso | `Update{Dominio}Request` | `UpdateAnimalRequest` |
| Filtrar/buscar | `{Dominio}FilterRequest` | `AnimalFilterRequest` |
| Resposta genérica | `{Dominio}Response` | `AnimalResponse` |
| Resposta paginada | `{Dominio}PageResponse` | `AnimalPageResponse` (se diferente do Response) |
| Resposta resumida | `{Dominio}SummaryResponse` | `AnimalSummaryResponse` |

### Validações

- `@NotNull`, `@NotBlank`, `@Size`, `@Min`, `@Max` nos campos de Request.
- Nunca validar em entidades — validação é responsabilidade do DTO de entrada.
- Usar `record` para DTOs simples (Java 21 suporta).

```java
// Request usando record
public record CreateAnimalRequest(
    @NotBlank String name,
    @NotNull SpeciesType species,
    @NotNull SizeType size,
    String chipId
) {}

// Response usando record
public record AnimalResponse(
    Long id,
    String name,
    SpeciesType species,
    SizeType size,
    String chipId,
    LocalDateTime createdAt
) {}
```

---

## Tratamento de Exceções

### Hierarquia de Exceções

```
RuntimeException
└── BaseApplicationError          # Raiz de todas as exceções de negócio
    ├── ResourceNotFoundException  # 404 - recurso não encontrado
    │   ├── AnimalNotFoundException
    │   ├── TutorNotFoundException
    │   └── ...
    ├── BusinessRuleException      # 422 - regra de negócio violada
    │   ├── DuplicateChipException
    │   └── ...
    └── ValidationException        # 400 - validação de entrada
```

### Regras

1. **Nunca** lançar `RuntimeException` diretamente no código de negócio.
2. Toda exceção de domínio estende `BaseApplicationError`.
3. `GlobalExceptionHandler` mapeia exceções para respostas HTTP corretas.
4. Mensagens de erro ficam em `messages.properties` (nunca hardcoded).
5. Para requisições HTMX, retornar fragmento `fragmentos/messages :: modalMessages`.
6. Para páginas normais, adicionar `mensagemErro` ao `Model` e renderizar a view.
7. Para REST API, retornar `ResponseEntity` com corpo padronizado.

### Corpo de Resposta de Erro (REST)

```java
public record ErrorResponse(
    int status,
    String error,
    String message,
    LocalDateTime timestamp,
    String path
) {}
```

### Template de Exceção de Domínio

```java
public class AnimalNotFoundException extends ResourceNotFoundException {
    public AnimalNotFoundException(Long id) {
        super("animal.not.found", id);
    }
}
```

---

## Templates Thymeleaf

### Estrutura de Diretórios

```
templates/
├── layouts/
│   ├── master.html          # Layout base (HTML + head + nav + scripts)
│   ├── list.html            # Layout para páginas de listagem
│   └── form.html            # Layout para páginas de formulário
├── fragments/               # Fragmentos reutilizáveis
│   ├── messages.html        # Mensagens: success/error/warning/info (page + modal)
│   ├── pagination.html      # Componente de paginação
│   ├── header.html          # Cabeçalho
│   ├── sidebar.html         # Menu lateral
│   └── modal.html           # Estrutura base de modal
├── components/              # Componentes de UI reutilizáveis
│   ├── status-badge.html    # Badge de status
│   └── cards.html           # Cards de estatísticas
├── animais/
│   ├── lista.html           # Listagem de animais
│   ├── perfil.html          # Detalhes/perfil do animal
│   └── formulario.html      # Formulário de criação/edição
├── tutores/
├── estoque/
├── auth/
├── admin/
└── dashboard/
    └── index.html
```

### Regras de Nomenclatura de Templates

- Arquivos em **português**, kebab-case: `lista.html`, `formulario.html`, `perfil.html`.
- **Proibido** ter versões duplicadas: `lista-refatorada.html`, `lista-backup.html` — deletar após migração.
- Cada domínio tem no máximo: `lista.html`, `formulario.html`, `perfil.html` (e `detalhes.html` se necessário).
- Fragmentos têm nomes no plural: `fragments` → `fragmentos/`.

### Sistema de Mensagens nos Templates

```html
<!-- Em páginas normais -->
<div th:replace="~{fragmentos/messages :: messages}"></div>

<!-- Em modais com HTMX -->
<div id="modalAlerts-nomeDoModal">
    <div th:replace="~{fragmentos/messages :: modalMessages}"></div>
</div>
<form hx-target="#modalAlerts-nomeDoModal" hx-swap="innerHTML">
```

### Variáveis de Modelo para Mensagens

| Variável | Tipo | Uso |
|----------|------|-----|
| `mensagemSucesso` | String | Operação bem-sucedida |
| `mensagemErro` | String | Erro de negócio ou validação |
| `mensagemAviso` | String | Aviso não crítico |
| `mensagemInfo` | String | Informação ao usuário |

---

## Segurança e Autorização

### Roles do Sistema

| Role | Descrição |
|------|-----------|
| `ADMIN` | Acesso total |
| `COLLABORATOR` | Acesso operacional (sem gestão de usuários) |

### Regras

1. Usar `@PreAuthorize` no nível do **controller**, nunca no serviço.
2. Padrão padrão: `@PreAuthorize("hasAnyRole('ADMIN', 'COLLABORATOR')")` para rotas protegidas.
3. Rotas de admin exclusivo: `@PreAuthorize("hasRole('ADMIN')")`.
4. Configuração geral de Security em `config/SecurityConfig.java`.
5. Credenciais **nunca** no `application.properties` — usar variáveis de ambiente ou `application-secrets.properties` (no `.gitignore`).

---

## Transações e Leitura

### Regras

1. `@Transactional` na **classe** da implementação do serviço (escrita por padrão).
2. Todo método de leitura (`find*`, `get*`, `list*`, `exists*`) deve ter `@Transactional(readOnly = true)`.
3. Nunca colocar `@Transactional` no controller.
4. Métodos no repositório não precisam de `@Transactional` (gerenciado pelo Spring Data).

```java
@Service
@RequiredArgsConstructor
@Transactional  // padrão para escrita
public class AnimalServiceImpl implements AnimalService {

    @Override
    @Transactional(readOnly = true)  // sobrescreve para leitura
    public AnimalResponse findById(Long id) { ... }

    @Override
    @Transactional(readOnly = true)
    public Page<AnimalResponse> findAllPaged(...) { ... }

    @Override  // herda @Transactional da classe
    public AnimalResponse create(CreateAnimalRequest request) { ... }
}
```

---

## Roadmap de Refatoração por Escopo

> Seguir essa ordem. Cada escopo deve ser concluído e testado antes do próximo.

### Escopo 0 — Limpeza e Preparação *(Pré-condição)*

- [ ] Remover arquivos duplicados de templates (`*-refatorado.html`, `*-backup.html`, `*-simples.html`)
- [ ] Unificar pasta `controller/` e `controllers/` em uma única pasta `controllers/`
- [ ] Remover `TesteController` e templates de teste
- [ ] Garantir `.gitignore` com `application-secrets.properties`, `*.env`

### Escopo 1 — Fundação: Exceções e DTOs

- [ ] Criar hierarquia de exceções (`BaseApplicationError`, `ResourceNotFoundException`, `BusinessRuleException`)
- [ ] Migrar todas as exceções existentes para a nova hierarquia
- [ ] Atualizar `GlobalExceptionHandler` para nova hierarquia
- [ ] Padronizar todos os DTOs para sufixos `Request` / `Response`
- [ ] Mover todos os DTOs para `dto/request/` e `dto/response/`
- [ ] Migrar para `record` onde aplicável (DTOs simples)

### Escopo 2 — Entidades e Repositórios

- [ ] Remover `@Data` das entidades e substituir por `@Getter` + `@Setter`
- [ ] Verificar e corrigir nomes de repositórios (plural → singular)
- [ ] Mover Specifications para `repositories/specs/`
- [ ] Padronizar naming de métodos de repositório (convenção Spring Data)

### Escopo 3 — Serviços

- [ ] Padronizar nomes de métodos (inglês, convenção CRUD)
- [ ] Garantir `@Transactional(readOnly = true)` em todos os métodos de leitura
- [ ] Mover lógica de negócio que está em controllers para os serviços
- [ ] Padronizar mappers em classes estáticas `{Dominio}Mapper`

### Escopo 4 — Controllers

- [ ] Separar controllers MVC e REST (eliminar controllers híbridos)
- [ ] Padronizar detecção de requisições HTMX via utilitário comum
- [ ] Garantir que todo controller usa apenas DTOs (nunca entidades)
- [ ] Aplicar `@PreAuthorize` consistentemente

### Escopo 5 — Templates

- [ ] Eliminar todos os templates duplicados/backup
- [ ] Migrar todas as páginas para usar `layouts/master.html`
- [ ] Padronizar sistema de mensagens com `fragmentos/messages.html`
- [ ] Nomear templates conforme padrão definido

### Escopo 6 — Configuração e Segurança

- [ ] Mover credenciais para variáveis de ambiente
- [ ] Revisar configuração de Security
- [ ] Organizar `application.properties` por seções comentadas
- [ ] Garantir perfis `dev` / `prod` corretos

---

*Última atualização: 2026-04-10*
*Versão: 1.0*
