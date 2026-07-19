function cargarResenas(platoId) {
    const container = document.getElementById('resenas-lista-' + platoId);
    if (!container) return;
    fetch('/api/resenas/' + platoId)
        .then(res => res.json())
        .then(resenas => {
            if (!resenas || resenas.length === 0) {
                container.innerHTML = '<small class="text-muted">Sin reseñas aún. ¡Sé el primero en calificar!</small>';
                return;
            }
            let html = '';
            resenas.forEach(r => {
                let estrellas = '';
                for (let i = 1; i <= 5; i++) {
                    estrellas += i <= r.puntuacion ? '<span class="estrella-llena">★</span>' : '<span class="estrella-vacia">☆</span>';
                }
                let likedClass = r.likedByCurrentUser ? 'text-primary' : 'text-muted';
                html += '<div class="resena-item p-2 mb-1" style="border-bottom: 1px solid #eee;">' +
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
            container.innerHTML = html;
        })
        .catch(() => {
            container.innerHTML = '<small class="text-muted">Error al cargar reseñas.</small>';
        });
}

function mostrarFormularioResena(btn) {
    const container = btn.closest('.resenas-container');
    if (!container) return;
    const form = container.querySelector('.form-resena');
    if (!form) return;
    form.classList.remove('d-none');
    btn.classList.add('d-none');
}

function cancelarResena(btn) {
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

function seleccionarEstrella(el) {
    const container = el.closest('.estrellas-input');
    const valor = parseInt(el.getAttribute('data-valor'));
    container.querySelectorAll('.estrella-input').forEach(e => {
        const v = parseInt(e.getAttribute('data-valor'));
        e.textContent = v <= valor ? '★' : '☆';
    });
    container.querySelector('.puntuacion-input').value = valor;
}

function enviarResena(btn) {
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

function darLike(resenaId, btn) {
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
});
