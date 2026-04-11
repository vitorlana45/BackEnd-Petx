document.addEventListener("DOMContentLoaded", () => {
    const atributosContainer = document.getElementById("atributos");
    const btnAdd = document.getElementById("adicionar-atributo");
    const form = document.getElementById("formProduto");
    const hiddenJson = document.getElementById("atributosJson");

    if (!atributosContainer || !btnAdd || !form) return;

    function criarLinhaAtributo(chave = "", valor = "") {
        const row = document.createElement("div");
        row.className = "atributo-row mb-2";

        row.innerHTML = `
      <div class="input-group">
        <input type="text" class="form-control" name="atributoChave[]" placeholder="Chave (ex: Marca)" value="${chave}">
        <input type="text" class="form-control" name="atributoValor[]" placeholder="Valor (ex: PetFood)" value="${valor}">
        <button type="button" class="btn btn-outline-danger remover-atributo" title="Remover atributo">
          <i class="fas fa-times"></i>
        </button>
      </div>
    `;

        return row;
    }

    btnAdd.addEventListener("click", () => {
        atributosContainer.appendChild(criarLinhaAtributo());
    });

    // Remover usando delegação de evento
    atributosContainer.addEventListener("click", (e) => {
        const btn = e.target.closest(".remover-atributo");
        if (!btn) return;
        const row = btn.closest(".atributo-row");
        if (row) row.remove();
    });

    function montarMapAtributos() {
        const chaves = [...form.querySelectorAll('input[name="atributoChave[]"]')];
        const valores = [...form.querySelectorAll('input[name="atributoValor[]"]')];

        const map = {};
        for (let i = 0; i < Math.min(chaves.length, valores.length); i++) {
            const k = (chaves[i].value || "").trim();
            const v = (valores[i].value || "").trim();

            // ignora linhas vazias
            if (!k && !v) continue;

            // se chave vazia mas valor preenchido -> você decide: aqui eu bloqueio
            if (!k) {
                chaves[i].classList.add("is-invalid");
                throw new Error("Existe atributo com chave vazia.");
            } else {
                chaves[i].classList.remove("is-invalid");
            }

            // se repetir chave, o último sobrescreve (ou você pode juntar em array)
            map[k] = v;
        }
        return map;
    }

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        let atributos;
        try {
            atributos = montarMapAtributos();
            if (hiddenJson) hiddenJson.value = JSON.stringify(atributos);
        } catch (err) {
            alert(err.message);
            return;
        }

        // monta payload do produto (exemplo)
        const payload = {
            id: document.getElementById("produtoId")?.value || null,
            estoqueId: document.getElementById("produtoEstoqueId")?.value || null,
            nome: document.getElementById("nomeProduto")?.value?.trim(),
            descricao: document.getElementById("descricaoProduto")?.value?.trim(),
            quantidade: Number(document.getElementById("quantidadeProduto")?.value || 0),
            unidadeDeMedida: document.getElementById("unidadeMedida")?.value,
            categoriaId: document.getElementById("categoriaProduto")?.value,
            atributos: atributos
        };

        // aqui você chama sua rota de salvar produto
        const resp = await fetch("/produtos", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });

        if (!resp.ok) {
            const txt = await resp.text();
            alert("Erro ao salvar: " + txt);
            return;
        }

        // sucesso
        window.location.reload();
    });
});
