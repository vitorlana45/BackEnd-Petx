/**
 window.ResgatesPage = (function() {
    'use strict';

    // ======= ESTADO PRIVADO =======
    let graficos = {};
    let graficosCarregados = false;
    let estatisticas = {};
    let estatisticasCarregadas = false;ript específico para a página de Resgates
 * Responsável pela renderização e gerenciamento dos gráficos de estatísticas
 */

// Namespace para evitar conflitos globais
window.ResgatesPage = (function() {
    'use strict';

    // ======= VARIÁVEIS PRIVADAS =======
    let graficos = {};
    let graficosCarregados = false;
    let estatisticas = {};
    let estatisticasCarregadas = false;

    // ======= CONFIGURAÇÕES DE CORES =======
    const CORES = {
        paleta: ['#4361ee','#7209b7','#f72585','#4cc9f0','#0cce6b','#ff9e00','#3a0ca3','#4895ef','#b5179e'],
        paletaAlt: ['#0cce6b','#ff9e00','#7209b7','#4cc9f0','#4361ee','#f72585','#3a0ca3','#4895ef','#b5179e']
    };

    // ======= HELPER FUNCTIONS =======
    function temaAtual() {
        const isDark = (document.documentElement.getAttribute('data-theme') === 'dark');
        return {
            isDark,
            texto: isDark ? '#ffffff' : '#333333',
            grade: isDark ? 'rgba(255,255,255,0.1)' : 'rgba(0,0,0,0.1)',
            bgTooltip: isDark ? 'rgba(30,30,30,0.95)' : 'rgba(255,255,255,0.95)',
            borderTooltip: isDark ? 'rgba(255,255,255,0.2)' : 'rgba(0,0,0,0.1)'
        };
    }

    function normalizarLabel(str) {
        if (!str) return 'Desconhecida';
        let s = str.toString().trim().replace(/_/g, ' ').toLowerCase();
        s = s.replace(/cao(\b)/g, 'ção$1').replace(/coes(\b)/g, 'ções$1');
        const wordMap = { 
            'devolucao': 'devolução', 
            'adocao': 'adoção', 
            'reintroducao': 'reintrodução', 
            'tutor': 'tutor' 
        };
        s = s.split(/\s+/).filter(Boolean)
            .map(w => wordMap[w] || w)
            .map(w => w.charAt(0).toUpperCase() + w.slice(1))
            .join(' ');
        return s;
    }

    function aplicarPercentual(valores, total, usarPercentual) {
        if (!usarPercentual) return valores;
        return valores.map(v => total === 0 ? 0 : +((v / total) * 100).toFixed(1));
    }

    function brightenColor(hex) {
        const { isDark } = temaAtual();
        if (!isDark) return hex;
        
        const r = parseInt(hex.slice(1, 3), 16);
        const g = parseInt(hex.slice(3, 5), 16);
        const b = parseInt(hex.slice(5, 7), 16);
        const factor = 1.25;
        
        const nr = Math.min(255, Math.round(r * factor));
        const ng = Math.min(255, Math.round(g * factor));
        const nb = Math.min(255, Math.round(b * factor));
        
        return `#${nr.toString(16).padStart(2, '0')}${ng.toString(16).padStart(2, '0')}${nb.toString(16).padStart(2, '0')}`;
    }

    // ======= PLUGINS CHART.JS =======
    const textShadowPlugin = {
        id: 'textShadow',
        beforeDraw(chart) {
            const { isDark } = temaAtual();
            const { ctx } = chart;
            if (isDark) {
                ctx.shadowColor = 'rgba(0, 0, 0, 0.7)';
                ctx.shadowBlur = 3;
                ctx.shadowOffsetX = 1;
                ctx.shadowOffsetY = 1;
            } else {
                ctx.shadowColor = 'rgba(0,0,0,0)';
                ctx.shadowBlur = 0;
                ctx.shadowOffsetX = 0;
                ctx.shadowOffsetY = 0;
            }
        },
        afterDraw(chart) {
            const { ctx } = chart;
            ctx.shadowColor = 'rgba(0,0,0,0)';
            ctx.shadowBlur = 0;
            ctx.shadowOffsetX = 0;
            ctx.shadowOffsetY = 0;
        }
    };

    const pluginCentroDonut = {
        id: 'centerText',
        afterDraw(chart, args, opts) {
            if (!opts?.show) return;
            const { ctx } = chart;
            const { isDark, texto } = temaAtual();
            
            ctx.save();
            ctx.font = 'bold 15px system-ui, -apple-system, sans-serif';
            ctx.fillStyle = texto;
            ctx.textAlign = 'center';
            ctx.textBaseline = 'middle';
            
            if (isDark) {
                ctx.shadowColor = 'rgba(0,0,0,0.7)';
                ctx.shadowBlur = 3;
                ctx.shadowOffsetX = 1;
                ctx.shadowOffsetY = 1;
            }
            
            const total = chart.data.datasets[0].data.reduce((a, b) => a + b, 0);
            ctx.fillText(total + ' total', chart.width / 2, chart.height / 2);
            ctx.shadowColor = 'rgba(0,0,0,0)';
            ctx.shadowBlur = 0;
            ctx.shadowOffsetX = 0;
            ctx.shadowOffsetY = 0;
            ctx.restore();
        }
    };

    // ======= FUNÇÕES DE CRIAÇÃO DOS GRÁFICOS =======
    function criarGraficoRosca(ctx, itens, cores, mostrarCentro) {
        const etiquetas = itens.map(o => normalizarLabel(o.label) || '—');
        const totais = itens.map(o => o.total);
        const soma = totais.reduce((a, b) => a + b, 0);
        const chkPercentual = document.getElementById('togglePercent');
        const dadosPlot = aplicarPercentual(totais, soma, chkPercentual?.checked);
        const { texto, bgTooltip, borderTooltip } = temaAtual();

        return new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: etiquetas,
                datasets: [{
                    data: dadosPlot,
                    backgroundColor: cores,
                    borderWidth: 2,
                    borderColor: '#ffffff'
                }]
            },
            options: {
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'bottom',
                        labels: {
                            color: texto,
                            font: { size: 14, weight: 'bold' },
                            usePointStyle: true,
                            boxWidth: 10,
                            padding: 15
                        }
                    },
                    tooltip: {
                        backgroundColor: bgTooltip,
                        titleColor: texto,
                        bodyColor: texto,
                        borderColor: borderTooltip,
                        borderWidth: 1,
                        padding: 10,
                        callbacks: {
                            label: (c) => {
                                const raw = totais[c.dataIndex];
                                const pct = soma ? ((raw / soma) * 100).toFixed(1) : 0;
                                return `${c.label}: ${raw} (${pct}%)`;
                            }
                        }
                    }
                }
            },
            plugins: [textShadowPlugin, (mostrarCentro ? pluginCentroDonut : null)].filter(Boolean)
        });
    }

    function criarGraficoBarra(ctx, itens) {
        const ordenados = [...itens].sort((a, b) => b.total - a.total);
        const etiquetas = ordenados.map(o => normalizarLabel(o.label) || '—');
        const totais = ordenados.map(o => o.total);
        const soma = totais.reduce((a, b) => a + b, 0);
        const chkPercentual = document.getElementById('togglePercent');
        const dadosPlot = aplicarPercentual(totais, soma, chkPercentual?.checked);
        const { texto, grade, bgTooltip, borderTooltip } = temaAtual();

        return new Chart(ctx, {
            type: 'bar',
            data: {
                labels: etiquetas,
                datasets: [{
                    label: (chkPercentual?.checked ? '% dos Resgates' : 'Resgates'),
                    data: dadosPlot,
                    backgroundColor: brightenColor('#4361ee'),
                    borderColor: '#3a56d4',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { labels: { color: texto, font: { size: 14, weight: 'bold' } } },
                    tooltip: { backgroundColor: bgTooltip, titleColor: texto, bodyColor: texto, borderColor: borderTooltip, borderWidth: 1 }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: { color: texto, callback: (v) => chkPercentual?.checked ? v + '%' : v },
                        grid: { color: grade }
                    },
                    x: {
                        ticks: { color: texto, font: { weight: 'bold' } },
                        grid: { display: false }
                    }
                }
            },
            plugins: [textShadowPlugin]
        });
    }

    function criarGraficoLinha(ctx, itens) {
        const etiquetas = itens.map(o => o.periodo);
        const totais = itens.map(o => o.total);
        const { texto, grade, bgTooltip, borderTooltip } = temaAtual();

        return new Chart(ctx, {
            type: 'line',
            data: {
                labels: etiquetas,
                datasets: [{
                    label: 'Resgates',
                    data: totais,
                    fill: { target: 'origin', above: 'rgba(247, 37, 133, 0.1)' },
                    borderColor: '#f72585',
                    borderWidth: 3,
                    pointBackgroundColor: '#f72585',
                    pointBorderColor: '#ffffff',
                    pointRadius: 5,
                    pointHoverRadius: 7,
                    tension: 0.3
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { labels: { color: texto, font: { size: 14, weight: 'bold' } } },
                    tooltip: { backgroundColor: bgTooltip, titleColor: texto, bodyColor: texto, borderColor: borderTooltip, borderWidth: 1 }
                },
                scales: {
                    y: { beginAtZero: true, precision: 0, ticks: { color: texto }, grid: { color: grade } },
                    x: { ticks: { color: texto, font: { weight: 'bold' } }, grid: { display: false } }
                }
            },
            plugins: [textShadowPlugin]
        });
    }

    // ======= FUNÇÃO PRINCIPAL DE RENDERIZAÇÃO =======
    function renderCharts(dados) {
        if (!dados || Object.keys(dados).length === 0) {
            console.log('❌ Saindo: dados inválidos');
            return;
        }
        
        console.log('✅ Prosseguindo com renderização...');
        // Configurar defaults globais do Chart.js
        const { isDark, texto } = temaAtual();
        Chart.defaults.font.size = isDark ? 15 : 14;
        Chart.defaults.font.weight = 'bold';
        Chart.defaults.color = texto;

        // Obter elementos canvas
        const canvasOrigem = document.getElementById('chart-origem');
        const canvasEspecie = document.getElementById('chart-especie');
        const canvasTempo = document.getElementById('chart-tempo');
        const canvasDestino = document.getElementById('chart-destino');

        // Aplicar ajuste de brilho nas paletas
        const paletaAjustada = CORES.paleta.map(brightenColor);
        const paletaAltAjustada = CORES.paletaAlt.map(brightenColor);

        // Criar gráficos
        try {
            if (canvasOrigem && dados.porOrigem?.length > 0) {
                canvasOrigem.getContext('2d').clearRect(0, 0, canvasOrigem.width, canvasOrigem.height);
                graficos.origem = criarGraficoRosca(canvasOrigem, dados.porOrigem, paletaAjustada, true);
            }

            if (canvasEspecie && dados.porEspecie?.length > 0) {
                canvasEspecie.getContext('2d').clearRect(0, 0, canvasEspecie.width, canvasEspecie.height);
                graficos.especie = criarGraficoBarra(canvasEspecie, dados.porEspecie);
            }

            if (canvasTempo && dados.serieTemporal?.length > 0) {
                canvasTempo.getContext('2d').clearRect(0, 0, canvasTempo.width, canvasTempo.height);
                graficos.tempo = criarGraficoLinha(canvasTempo, dados.serieTemporal);
            }

            if (canvasDestino && dados.porDestino?.length > 0) {
                canvasDestino.getContext('2d').clearRect(0, 0, canvasDestino.width, canvasDestino.height);
                graficos.destino = criarGraficoRosca(canvasDestino, dados.porDestino, paletaAltAjustada, false);
            }

            graficosCarregados = true;
            console.log('Gráficos criados com sucesso');
        } catch (error) {
            console.error('Erro ao criar gráficos:', error);
        }
    }

    // ======= FUNÇÃO PARA RECRIAR GRÁFICOS =======
    function recriarGraficos() {

        // Destruir gráficos existentes
        Object.values(graficos).forEach(g => {
            try {
                g?.destroy();
                console.log('Gráfico destruído:', g);
            } catch (err) {
                console.log('Erro ao destruir gráfico:', err);
            }
        });

        // Resetar estado
        graficos = {};
        graficosCarregados = false;

        // Renderizar novamente se há dados
        if (estatisticasCarregadas && estatisticas) {
            console.log('Chamando renderCharts com dados:', estatisticas);
            renderCharts(estatisticas);
        } else {
            console.log('Não renderizando - estatísticasCarregadas:', estatisticasCarregadas, 'estatisticas:', estatisticas);
        }
    }

    // ======= INICIALIZAÇÃO E EVENT LISTENERS =======
    function inicializar(dadosEstatisticas, carregadas) {
        estatisticas = dadosEstatisticas || {};
        estatisticasCarregadas = carregadas || false;

        // Event listener para troca de tema
        document.addEventListener('petx:theme-changed', (event) => {
            console.log(`Tema alterado para: ${event.detail?.theme}. Recriando gráficos...`);
            // Sempre recriar se há dados
            if (estatisticasCarregadas) {
                recriarGraficos();
            } else {
                console.log('Estatísticas não carregadas, não recriando gráficos');
            }
        });

        // Event listener para abertura da aba estatísticas
        document.addEventListener('shown.bs.tab', function (e) {
            if (e.target && e.target.id === 'estatisticas-tab') {
                console.log('Aba estatísticas aberta, renderizando gráficos...');
                if (estatisticasCarregadas) {
                    recriarGraficos();
                }
            }
        });

        // Event listener para toggle de percentual
        const chkPercentual = document.getElementById('togglePercent');
        if (chkPercentual) {
            chkPercentual.addEventListener('change', () => {
                console.log('Toggle percentual alterado, recriando gráficos...');
                recriarGraficos();
            });
        }

        // Renderizar gráficos se a aba já estiver ativa
        document.addEventListener('DOMContentLoaded', () => {
            const activeTab = document.querySelector('.nav-link.active');
            if (estatisticasCarregadas && activeTab && activeTab.id === 'estatisticas-tab') {
                console.log('Página carregada com aba estatísticas ativa, renderizando gráficos...');
                renderCharts(estatisticas);
            }
        });
    }

    // ======= API PÚBLICA =======
    return {
        init: inicializar,
        recriarGraficos: recriarGraficos,
        renderCharts: renderCharts
    };
})();
