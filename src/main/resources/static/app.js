const shortenForm = document.getElementById('shortenForm');
const urlInput = document.getElementById('urlInput');
const shortenBtn = document.getElementById('shortenBtn');
const clearBtn = document.getElementById('clearBtn');
const resultDiv = document.getElementById('result');
const errorDiv = document.getElementById('error');

const statsKey = document.getElementById('statsKey');
const getStatsBtn = document.getElementById('getStatsBtn');
const statsResult = document.getElementById('statsResult');

const API_PREFIX = '/api';

function showError(err) {
  errorDiv.innerHTML = '';
  if (!err) return;
  if (typeof err === 'string') errorDiv.textContent = err;
  else if (err.message) errorDiv.textContent = err.message;
  else if (typeof err === 'object') {
    errorDiv.innerHTML = Object.entries(err).map(([k, v]) => `<div><strong>${k}:</strong> ${v}</div>`).join('');
  } else errorDiv.textContent = JSON.stringify(err);
}

function showResult(obj) {
  resultDiv.innerHTML = '';
  if (!obj) return;
  const shortUrl = obj.shortUrl || obj.shorturl || obj.shorturl || obj.shortUrl;
  const longUrl = obj.Url || obj.url || obj.Url;

  const s = document.createElement('div');
  s.innerHTML = `
    <div><strong>Original</strong>: <a href="${escapeHtml(longUrl)}" target="_blank">${escapeHtml(longUrl)}</a></div>
    <div style="margin-top:6px"><strong>Short</strong>: <a id="shortLink" href="${escapeHtml(shortUrl)}" target="_blank">${escapeHtml(shortUrl)}</a></div>
    <div class="row" style="margin-top:8px">
      <button class="btn" id="openBtn">Open</button>
      <button class="btn" id="copyBtn">Copy</button>
    </div>
  `;
  resultDiv.appendChild(s);

  document.getElementById('openBtn').addEventListener('click', async () => {
    const a = document.getElementById('shortLink');
    const shortKey = a.href.split('/').pop();
    const resp = await fetch(`/${shortKey}`);
    const longUrl = await resp.text();
    window.open(longUrl, '_blank');
  });

  document.getElementById('copyBtn').addEventListener('click', async () => {
    try {
      await navigator.clipboard.writeText(shortUrl);
      showError('Copied to clipboard');
      setTimeout(() => showError(''), 2000);
    } catch (e) {
      showError('Copy failed');
    }
  });
}

function escapeHtml(s) {
  if (!s) return '';
  return s.replace(/[&"'<>]/g, function (c) {
    return { '&': '&amp;', '"': '&quot;', "'": "&#39;", "<": "&lt;", ">": "&gt;" }[c];
  });
}

shortenForm.addEventListener('submit', async (e) => {
  e.preventDefault();
  showError('');
  resultDiv.innerHTML = '';
  const url = urlInput.value.trim();
  if (!url) { showError('Please enter a URL'); return; }

  shortenBtn.disabled = true;
  shortenBtn.textContent = 'Shortening...';
  try {
    const resp = await fetch(`${API_PREFIX}/shorten`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ url })
    });

    const contentType = resp.headers.get('content-type') || '';
    let body = null;
    if (contentType.includes('application/json')) body = await resp.json();
    else body = await resp.text();

    if (!resp.ok) {
      showError(body || `Error ${resp.status}`);
      return;
    }

    showResult(body);
  } catch (err) {
    showError('Network error: ' + err.message);
  } finally {
    shortenBtn.disabled = false;
    shortenBtn.textContent = 'Shorten';
  }
});

clearBtn.addEventListener('click', () => {
  urlInput.value = '';
  resultDiv.innerHTML = '';
  showError('');
});

function extractKey(input) {
  if (!input) return '';
  input = input.trim();
  try {
    const u = new URL(input.startsWith('http') ? input : 'http://example.com/' + input);
    const path = u.pathname || '/';
    const segs = path.split('/').filter(Boolean);
    return segs.length ? segs[segs.length - 1] : '';
  } catch (e) {
    return input;
  }
}

getStatsBtn.addEventListener('click', async () => {
  statsResult.innerHTML = '';
  showError('');
  const raw = statsKey.value;
  const candidate = extractKey(raw);
  if (!candidate) { showError('Please enter a short URL'); return; }
  const key = candidate;
  getStatsBtn.disabled = true;
  try {
    const resp = await fetch(`${API_PREFIX}/stats/${encodeURIComponent(key)}`);
    const body = await resp.json();
    if (!resp.ok) { showError(body || `Error ${resp.status}`); return; }
    statsResult.innerHTML = `
      <div><strong>Original:</strong> <a href="${escapeHtml(body.Url)}" target="_blank">${escapeHtml(body.Url)}</a></div>
      <div><strong>Short:</strong> <a id="statsShortLink" href="${escapeHtml(body.shortUrl)}" target="_blank">${escapeHtml(body.shortUrl)}</a></div>
      <div><strong>Clicks:</strong> ${escapeHtml(String(body.clicks))}</div>
    `;
  } catch (err) { showError('Network error: ' + err.message); }
  finally { getStatsBtn.disabled = false; }
});
