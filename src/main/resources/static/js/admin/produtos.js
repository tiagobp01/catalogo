/**
 * Script para gerenciar a funcionalidade de produtos no painel administrativo
 */

$(document).ready(function() {
    // Inicialização dos selects com busca
    $('.select2').select2({
        theme: 'bootstrap-5',
        width: '100%',
        placeholder: 'Digite para buscar...',
        allowClear: true,
        ajax: {
            delay: 250, // Atraso em ms após cada pressionamento de tecla
            data: function(params) {
                return {
                    termo: params.term, // Termo de busca
                    page: params.page || 1
                };
            },
            processResults: function(data, params) {
                params.page = params.page || 1;
                
                return {
                    results: data.map(function(item) {
                        return {
                            id: item.id,
                            text: item.nome || item.titulo || 'Sem nome'
                        };
                    }),
                    pagination: {
                        more: (params.page * 10) < data.total_count
                    }
                };
            },
            cache: true
        }
    });

    // Configuração específica para o campo de Categoria
    $('#categoria').select2({
        theme: 'bootstrap-5',
        width: '100%',
        placeholder: 'Digite para buscar categorias...',
        allowClear: true,
        ajax: {
            url: '/admin/produtos/buscar-categorias',
            dataType: 'json',
            delay: 250,
            data: function (params) {
                return {
                    termo: params.term
                };
            },
            processResults: function (data) {
                return {
                    results: data.map(function(item) {
                        return {
                            id: item.id,
                            text: item.nome
                        };
                    })
                };
            },
            cache: true
        }
    });

    // Configuração específica para o campo de Autor
    $('#autor').select2({
        theme: 'bootstrap-5',
        width: '100%',
        placeholder: 'Digite para buscar autores...',
        allowClear: true,
        ajax: {
            url: '/admin/produtos/buscar-autores',
            dataType: 'json',
            delay: 250,
            data: function (params) {
                return {
                    termo: params.term
                };
            },
            processResults: function (data) {
                return {
                    results: data.map(function(item) {
                        return {
                            id: item.id,
                            text: item.nome
                        };
                    })
                };
            },
            cache: true
        }
    });

    // Configuração específica para o campo de Diretor
    $('#diretor').select2({
        theme: 'bootstrap-5',
        width: '100%',
        placeholder: 'Digite para buscar diretores...',
        allowClear: true,
        ajax: {
            url: '/admin/produtos/buscar-diretores',
            dataType: 'json',
            delay: 250,
            data: function (params) {
                return {
                    termo: params.term
                };
            },
            processResults: function (data) {
                return {
                    results: data.map(function(item) {
                        return {
                            id: item.id,
                            text: item.nome
                        };
                    })
                };
            },
            cache: true
        }
    });

    // Validação do formulário
    (function () {
        'use strict'
        
        // Busca todos os formulários que precisam de validação
        var forms = document.querySelectorAll('.needs-validation')
        
        // Loop sobre eles e previne envio
        Array.prototype.slice.call(forms)
            .forEach(function (form) {
                form.addEventListener('submit', function (event) {
                    if (!form.checkValidity()) {
                        event.preventDefault()
                        event.stopPropagation()
                    }
                    
                    form.classList.add('was-validated')
                }, false)
            })
    })()

    // Função para pré-visualizar a imagem selecionada
    $(document).on('change', '#imagem', function() {
        previewImagem(this);
    });
});

/**
 * Exibe uma prévia da imagem selecionada
 */
function previewImagem(input) {
    const preview = document.getElementById('preview-imagem');
    const container = input.closest('.card-body').querySelector('.imagem-container');
    const semImagem = container ? container.querySelector('.text-muted') : null;
    
    if (input.files && input.files[0]) {
        const reader = new FileReader();
        
        reader.onload = function(e) {
            if (!preview) {
                // Se não existir o elemento de preview, cria um novo
                const img = document.createElement('img');
                img.id = 'preview-imagem';
                img.className = 'img-fluid preview-imagem';
                img.alt = 'Prévia da imagem';
                img.src = e.target.result;
                img.style.display = 'block';
                
                if (semImagem) {
                    semImagem.style.display = 'none';
                }
                
                container.insertBefore(img, input.parentNode);
            } else {
                // Se existir, apenas atualiza o src
                preview.src = e.target.result;
                preview.style.display = 'block';
                
                if (semImagem) {
                    semImagem.style.display = 'none';
                }
            }
        }
        
        reader.readAsDataURL(input.files[0]);
    }
}

/**
 * Confirma a exclusão de um produto
 * @param {string} mensagem - Mensagem de confirmação
 * @param {string} url - URL para redirecionar após confirmação
 */
function confirmarExclusao(mensagem, url) {
    if (confirm(mensagem)) {
        window.location.href = url;
    }
}
