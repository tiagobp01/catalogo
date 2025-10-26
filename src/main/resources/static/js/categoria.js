// Função para verificar se todos os elementos do modal existem
function verificarElementosModal() {
    const elementosNecessarios = [
        'nomeCategoria',
        'produtosAssociados',
        'btnConfirmarExcluir',
        'formExcluir',
        'modalExcluir'
    ];
    
    return elementosNecessarios.every(id => document.getElementById(id) !== null);
}

// Função para abrir o modal de exclusão
function abrirModalExclusao(id, nome) {
    // Verifica se os elementos do modal existem
    if (!verificarElementosModal()) {
        console.error(nome + ' - Elementos do modal não encontrados. Verifique se o modal está no DOM.' + id);
        return;
    }
    
    const nomeCategoriaEl = document.getElementById('nomeCategoria');
    const produtosAssociadosEl = document.getElementById('produtosAssociados');
    const btnConfirmarExcluir = document.getElementById('btnConfirmarExcluir');
    const formExcluir = document.getElementById('formExcluir');
    const modalExcluir = document.getElementById('modalExcluir');

    // Define o nome da categoria no modal
    nomeCategoriaEl.textContent = nome;
    
    // Reseta o estado do modal
    produtosAssociadosEl.classList.add('d-none');
    btnConfirmarExcluir.disabled = false;
    
    // Define a ação do formulário
    formExcluir.action = '/admin/categorias/excluir/' + id;
    
    // Verifica se existem produtos associados
    fetch('/admin/categorias/verificar-produtos/' + id)
        .then(response => response.json())
        .then(data => {
            if (data.temProdutos) {
                produtosAssociadosEl.classList.remove('d-none');
                btnConfirmarExcluir.disabled = true;
            } else {
                produtosAssociadosEl.classList.add('d-none');
                btnConfirmarExcluir.disabled = false;
            }
        })
        .catch(error => {
            console.error('Erro ao verificar produtos associados:', error);
            produtosAssociadosEl.classList.add('d-none');
            btnConfirmarExcluir.disabled = false;
        });
    
    // Abre o modal
    const modal = new bootstrap.Modal(modalExcluir);
    modal.show();
}

// Inicialização quando o documento estiver pronto
document.addEventListener('DOMContentLoaded', function() {
    // Inicializa tooltips
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Adiciona evento de clique aos botões de exclusão
    document.addEventListener('click', function(event) {
        // Verifica se o clique foi em um botão de exclusão ou em um de seus filhos
        const btnExcluir = event.target.closest('.btn-excluir');
        if (btnExcluir) {
            event.preventDefault();
            const id = btnExcluir.getAttribute('data-id');
            const nome = btnExcluir.getAttribute('data-nome');
            
            // Verifica se os atributos necessários existem
            if (id && nome) {
                abrirModalExclusao(id, nome);
            } else {
                console.error('Atributos data-id ou data-nome não encontrados no botão de exclusão');
            }
        }
    });
});
