// Funções para gerenciar os modais de cadastro de estoque e produtos
document.addEventListener('DOMContentLoaded', function() {
    // Modal de Estoque
    const estoqueModal = document.getElementById('modalCadastroEstoque');
    if (estoqueModal) {
        const estoqueForm = estoqueModal.querySelector('form');

        estoqueForm.addEventListener('submit', function(e) {
            e.preventDefault();

            const formData = new FormData(estoqueForm);
            fetch('/estoque/salvar-ajax', {
                method: 'POST',
                body: formData
            })
            .then(response => response.json())
            .then(data => {
                if (data.sucesso) {
                    // Fechar o modal
                    bootstrap.Modal.getInstance(estoqueModal).hide();

                    // Exibir mensagem de sucesso
                    const alertDiv = document.createElement('div');
                    alertDiv.className = 'alert alert-success alert-dismissible fade show';
                    alertDiv.innerHTML = `
                        <i class="fas fa-check-circle me-2"></i>${data.mensagem}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    `;
                    document.querySelector('.container-fluid').prepend(alertDiv);

                    // Recarregar a página após 2 segundos
                    setTimeout(() => {
                        window.location.reload();
                    }, 2000);
                } else {
                    // Exibir mensagens de erro
                    const errorContainer = estoqueModal.querySelector('.error-container');
                    errorContainer.innerHTML = `<div class="alert alert-danger">${data.mensagem}</div>`;
                }
            })
            .catch(error => {
                console.error('Erro:', error);
            });
        });
    }

    // Modal de Produto
    const produtoModal = document.getElementById('modalCadastroProduto');
    if (produtoModal) {
        const produtoForm = produtoModal.querySelector('form');

        // Adicionar/remover atributos específicos do produto
        const btnAdicionarAtributo = produtoModal.querySelector('#adicionar-atributo');
        if (btnAdicionarAtributo) {
            btnAdicionarAtributo.addEventListener('click', function() {
                const atributosDiv = produtoModal.querySelector('#atributos');
                const templateRow = atributosDiv.querySelector('.atributo-row');
                const novoAtributo = templateRow.cloneNode(true);

                // Limpar valores
                novoAtributo.querySelectorAll('input').forEach(input => input.value = '');

                // Adicionar evento de remover ao botão
                novoAtributo.querySelector('.remover-atributo').addEventListener('click', function() {
                    if (atributosDiv.querySelectorAll('.atributo-row').length > 1) {
                        this.closest('.atributo-row').remove();
                    }
                });

                atributosDiv.appendChild(novoAtributo);
            });

            // Adicionar evento aos botões remover existentes
            produtoModal.querySelectorAll('.remover-atributo').forEach(btn => {
                btn.addEventListener('click', function() {
                    if (produtoModal.querySelectorAll('.atributo-row').length > 1) {
                        this.closest('.atributo-row').remove();
                    }
                });
            });
        }

        // Submeter formulário de produto
        produtoForm.addEventListener('submit', function(e) {
            e.preventDefault();

            const formData = new FormData(produtoForm);

            // Adicionar atributos específicos
            const atributosChave = produtoForm.querySelectorAll('input[name="atributoChave[]"]');
            const atributosValor = produtoForm.querySelectorAll('input[name="atributoValor[]"]');

            for (let i = 0; i < atributosChave.length; i++) {
                if (atributosChave[i].value && atributosValor[i].value) {
                    formData.append('atributos[' + atributosChave[i].value + ']', atributosValor[i].value);
                }
            }

            fetch('/estoque/produtos/salvar-ajax', {
                method: 'POST',
                body: formData
            })
            .then(response => response.json())
            .then(data => {
                if (data.sucesso) {
                    // Fechar o modal
                    bootstrap.Modal.getInstance(produtoModal).hide();

                    // Exibir mensagem de sucesso
                    const alertDiv = document.createElement('div');
                    alertDiv.className = 'alert alert-success alert-dismissible fade show';
                    alertDiv.innerHTML = `
                        <i class="fas fa-check-circle me-2"></i>${data.mensagem}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    `;
                    document.querySelector('.container-fluid').prepend(alertDiv);

                    // Recarregar a página após 2 segundos
                    setTimeout(() => {
                        window.location.reload();
                    }, 2000);
                } else {
                    // Exibir mensagens de erro
                    const errorContainer = produtoModal.querySelector('.error-container');
                    errorContainer.innerHTML = `<div class="alert alert-danger">${data.mensagem}</div>`;
                }
            })
            .catch(error => {
                console.error('Erro:', error);
            });
        });
    }
});

