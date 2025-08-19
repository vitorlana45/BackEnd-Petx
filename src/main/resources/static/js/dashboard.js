// Filtro de tabela por input[data-table-filter="#idTabela"]
document.querySelectorAll('[data-table-filter]').forEach(inp => {
  const tableId = inp.getAttribute('data-table-filter');
  const table = document.getElementById(tableId);
  const tbody = table?.querySelector('tbody');
  const counter = table?.closest('.card')?.querySelector('[data-table-count]');
  const update = () => {
    const q = (inp.value || '').toLowerCase();
    let visible = 0;
    tbody?.querySelectorAll('tr').forEach(tr => {
      const txt = tr.innerText.toLowerCase();
      const show = txt.includes(q);
      tr.style.display = show ? '' : 'none';
      if (show) visible++;
    });
    if (counter) counter.textContent = visible;
  };
  inp.addEventListener('input', update);
  update();
});

// Contador animado para .counter-number[data-target]
document.querySelectorAll('.counter-number').forEach(el => {
  const target = Number(el.getAttribute('data-target') || el.textContent || 0);
  const dur = 800, start = performance.now();
  const fmt = new Intl.NumberFormat('pt-BR');
  const step = (t) => {
    const p = Math.min((t - start) / dur, 1);
    el.textContent = fmt.format(Math.round(target * p));
    if (p < 1) requestAnimationFrame(step);
  };
  requestAnimationFrame(step);
});

// // Mini gauge .progress-circle[data-progress]
// document.querySelectorAll('.progress-circle').forEach(pc => {
//   const val = Math.max(0, Math.min(100, Number(pc.dataset.progress || 0)));
//   pc.style.width = '36px'; pc.style.height = '36px'; pc.style.borderRadius = '50%';
//   pc.style.background = `conic-gradient(var(--bs-primary) ${val}%, var(--bs-secondary-bg) 0)`;
//   pc.style.display = 'grid'; pc.style.placeItems = 'center';
//   const inner = pc.querySelector('.progress-circle-inner');
//   if (inner) { inner.style.width='22px'; inner.style.height='22px'; inner.style.borderRadius='50%'; inner.style.background='var(--bs-body-bg)'; }
// });
