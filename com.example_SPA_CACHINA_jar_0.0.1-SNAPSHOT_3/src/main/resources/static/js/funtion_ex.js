function botonMas() {
    const btnShowMore = document.getElementById("btn-show-more");
    const btnShowLess = document.getElementById("btn-show-less");
    const extraCards = document.querySelectorAll(".extra-card");
    // Mostrar tarjetas adicionales
    btnShowMore.addEventListener("click", () => {
        extraCards.forEach(card => card.classList.remove("d-none"));
        btnShowMore.classList.add("d-none");
        btnShowLess.classList.remove("d-none");
        window.scrollTo({top: document.getElementById('section-title').offsetTop, behavior: 'smooth'});
    });
    // Ocultar tarjetas adicionales
    btnShowLess.addEventListener("click", () => {
        extraCards.forEach(card => card.classList.add("d-none"));
        btnShowMore.classList.remove("d-none");
        btnShowLess.classList.add("d-none");
        window.scrollTo({top: document.getElementById('section-title').offsetTop, behavior: 'smooth'});
    });
}
//Funcion carrito
function initializeCart() {
    let cart = [];
    // Función para agregar un plato al carrito
    document.querySelectorAll('#addToCart').forEach(button => {
        button.addEventListener('click', function () {
            let id = this.getAttribute('data-id');
            let nombre = this.getAttribute('data-nombre');
            let precio = parseFloat(this.getAttribute('data-precio'));
            let imagen = this.getAttribute('data-imagen');
            let modal = this.closest('.modal');
            let cantidad = parseInt(modal.querySelector('#cantidad').value);
            let comentario = modal.querySelector('#comentarios').value;
            // Verificar si la cantidad es mayor a 10
            if (cantidad > 10) {
                alert("La cantidad máxima es 10.");
                return;
            }
            // Verificar si el plato ya existe en el carrito
            let existingItem = cart.find(item => item.id === id);
            if (existingItem) {
                let nuevaCantidad = existingItem.cantidad + cantidad;
                if (nuevaCantidad > 10) {
                    showErrorAlert();
                    return;
                }
                existingItem.cantidad = nuevaCantidad;
                existingItem.comentario = comentario; // Actualizar comentario
            } else {
                // Agregar nuevo plato al carrito
                let item = {
                    id: id,
                    nombre: nombre,
                    precio: precio,
                    imagen: imagen,
                    cantidad: cantidad,
                    comentario: comentario
                };
                cart.push(item);
            }
            updateCartModal();
            // Mostrar notificación de que se agregó el plato
            let toastElement = new bootstrap.Toast(document.getElementById('cartToast'));
            toastElement.show();
        });
    });
    document.getElementById('realizarPedidoBtn').addEventListener('click', function () {
        if (cart.length === 0) {
            showWarningAlert();
            return;
        }
        const cartModal = bootstrap.Modal.getInstance(document.getElementById('cartModal'));
        cartModal.hide();
        // Mostrar el modal de confirmación para completar la información
        new bootstrap.Modal(document.getElementById('confirmOrderModal')).show();
        let toastElement1 = new bootstrap.Toast(document.getElementById('cartToast2'));
        toastElement1.show();
    });
    document.getElementById('sendOrder').addEventListener('click', function () {
        const email = document.getElementById('email').value;
        const phone = document.getElementById('phone').value;
        const direccion = document.getElementById('direccion').value;
        if (!email || !phone || !direccion) {
            showInfoAlert();
            return;
        }
        // Mostrar el modal de carga
        let loadingModal = new bootstrap.Modal(document.getElementById('loadingModal'));
        loadingModal.show();
        // Crear objeto con datos del carrito y los datos del usuario
        const orderData = {
            orderItems: cart.map(item => ({
                    idPlato: item.id,
                    nombre: item.nombre,
                    precio: item.precio,
                    cantidad: item.cantidad,
                    comentario: item.comentario
                })),
            email: email,
            phone: phone,
            direccion: direccion
        };
        const loadingText = document.querySelector('#loadingModal .modal-body p');
        const spinner = document.querySelector('#loadingModal .spinner-border');
        // Enviar los datos al backend
        fetch('/realizarPedido', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(orderData)
        })
                .then(response => response.json())
                .then(data => {
                    if (data.message === "success") {
                        loadingText.textContent = "¡Pedido completado con éxito!";
                        spinner.style.display = 'none';
                        setTimeout(() => {
                            loadingModal.hide();
                            showSuccessAlert("Pedido realizado con éxito. Verifica tu email para pagar.");
                            cart = [];  // Limpiar el carrito
                            updateCartModal();
                            if (typeof refreshOrderNotifications === 'function') {
                                refreshOrderNotifications();
                            }
                            // Cerrar el modal de confirmación
                            bootstrap.Modal.getInstance(document.getElementById('confirmOrderModal')).hide();
                        }, 1000);
                    } else {
                        showErrorAlert2("Hubo un error al realizar el pedido: " + data.message);
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert("Error al realizar el pedido." + error.message);
                });
    });
//mensajes
    function showWarningAlert() {
        const toastElement = document.getElementById('warningToast');
        const toast = new bootstrap.Toast(toastElement);
        toast.show();
    }
    function showInfoAlert() {
        const toastElement = document.getElementById('infoToast');
        const toast = new bootstrap.Toast(toastElement);
        toast.show();
    }
    function showSuccessAlert(message) {
        Swal.fire({
            title: '¡Éxito!',
            text: message,
            icon: 'success',
            confirmButtonText: 'Entendido',
            customClass: {
                confirmButton: 'btn btn-success'
            },
            buttonsStyling: false
        });
    }
    function showErrorAlert2(message) {
        Swal.fire({
            title: 'Error',
            text: message,
            icon: 'error',
            confirmButtonText: 'Entendido',
            customClass: {
                confirmButton: 'btn btn-danger'
            },
            buttonsStyling: false
        });
    }
    function showErrorAlert() {
        const toastElement = document.getElementById('errorToast');
        const toast = new bootstrap.Toast(toastElement);
        toast.show();
    }
    function showWarningAlert2() {
        const toastElement = document.getElementById('warningToast2');
        const toast = new bootstrap.Toast(toastElement);
        toast.show();
    }
    // Función para actualizar el modal del carrito
    function updateCartModal() {
        let cartItemsContainer = document.getElementById('cartItems');
        cartItemsContainer.innerHTML = '';
        let totalPrice = 0;
        cart.forEach(item => {
            totalPrice += item.precio * item.cantidad; // Calcular el precio total
            let li = document.createElement('li');
            li.className = 'list-group-item mb-2'; // Añadida la separación entre pedidos
            li.innerHTML = `
                <div class="d-flex justify-content-between align-items-center">
                    <div class="d-flex align-items-center">
                        <img src="${item.imagen}" alt="${item.nombre}" style="width: 50px; height: 50px; object-fit: cover;" class="me-3">
                        <div>
                            <h6 class="mb-0">${item.nombre}</h6>
                            <small>S/. ${item.precio} x ${item.cantidad}</small>
                            <textarea class="form-control mt-2 comentario-input text-muted" rows="2" 
                                placeholder="Comentario">${item.comentario}</textarea>
                        </div>
                    </div>
                    <div class="btn-group">
                        <button class="btn btn-sm btn-outline-secondary decrease-quantity" data-id="${item.id}">-</button>
                        <button class="btn btn-sm btn-outline-secondary increase-quantity" data-id="${item.id}">+</button>
                        <button class="btn btn-sm btn-outline-danger remove-item" data-id="${item.id}">&times;</button>
                    </div>
                </div>
            `;
            cartItemsContainer.appendChild(li);
        });
        // Mostrar el precio total
        let totalContainer = document.getElementById('totalPrice');
        totalContainer.innerHTML = `Total: S/. ${totalPrice.toFixed(2)}`;
        // Asignar eventos a los botones de incrementar, disminuir y eliminar
        document.querySelectorAll('.increase-quantity').forEach(button => {
            button.addEventListener('click', function () {
                let id = this.getAttribute('data-id');
                let item = cart.find(item => item.id === id);
                if (item.cantidad < 10) {
                    item.cantidad++;
                    updateCartModal();
                } else {
                    showErrorAlert();
                }
            });
        });
        document.querySelectorAll('.decrease-quantity').forEach(button => {
            button.addEventListener('click', function () {
                let id = this.getAttribute('data-id');
                let item = cart.find(item => item.id === id);
                if (item.cantidad > 1) {
                    item.cantidad--;
                    updateCartModal();
                } else {
                    showWarningAlert2();
                }
            });
        });
        document.querySelectorAll('.remove-item').forEach(button => {
            button.addEventListener('click', function () {
                let id = this.getAttribute('data-id');
                cart = cart.filter(item => item.id !== id);
                updateCartModal();
            });
        });
        // Actualizar comentarios en tiempo real
        document.querySelectorAll('.comentario-input').forEach(textarea => {
            textarea.addEventListener('input', function () {
                let id = this.closest('li').querySelector('.remove-item').getAttribute('data-id');
                let item = cart.find(item => item.id === id);
                if (item) {
                    item.comentario = this.value;
                }
            });
        });
    }
}

function initializeOrderNotifications() {
    const notificationButton = document.getElementById('notificationButton');
    const notificationsModal = document.getElementById('notificationsModal');

    if (!notificationButton || !notificationsModal) {
        return;
    }

    notificationButton.addEventListener('click', () => {
        loadOrderNotifications(true);
    });

    refreshOrderNotifications();
    checkConfirmedOrderNotifications();
    setInterval(() => {
        refreshOrderNotifications();
        checkConfirmedOrderNotifications();
    }, 30000);
}

function refreshOrderNotifications() {
    loadOrderNotifications(false);
}

function loadOrderNotifications(renderList) {
    const badge = document.getElementById('notificationBadge');
    const loading = document.getElementById('notificationsLoading');
    const empty = document.getElementById('notificationsEmpty');
    const list = document.getElementById('notificationsList');

    if (!badge) {
        return;
    }

    if (renderList) {
        if (loading) loading.classList.remove('d-none');
        if (empty) empty.classList.add('d-none');
        if (list) list.innerHTML = '';
    }

    fetch('/mis-pedidos/pendientes')
            .then(response => {
                if (!response.ok) {
                    throw new Error('No se pudieron cargar los pedidos pendientes.');
                }
                return response.json();
            })
            .then(pedidos => {
                updateNotificationBadge(pedidos.length);

                if (!renderList || !list || !loading || !empty) {
                    return;
                }

                loading.classList.add('d-none');
                list.innerHTML = '';

                if (pedidos.length === 0) {
                    empty.classList.remove('d-none');
                    return;
                }

                empty.classList.add('d-none');
                pedidos.forEach(pedido => {
                    const button = document.createElement('button');
                    button.type = 'button';
                    button.className = 'pedido-notificacion bg-white text-start p-3 w-100';
                    button.innerHTML = `
                        <div class="d-flex justify-content-between gap-3 align-items-start">
                            <div>
                                <div class="fw-bold text-primary">Pedido #${pedido.id}</div>
                                <div class="text-muted small">
                                    <i class="bi bi-clock me-1"></i>${escapeHtml(pedido.fecha || 'Fecha no disponible')}
                                </div>
                            </div>
                            <div class="text-end">
                                <span class="badge bg-warning text-dark mb-2">${escapeHtml(pedido.estado || 'Pendiente')}</span>
                                <div class="fw-bold">S/. ${formatCurrency(pedido.total)}</div>
                            </div>
                        </div>
                    `;
                    button.addEventListener('click', () => showOrderDetail(pedido.id));
                    list.appendChild(button);
                });
            })
            .catch(error => {
                console.error(error);
                if (renderList && loading && empty && list) {
                    loading.classList.add('d-none');
                    empty.classList.remove('d-none');
                    empty.innerHTML = `
                        <i class="bi bi-exclamation-triangle fs-1 text-warning"></i>
                        <p class="mb-0 mt-2">No se pudieron cargar tus pedidos pendientes.</p>
                    `;
                    list.innerHTML = '';
                }
            });
}

function updateNotificationBadge(count) {
    const badge = document.getElementById('notificationBadge');
    if (!badge) {
        return;
    }

    badge.textContent = count;
    badge.classList.toggle('d-none', count === 0);
}

function showOrderDetail(orderId) {
    const detailBody = document.getElementById('orderDetailBody');
    if (!detailBody) {
        return;
    }

    detailBody.innerHTML = `
        <div class="text-center text-muted py-4">
            <div class="spinner-border text-primary mb-3" role="status">
                <span class="visually-hidden">Cargando...</span>
            </div>
            <p class="mb-0">Cargando detalle del pedido...</p>
        </div>
    `;

    const notificationsModalElement = document.getElementById('notificationsModal');
    const notificationsModal = bootstrap.Modal.getInstance(notificationsModalElement);
    if (notificationsModal) {
        notificationsModal.hide();
    }

    const detailModal = new bootstrap.Modal(document.getElementById('orderDetailModal'));
    detailModal.show();

    fetch(`/mis-pedidos/${orderId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('No se pudo cargar el detalle del pedido.');
                }
                return response.json();
            })
            .then(pedido => {
                const detalles = Array.isArray(pedido.detalles) ? pedido.detalles : [];
                const filas = detalles.map(item => `
                    <tr>
                        <td>${escapeHtml(item.nombre || '-')}</td>
                        <td class="text-center">${item.cantidad || 0}</td>
                        <td class="text-end">S/. ${formatCurrency(item.precio)}</td>
                        <td class="text-end">S/. ${formatCurrency(item.subtotal)}</td>
                    </tr>
                    ${item.comentario ? `<tr><td colspan="4" class="text-muted small pt-0">Comentario: ${escapeHtml(item.comentario)}</td></tr>` : ''}
                `).join('');

                detailBody.innerHTML = `
                    <div class="d-flex justify-content-between flex-wrap gap-3 mb-3">
                        <div>
                            <h5 class="text-primary mb-1">Pedido #${pedido.id}</h5>
                            <div class="text-muted"><i class="bi bi-clock me-1"></i>${escapeHtml(pedido.fecha || 'Fecha no disponible')}</div>
                        </div>
                        <div class="text-end">
                            <span class="badge bg-warning text-dark">${escapeHtml(pedido.estado || 'Pendiente')}</span>
                            <div class="fs-5 fw-bold text-primary mt-1">S/. ${formatCurrency(pedido.total)}</div>
                        </div>
                    </div>
                    <div class="row g-3 mb-3">
                        <div class="col-md-4"><strong>Correo:</strong><br>${escapeHtml(pedido.email || '-')}</div>
                        <div class="col-md-4"><strong>Telefono:</strong><br>${escapeHtml(pedido.phone || '-')}</div>
                        <div class="col-md-4"><strong>Direccion:</strong><br>${escapeHtml(pedido.direccion || '-')}</div>
                    </div>
                    <div class="table-responsive">
                        <table class="table table-sm align-middle pedido-detalle-table bg-white">
                            <thead>
                                <tr>
                                    <th>Producto</th>
                                    <th class="text-center">Cant.</th>
                                    <th class="text-end">Precio</th>
                                    <th class="text-end">Subtotal</th>
                                </tr>
                            </thead>
                            <tbody>
                                ${filas || '<tr><td colspan="4" class="text-center text-muted">Sin detalles.</td></tr>'}
                            </tbody>
                        </table>
                    </div>
                `;
            })
            .catch(error => {
                console.error(error);
                detailBody.innerHTML = `
                    <div class="text-center text-muted py-4">
                        <i class="bi bi-exclamation-triangle fs-1 text-warning"></i>
                        <p class="mb-0 mt-2">No se pudo cargar el detalle del pedido.</p>
                    </div>
                `;
            });
}

function formatCurrency(value) {
    const number = Number(value || 0);
    return number.toFixed(2);
}

function escapeHtml(value) {
    return String(value)
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#039;');
}

function checkConfirmedOrderNotifications() {
    fetch('/mis-pedidos/confirmados')
            .then(response => {
                if (!response.ok) {
                    throw new Error('No se pudieron cargar pedidos confirmados.');
                }
                return response.json();
            })
            .then(pedidos => {
                const storageKey = 'cachina_confirmed_orders_notified';
                const notifiedIds = JSON.parse(localStorage.getItem(storageKey) || '[]');
                const currentIds = pedidos.map(pedido => String(pedido.id));
                const newConfirmed = pedidos.filter(pedido => !notifiedIds.includes(String(pedido.id)));

                if (newConfirmed.length > 0) {
                    showConfirmedOrdersAlert(newConfirmed);
                    localStorage.setItem(storageKey, JSON.stringify([...new Set([...notifiedIds, ...currentIds])]));
                }
            })
            .catch(error => console.error(error));
}

function showConfirmedOrdersAlert(pedidos) {
    const plural = pedidos.length > 1;
    const title = plural ? 'Tus pedidos fueron confirmados' : 'Tu pedido fue confirmado';
    const text = plural
            ? `Tienes ${pedidos.length} pedidos confirmados. Revisa tu historial de pedidos.`
            : `El pedido #${pedidos[0].id} fue confirmado. Revisa tu historial de pedidos.`;

    if (typeof Swal !== 'undefined') {
        Swal.fire({
            icon: 'success',
            title: title,
            text: text,
            confirmButtonText: 'Ver historial',
            showCancelButton: true,
            cancelButtonText: 'Cerrar',
            confirmButtonColor: '#0277bd'
        }).then(result => {
            if (result.isConfirmed) {
                window.location.href = '/historial-pedidos';
            }
        });
        return;
    }

    alert(text);
}
//El camarón :D función
// Función para desplazarse hacia el principio de la página
function scrollToTop() {
    const btn = document.getElementById("backToTopBtn");

    // Añade la clase de rotación
    btn.classList.add('rotate');

    // Desplazamiento suave hacia arriba
    window.scrollTo({top: 0, behavior: 'smooth'});

    // Remueve la clase de rotación después de 1 segundo
    setTimeout(() => {
        btn.classList.remove('rotate');
    }, 1000);
}
// Mostrar/ocultar el botón según la posición de desplazamiento
window.onscroll = function () {
    const btn = document.getElementById("backToTopBtn");
    if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
        btn.style.display = "block";
    } else {
        btn.style.display = "none";
    }
};
//Botón de cargar  modales
function initializeModalButtons() {
    document.querySelectorAll('button[data-bs-toggle="modal"]').forEach(button => {
        button.addEventListener('click', function () {
            const targetModalId = this.getAttribute('data-bs-target').substring(1);
            let modalElement = document.getElementById(targetModalId);
            if (!modalElement) {
                fetch('modales/modales.html')
                        .then(response => response.text())
                        .then(data => {
                            // Crear un contenedor temporal para los modales
                            const tempContainer = document.createElement('div');
                            tempContainer.innerHTML = data;
                            document.body.appendChild(tempContainer);

                            modalElement = document.getElementById(targetModalId);
                            if (modalElement) {
                                const modal = new bootstrap.Modal(modalElement, {
                                    keyboard: true
                                });
                                modal.show();
                                modalElement.addEventListener('hidden.bs.modal', function () {
                                    // Eliminar el modal del DOM
                                    modalElement.remove();
                                    // Eliminar el backdrop si existe
                                    const backdrop = document.querySelector('.modal-backdrop');
                                    if (backdrop)
                                        backdrop.remove();
                                    // Reiniciar el overflow del body
                                    document.body.style = '';
                                });
                            } else {
                                console.error('El modal solicitado no se encontró después de cargar.');
                            }
                        })
                        .catch(error => console.error('Error al cargar el modal:', error));
            } else {
                const modal = new bootstrap.Modal(modalElement, {
                    keyboard: true
                });
                modal.show();
                modalElement.addEventListener('hidden.bs.modal', function () {
                    // Eliminar el modal del DOM
                    modalElement.remove();
                    // Eliminar el backdrop si existe
                    const backdrop = document.querySelector('.modal-backdrop');
                    if (backdrop)
                        backdrop.remove();
                    // Reiniciar el overflow del body
                    document.body.style = '';
                });
            }
        });
    });
}

