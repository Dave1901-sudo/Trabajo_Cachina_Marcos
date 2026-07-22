var autoSlideTimers = {};

function cargarResenas(platoId) { // Obtiene y renderiza reseñas de un plato
    const slidesContainer = document.getElementById('resenas-lista-' + platoId);
    if (!slidesContainer) return;
    fetch('/api/resenas/' + platoId)
        .then(res => res.json())
        .then(resenas => {
            if (!resenas || resenas.length === 0) {
                slidesContainer.innerHTML = '<div class="carrusel-slide"><small class="text-muted">Sin reseñas aún. ¡Sé el primero en calificar!</small></div>';
                actualizarCarrusel(platoId);
                detenerAutoSlide(platoId);
                return;
            }
            let html = '';
            resenas.forEach(r => {
                let estrellas = '';
                for (let i = 1; i <= 5; i++) {
                    estrellas += i <= r.puntuacion ? '<span class="estrella-llena">★</span>' : '<span class="estrella-vacia">☆</span>';
                }
                let likedClass = r.likedByCurrentUser ? 'text-primary' : 'text-muted';
                html += '<div class="carrusel-slide">' +
                    '<div class="d-flex justify-content-between align-items-start">' +
                    '<div>' +
                    '<strong>' + escapeHtml(r.usuarioNombre || 'Anónimo') + '</strong> ' +
                    '<span class="estrellas">' + estrellas + '</span>' +
                    '</div>' +
                    '<button class="btn btn-sm btn-link p-0 ' + likedClass + ' like-btn" onclick="darLike(' + r.id + ', this)" title="Me gusta">' +
                    '<i class="bi bi-hand-thumbs-up"></i> <span class="like-count">' + (r.likes || 0) + '</span>' +
                    '</button>' +
                    '</div>' +
                    '<small class="text-muted">' + escapeHtml(r.comentario || '') + '</small>' +
                    '</div>';
            });
            slidesContainer.innerHTML = html;
            actualizarCarrusel(platoId);
            iniciarAutoSlide(platoId);
        })
        .catch(() => {
            slidesContainer.innerHTML = '<div class="carrusel-slide"><small class="text-muted">Error al cargar reseñas.</small></div>';
            actualizarCarrusel(platoId);
            detenerAutoSlide(platoId);
        });
}

function iniciarAutoSlide(platoId) { // Inicia auto-desplazamiento del carrusel
    detenerAutoSlide(platoId);
    const container = document.getElementById('resenas-lista-' + platoId);
    if (!container) return;
    const total = container.querySelectorAll('.carrusel-slide').length;
    if (total < 2) return;
    autoSlideTimers[platoId] = setInterval(function () {
        const c = document.getElementById('resenas-lista-' + platoId);
        if (!c) { detenerAutoSlide(platoId); return; }
        var idx = parseInt(c.getAttribute('data-index') || '0');
        var tot = c.querySelectorAll('.carrusel-slide').length;
        idx = (idx + 1) % tot;
        c.setAttribute('data-index', idx);
        actualizarCarrusel(platoId);
    }, 60000);
}

function detenerAutoSlide(platoId) { // Detiene auto-desplazamiento del carrusel
    if (autoSlideTimers[platoId]) {
        clearInterval(autoSlideTimers[platoId]);
        delete autoSlideTimers[platoId];
    }
}

function reiniciarAutoSlide(platoId) { // Reinicia auto-desplazamiento del carrusel
    detenerAutoSlide(platoId);
    iniciarAutoSlide(platoId);
}

function actualizarCarrusel(platoId) { // Actualiza posición del carrusel y botones
    const slidesContainer = document.getElementById('resenas-lista-' + platoId);
    if (!slidesContainer) return;
    const slides = slidesContainer.querySelectorAll('.carrusel-slide');
    const total = slides.length;
    const index = parseInt(slidesContainer.getAttribute('data-index') || '0');
    const safeIndex = Math.min(index, total - 1);
    slidesContainer.setAttribute('data-index', safeIndex);
    slidesContainer.style.transform = 'translateX(-' + (safeIndex * 100) + '%)';
    const prevBtn = document.querySelector('.carrusel-prev[data-id="' + platoId + '"]');
    const nextBtn = document.querySelector('.carrusel-next[data-id="' + platoId + '"]');
    if (prevBtn) prevBtn.disabled = safeIndex <= 0;
    if (nextBtn) nextBtn.disabled = safeIndex >= total - 1;
    const counter = document.getElementById('resenas-counter-' + platoId);
    if (counter && total > 0) {
        counter.textContent = (safeIndex + 1) + ' / ' + total;
    } else if (counter) {
        counter.textContent = '';
    }
}

function carruselAnterior(btn) { // Navega al slide anterior de reseñas
    const platoId = btn.getAttribute('data-id');
    const container = document.getElementById('resenas-lista-' + platoId);
    if (!container) return;
    const index = parseInt(container.getAttribute('data-index') || '0');
    if (index > 0) {
        container.setAttribute('data-index', index - 1);
        actualizarCarrusel(platoId);
        reiniciarAutoSlide(platoId);
    }
}

function carruselSiguiente(btn) { // Navega al siguiente slide de reseñas
    const platoId = btn.getAttribute('data-id');
    const container = document.getElementById('resenas-lista-' + platoId);
    if (!container) return;
    const total = container.querySelectorAll('.carrusel-slide').length;
    const index = parseInt(container.getAttribute('data-index') || '0');
    if (index < total - 1) {
        container.setAttribute('data-index', index + 1);
        actualizarCarrusel(platoId);
        reiniciarAutoSlide(platoId);
    }
}

function mostrarFormularioResena(btn) { // Muestra formulario de envío de reseña
    const container = btn.closest('.resenas-container');
    if (!container) return;
    const form = container.querySelector('.form-resena');
    if (!form) return;
    form.classList.remove('d-none');
    btn.classList.add('d-none');
}

function cancelarResena(btn) { // Oculta formulario de reseña y reinicia campos
    const container = btn.closest('.resenas-container');
    if (!container) return;
    const form = container.querySelector('.form-resena');
    const btnResena = container.querySelector('[id^="btn-resena-"]');
    if (form) {
        form.classList.add('d-none');
        form.querySelector('textarea').value = '';
        form.querySelector('.puntuacion-input').value = '0';
        form.querySelectorAll('.estrella-input').forEach(e => e.textContent = '☆');
    }
    if (btnResena) btnResena.classList.remove('d-none');
}

function seleccionarEstrella(el) { // Selecciona calificación con estrellas
    const container = el.closest('.estrellas-input');
    const valor = parseInt(el.getAttribute('data-valor'));
    container.querySelectorAll('.estrella-input').forEach(e => {
        const v = parseInt(e.getAttribute('data-valor'));
        e.textContent = v <= valor ? '★' : '☆';
    });
    container.querySelector('.puntuacion-input').value = valor;
}

function enviarResena(btn) { // Envía reseña a la API
    const container = btn.closest('.resenas-container');
    if (!container) return;
    const idMatch = container.id.match(/resenas-container-(\d+)/);
    if (!idMatch) return;
    const platoId = idMatch[1];
    const puntuacion = parseInt(container.querySelector('.puntuacion-input').value);
    const comentario = container.querySelector('textarea').value.trim();
    if (puntuacion === 0) {
        alert('Selecciona una calificación de estrellas.');
        return;
    }
    if (!comentario) {
        alert('Escribe un comentario.');
        return;
    }
    fetch('/api/resenas', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ platoId: platoId, puntuacion: puntuacion, comentario: comentario })
    })
        .then(res => res.json())
        .then(data => {
            if (data.mensaje) {
                alert('Reseña enviada. Gracias por tu opinión.');
                cancelarResena(btn);
                cargarResenas(platoId);
            } else {
                alert('Error: ' + (data.error || 'No se pudo enviar'));
            }
        })
        .catch(err => alert('Error al enviar reseña.'));
}

function darLike(resenaId, btn) { // Alterna like en una reseña
    fetch('/api/resenas/' + resenaId + '/like', { method: 'POST' })
        .then(res => res.json())
        .then(data => {
            if (data.error) {
                if (data.error.includes('iniciar sesion')) {
                    alert('Debes iniciar sesión para dar like.');
                }
                return;
            }
            const countSpan = btn.querySelector('.like-count');
            if (countSpan) countSpan.textContent = data.likes;
            if (data.liked) {
                btn.classList.remove('text-muted');
                btn.classList.add('text-primary');
            } else {
                btn.classList.remove('text-primary');
                btn.classList.add('text-muted');
            }
        })
        .catch(err => console.error(err));
}

function escapeHtml(text) {
    if (!text) return '';
    var div = document.createElement('div');
    div.appendChild(document.createTextNode(text));
    return div.innerHTML;
}

document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('[id^="resenas-container-"]').forEach(container => {
        const idMatch = container.id.match(/resenas-container-(\d+)/);
        if (idMatch) cargarResenas(idMatch[1]);
    });
    document.addEventListener('hidden.bs.modal', function () {
        for (var key in autoSlideTimers) {
            detenerAutoSlide(key);
        }
    });
});