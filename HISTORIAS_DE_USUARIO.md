# Historias de Usuario - PTC Agency Demo

## Sistema de Gestion para Agencia de Viajes Peru Travel Company (PTC)

**Version:** 1.0
**Fecha:** Enero 2026
**Proyecto:** ptcagencydemo - Backend API REST

---

## Indice

1. [Modulo de Autenticacion](#1-modulo-de-autenticacion)
2. [Modulo de Usuarios](#2-modulo-de-usuarios)
3. [Modulo de Personal (Staff)](#3-modulo-de-personal-staff)
4. [Modulo de Clientes](#4-modulo-de-clientes)
5. [Modulo de Liquidaciones](#5-modulo-de-liquidaciones)
6. [Modulo de Servicios de Viaje](#6-modulo-de-servicios-de-viaje)
7. [Modulo de Pagos](#7-modulo-de-pagos)
8. [Modulo de Incidencias](#8-modulo-de-incidencias)
9. [Modulo de Notificaciones](#9-modulo-de-notificaciones)
10. [Modulo de Reportes y PDF](#10-modulo-de-reportes-y-pdf)

---

## 1. Modulo de Autenticacion

### HU-AUTH-001: Iniciar sesion en el sistema
**Como** usuario del sistema
**Quiero** poder iniciar sesion con mis credenciales (email y contrasena)
**Para** acceder a las funcionalidades del sistema de manera segura

**Criterios de Aceptacion:**
- El sistema debe validar que el email y contrasena sean correctos
- Al autenticarse exitosamente, el sistema debe devolver un token de acceso (JWT) y un refresh token
- El sistema debe registrar la informacion de la sesion (IP, User-Agent)
- Si las credenciales son incorrectas, debe mostrar un mensaje de error apropiado
- La contrasena debe estar encriptada en la base de datos

**Endpoint:** `POST /auth/login`

---

### HU-AUTH-002: Cerrar sesion
**Como** usuario autenticado
**Quiero** poder cerrar mi sesion actual
**Para** proteger mi cuenta cuando termine de usar el sistema

**Criterios de Aceptacion:**
- El sistema debe revocar el token de acceso actual
- La sesion debe marcarse como inactiva en la base de datos
- El usuario debe ser redirigido a la pantalla de login
- El token revocado no debe permitir acceso posterior

**Endpoint:** `POST /auth/logout`

---

### HU-AUTH-003: Cerrar todas las sesiones
**Como** usuario autenticado
**Quiero** poder cerrar todas mis sesiones activas en todos los dispositivos
**Para** proteger mi cuenta si sospecho de acceso no autorizado

**Criterios de Aceptacion:**
- El sistema debe revocar todos los tokens activos del usuario
- Todas las sesiones del usuario deben marcarse como inactivas
- El usuario debe recibir confirmacion de que todas las sesiones fueron cerradas

**Endpoint:** `POST /auth/logout-all`

---

### HU-AUTH-004: Refrescar token de acceso
**Como** usuario autenticado
**Quiero** que mi sesion se mantenga activa mediante la renovacion automatica del token
**Para** no tener que iniciar sesion frecuentemente

**Criterios de Aceptacion:**
- El sistema debe aceptar un refresh token valido
- Debe generar un nuevo par de tokens (acceso y refresh)
- El refresh token anterior debe invalidarse (rotacion de tokens)
- Si el refresh token ha expirado, debe requerir nuevo login

**Endpoint:** `POST /auth/refresh`

---

### HU-AUTH-005: Cambiar contrasena
**Como** usuario autenticado
**Quiero** poder cambiar mi contrasena
**Para** mantener la seguridad de mi cuenta

**Criterios de Aceptacion:**
- El usuario debe proporcionar su contrasena actual
- La nueva contrasena debe cumplir con requisitos minimos de seguridad
- El sistema debe validar que la contrasena actual sea correcta
- La nueva contrasena debe encriptarse antes de guardarse
- El usuario debe recibir confirmacion del cambio exitoso

**Endpoint:** `POST /auth/change-password`

---

### HU-AUTH-006: Obtener informacion del usuario actual
**Como** usuario autenticado
**Quiero** poder ver mi informacion de perfil
**Para** verificar mis datos en el sistema

**Criterios de Aceptacion:**
- El sistema debe devolver los datos del usuario autenticado
- Debe incluir: ID, nombre de usuario, email y rol
- No debe exponer informacion sensible como la contrasena

**Endpoint:** `GET /auth/me`

---

### HU-AUTH-007: Ver sesiones activas
**Como** usuario autenticado
**Quiero** ver todas mis sesiones activas
**Para** identificar desde donde he accedido a mi cuenta

**Criterios de Aceptacion:**
- El sistema debe listar todas las sesiones activas del usuario
- Debe mostrar: dispositivo/navegador, direccion IP, fecha de creacion
- Debe identificar cual es la sesion actual
- El usuario debe poder identificar sesiones sospechosas

**Endpoint:** `GET /auth/sessions`

---

## 2. Modulo de Usuarios

### HU-USER-001: Crear nuevo usuario
**Como** administrador del sistema
**Quiero** poder crear nuevos usuarios
**Para** dar acceso a nuevos miembros del equipo

**Criterios de Aceptacion:**
- El sistema debe validar que el email no este duplicado
- El sistema debe validar que el nombre de usuario sea unico
- La contrasena debe encriptarse antes de guardarse
- El usuario debe crearse con estado activo por defecto
- Se deben registrar fechas de creacion y actualizacion

**Endpoint:** `POST /users`

---

### HU-USER-002: Obtener usuario por ID
**Como** administrador del sistema
**Quiero** poder consultar la informacion de un usuario especifico
**Para** verificar sus datos o gestionar su cuenta

**Criterios de Aceptacion:**
- El sistema debe buscar el usuario por su ID
- Si el usuario existe, debe devolver toda su informacion (excepto contrasena)
- Si no existe, debe devolver error 404

**Endpoint:** `GET /users/{id}`

---

### HU-USER-003: Listar usuarios paginados
**Como** administrador del sistema
**Quiero** poder ver una lista paginada de todos los usuarios
**Para** gestionar las cuentas del sistema

**Criterios de Aceptacion:**
- El sistema debe soportar paginacion (numero de pagina y tamano)
- Debe permitir ordenamiento por diferentes campos
- Debe devolver informacion de la paginacion (total, paginas, etc.)
- Solo debe mostrar usuarios activos por defecto

**Endpoint:** `GET /users/paginados`

---

### HU-USER-004: Actualizar usuario
**Como** administrador del sistema
**Quiero** poder actualizar la informacion de un usuario
**Para** mantener los datos actualizados

**Criterios de Aceptacion:**
- El sistema debe validar que el usuario existe
- Debe validar que el nuevo email/username no esten duplicados
- Debe actualizar la fecha de modificacion
- Solo debe actualizar los campos proporcionados

**Endpoint:** `PUT /users/{id}`

---

### HU-USER-005: Desactivar usuario
**Como** administrador del sistema
**Quiero** poder desactivar un usuario
**Para** revocar su acceso sin eliminar sus datos historicos

**Criterios de Aceptacion:**
- El sistema debe marcar el usuario como inactivo (soft delete)
- El usuario desactivado no debe poder iniciar sesion
- Los datos del usuario deben conservarse para consultas historicas
- Debe actualizar la fecha de modificacion

**Endpoint:** `DELETE /users/{id}`

---

## 3. Modulo de Personal (Staff)

### HU-STAFF-001: Crear nuevo miembro del personal
**Como** administrador del sistema
**Quiero** poder registrar un nuevo miembro del personal
**Para** asignarle responsabilidades en la agencia

**Criterios de Aceptacion:**
- El sistema debe requerir un usuario existente
- Debe registrar: rol, telefono, fecha de contratacion, salario, moneda
- Los roles disponibles son: SALES, COUNTER, ACCOUNTING, OPERATIONS, SUPERADMIN, SUPPORT
- Las monedas disponibles son: PEN, USD
- Debe validar que el usuario no tenga ya un registro de staff

**Endpoint:** `POST /staff`

---

### HU-STAFF-002: Crear usuario con staff en una operacion
**Como** administrador del sistema
**Quiero** poder crear un usuario y su perfil de staff simultaneamente
**Para** agilizar el proceso de alta de nuevos empleados

**Criterios de Aceptacion:**
- El sistema debe crear el usuario y el staff en una transaccion atomica
- Si falla alguna parte, debe hacer rollback completo
- Debe validar todos los datos de usuario y staff
- Debe devolver la informacion completa del staff creado

**Endpoint:** `POST /staff/with-user`

---

### HU-STAFF-003: Obtener personal por ID
**Como** usuario del sistema
**Quiero** poder consultar la informacion de un miembro del personal
**Para** ver sus datos y contactarlo si es necesario

**Criterios de Aceptacion:**
- El sistema debe buscar el staff por su ID
- Debe incluir la informacion del usuario asociado
- Si no existe, debe devolver error 404

**Endpoint:** `GET /staff/{id}`

---

### HU-STAFF-004: Listar personal paginado
**Como** administrador del sistema
**Quiero** ver una lista paginada del personal
**Para** gestionar el equipo de trabajo

**Criterios de Aceptacion:**
- El sistema debe soportar paginacion
- Debe permitir ordenamiento
- Debe incluir informacion basica de cada miembro

**Endpoint:** `GET /staff/paginados`

---

### HU-STAFF-005: Filtrar personal por rol
**Como** usuario del sistema
**Quiero** poder filtrar el personal por su rol
**Para** encontrar rapidamente personal de un area especifica

**Criterios de Aceptacion:**
- El sistema debe filtrar por el rol especificado
- Debe devolver solo personal con estado activo
- Los roles validos son: SALES, COUNTER, ACCOUNTING, OPERATIONS, SUPERADMIN, SUPPORT

**Endpoint:** `GET /staff/by-role/{role}`

---

### HU-STAFF-006: Actualizar informacion del personal
**Como** administrador del sistema
**Quiero** poder actualizar los datos de un miembro del personal
**Para** mantener la informacion actualizada

**Criterios de Aceptacion:**
- Debe validar que el staff existe
- Debe permitir actualizar: rol, telefono, salario, moneda
- Debe actualizar la fecha de modificacion

**Endpoint:** `PUT /staff/{id}`

---

### HU-STAFF-007: Desactivar miembro del personal
**Como** administrador del sistema
**Quiero** poder desactivar un miembro del personal
**Para** registrar su baja sin perder el historial

**Criterios de Aceptacion:**
- El sistema debe marcar el staff como inactivo
- El usuario asociado debe poder mantenerse activo o desactivarse segun sea necesario
- Los datos historicos deben conservarse

**Endpoint:** `DELETE /staff/{id}`

---

## 4. Modulo de Clientes

### HU-CUST-001: Crear nuevo cliente
**Como** agente de ventas
**Quiero** poder registrar un nuevo cliente
**Para** gestionar sus reservas de viaje

**Criterios de Aceptacion:**
- El sistema debe registrar: nombre, apellido, fecha de nacimiento, tipo y numero de documento
- Tipos de documento validos: PASSPORT, DNI, DRIVER_LICENSE, RUC, CE
- Opcionalmente: email, telefono, direccion, nacionalidad
- El numero de documento debe ser unico
- El email y telefono deben ser unicos si se proporcionan

**Endpoint:** `POST /clientes`

---

### HU-CUST-002: Listar todos los clientes
**Como** usuario del sistema
**Quiero** ver una lista de todos los clientes
**Para** buscar rapidamente un cliente existente

**Criterios de Aceptacion:**
- El sistema debe devolver todos los clientes activos
- Debe incluir informacion basica de cada cliente

**Endpoint:** `GET /clientes`

---

### HU-CUST-003: Listar clientes paginados
**Como** usuario del sistema
**Quiero** ver una lista paginada de clientes
**Para** navegar eficientemente por grandes volumenes de datos

**Criterios de Aceptacion:**
- El sistema debe soportar paginacion
- Debe permitir ordenamiento por diferentes campos
- Debe devolver metadata de paginacion

**Endpoint:** `GET /clientes/paginados`

---

### HU-CUST-004: Actualizar cliente
**Como** agente de ventas
**Quiero** poder actualizar los datos de un cliente
**Para** mantener su informacion actualizada

**Criterios de Aceptacion:**
- Debe validar que el cliente existe
- Debe validar unicidad de documento, email y telefono
- Debe actualizar la fecha de modificacion

**Endpoint:** `PUT /clientes/{id}`

---

### HU-CUST-005: Desactivar cliente
**Como** administrador del sistema
**Quiero** poder desactivar un cliente
**Para** mantener el historial sin mostrar clientes inactivos

**Criterios de Aceptacion:**
- El sistema debe marcar el cliente como inactivo
- Las liquidaciones asociadas deben mantenerse para consulta historica

**Endpoint:** `DELETE /clientes/{id}`

---

## 5. Modulo de Liquidaciones

### HU-LIQ-001: Crear nueva liquidacion
**Como** agente de ventas
**Quiero** crear una nueva liquidacion (reserva)
**Para** gestionar los servicios de viaje de un cliente

**Criterios de Aceptacion:**
- El sistema debe requerir: cliente, staff responsable, tipo de cambio, fecha limite de pago, numero de acompanantes
- El estado inicial debe ser "IN_QUOTE" (en cotizacion)
- El estado de pago inicial debe ser "PENDING"
- Debe calcular automaticamente los totales en PEN y USD

**Endpoint:** `POST /liquidations`

---

### HU-LIQ-002: Listar liquidaciones paginadas
**Como** usuario del sistema
**Quiero** ver una lista paginada de liquidaciones
**Para** gestionar las reservas de la agencia

**Criterios de Aceptacion:**
- El sistema debe soportar paginacion y ordenamiento
- Debe incluir detalles de cliente, staff y servicios asociados
- Debe mostrar totales y estados actuales

**Endpoint:** `GET /liquidations/paginated`

---

### HU-LIQ-003: Obtener liquidacion por ID
**Como** usuario del sistema
**Quiero** ver el detalle completo de una liquidacion
**Para** gestionar sus servicios y pagos

**Criterios de Aceptacion:**
- Debe devolver toda la informacion de la liquidacion
- Debe incluir: tours, vuelos, hoteles, servicios adicionales, pagos, incidencias
- Debe incluir informacion del cliente y staff responsable
- Si no existe, debe devolver error 404

**Endpoint:** `GET /liquidations/{liquidationId}`

---

### HU-LIQ-004: Filtrar liquidaciones por cliente
**Como** agente de ventas
**Quiero** ver todas las liquidaciones de un cliente especifico
**Para** revisar su historial de viajes

**Criterios de Aceptacion:**
- El sistema debe filtrar por el ID del cliente
- Debe soportar paginacion
- Debe ordenar por fecha de creacion descendente

**Endpoint:** `GET /liquidations/customer/{customerId}`

---

### HU-LIQ-005: Filtrar liquidaciones por estado
**Como** usuario del sistema
**Quiero** filtrar liquidaciones por su estado
**Para** gestionar las que requieren atencion

**Criterios de Aceptacion:**
- Estados validos: IN_QUOTE, PENDING, ON_COURSE, COMPLETED
- Debe soportar paginacion
- Debe mostrar solo liquidaciones activas

**Endpoint:** `GET /liquidations/status/{status}`

---

### HU-LIQ-006: Actualizar estado de liquidacion
**Como** agente de ventas
**Quiero** cambiar el estado de una liquidacion
**Para** reflejar su progreso

**Criterios de Aceptacion:**
- Estados permitidos: IN_QUOTE -> PENDING -> ON_COURSE -> COMPLETED
- Debe validar transiciones de estado validas
- Debe generar notificacion del cambio de estado

**Endpoint:** `PUT /liquidations/{liquidationId}/status`

---

### HU-LIQ-007: Actualizar estado de pago de liquidacion
**Como** contador
**Quiero** actualizar el estado de pago de una liquidacion
**Para** reflejar el progreso de los pagos

**Criterios de Aceptacion:**
- Estados de pago: PENDING, ON_COURSE, COMPLETED
- Debe validar que existan pagos registrados
- Debe generar notificacion del cambio

**Endpoint:** `PUT /liquidations/{liquidationId}/payment-status`

---

### HU-LIQ-008: Eliminar liquidacion (soft delete)
**Como** administrador del sistema
**Quiero** poder eliminar una liquidacion
**Para** removerla de las listas activas manteniendo el historial

**Criterios de Aceptacion:**
- El sistema debe marcar la liquidacion como inactiva
- Debe marcar como inactivos todos los servicios asociados
- Los datos deben conservarse para consultas historicas
- Debe generar notificacion de eliminacion

**Endpoint:** `DELETE /liquidations/{liquidationId}`

---

## 6. Modulo de Servicios de Viaje

### HU-TOUR-001: Agregar servicio de tour a liquidacion
**Como** agente de ventas
**Quiero** agregar un tour a una liquidacion
**Para** incluir paquetes turisticos en la reserva

**Criterios de Aceptacion:**
- El sistema debe registrar: titulo, lugar, fecha inicio/fin, precio, moneda
- Debe asociarse a un servicio de tour con tarifa e impuesto
- Estados del tour: PENDING, COMPLETED, CANCELED
- Debe recalcular el total de la liquidacion

**Endpoint:** `POST /liquidations/{liquidationId}/tour-services`

---

### HU-TOUR-002: Actualizar tour
**Como** agente de ventas
**Quiero** actualizar los datos de un tour
**Para** corregir o modificar la informacion

**Criterios de Aceptacion:**
- Debe validar que el tour pertenece a la liquidacion
- Debe permitir actualizar: titulo, lugar, fechas, precio, estado
- Debe recalcular el total de la liquidacion

**Endpoint:** `PUT /liquidations/{liquidationId}/tour-services/{tourServiceId}/tours/{tourId}`

---

### HU-TOUR-003: Eliminar tour (soft delete)
**Como** agente de ventas
**Quiero** eliminar un tour de una liquidacion
**Para** removerlo de la cotizacion

**Criterios de Aceptacion:**
- Debe marcar el tour como inactivo
- Debe recalcular el total de la liquidacion
- Debe generar notificacion del cambio

**Endpoint:** `DELETE /liquidations/{liquidationId}/tour-services/{tourServiceId}/tours/{tourId}`

---

### HU-FLIGHT-001: Agregar servicio de vuelo a liquidacion
**Como** agente de ventas
**Quiero** agregar reservas de vuelo a una liquidacion
**Para** incluir boletos aereos en la reserva

**Criterios de Aceptacion:**
- El sistema debe registrar: aerolinea, codigos de reserva, origen, destino, fechas, precio
- Debe incluir numeros de ticket
- Codigo de reserva de aerolinea es obligatorio
- Codigo Costamar es opcional
- Debe recalcular el total de la liquidacion

**Endpoint:** `POST /liquidations/{liquidationId}/flight-services`

---

### HU-FLIGHT-002: Actualizar reserva de vuelo
**Como** agente de ventas
**Quiero** actualizar los datos de una reserva de vuelo
**Para** corregir o modificar la informacion

**Criterios de Aceptacion:**
- Debe validar que la reserva pertenece a la liquidacion
- Debe permitir actualizar todos los campos del vuelo
- Debe recalcular el total si cambia el precio

**Endpoint:** `PUT /liquidations/{liquidationId}/flight-services/{flightServiceId}/bookings/{flightBookingId}`

---

### HU-FLIGHT-003: Eliminar reserva de vuelo (soft delete)
**Como** agente de ventas
**Quiero** eliminar una reserva de vuelo
**Para** removerla de la liquidacion

**Criterios de Aceptacion:**
- Debe marcar la reserva como inactiva
- Debe recalcular el total de la liquidacion

**Endpoint:** `DELETE /liquidations/{liquidationId}/flight-services/{flightServiceId}/bookings/{flightBookingId}`

---

### HU-HOTEL-001: Agregar servicio de hotel a liquidacion
**Como** agente de ventas
**Quiero** agregar reservas de hotel a una liquidacion
**Para** incluir hospedaje en la reserva

**Criterios de Aceptacion:**
- El sistema debe registrar: nombre del hotel, tipo de habitacion, descripcion, fechas check-in/check-out, precio por noche
- Debe calcular automaticamente el total segun las noches
- Debe recalcular el total de la liquidacion

**Endpoint:** `POST /liquidations/{liquidationId}/hotel-services`

---

### HU-HOTEL-002: Actualizar reserva de hotel
**Como** agente de ventas
**Quiero** actualizar los datos de una reserva de hotel
**Para** modificar fechas o habitacion

**Criterios de Aceptacion:**
- Debe validar que la reserva pertenece a la liquidacion
- Debe recalcular el total si cambian fechas o precio

**Endpoint:** `PUT /liquidations/{liquidationId}/hotel-services/{hotelServiceId}/bookings/{hotelBookingId}`

---

### HU-HOTEL-003: Eliminar reserva de hotel (soft delete)
**Como** agente de ventas
**Quiero** eliminar una reserva de hotel
**Para** removerla de la liquidacion

**Criterios de Aceptacion:**
- Debe marcar la reserva como inactiva
- Debe recalcular el total de la liquidacion

**Endpoint:** `DELETE /liquidations/{liquidationId}/hotel-services/{hotelServiceId}/bookings/{hotelBookingId}`

---

### HU-ADD-001: Agregar servicio adicional a liquidacion
**Como** agente de ventas
**Quiero** agregar servicios adicionales a una liquidacion
**Para** incluir traslados, seguros u otros servicios

**Criterios de Aceptacion:**
- El sistema debe registrar: precio, moneda, tarifa, impuesto
- Debe recalcular el total de la liquidacion

**Endpoint:** `POST /liquidations/{liquidationId}/additional-services`

---

### HU-ADD-002: Actualizar servicio adicional
**Como** agente de ventas
**Quiero** actualizar un servicio adicional
**Para** modificar su informacion

**Criterios de Aceptacion:**
- Debe validar que el servicio pertenece a la liquidacion
- Debe recalcular el total si cambia el precio

**Endpoint:** `PUT /liquidations/{liquidationId}/additional-services/{additionalServiceId}`

---

### HU-ADD-003: Eliminar servicio adicional (soft delete)
**Como** agente de ventas
**Quiero** eliminar un servicio adicional
**Para** removerlo de la liquidacion

**Criterios de Aceptacion:**
- Debe marcar el servicio como inactivo
- Debe recalcular el total de la liquidacion

**Endpoint:** `DELETE /liquidations/{liquidationId}/additional-services/{additionalServiceId}`

---

## 7. Modulo de Pagos

### HU-PAY-001: Registrar pago en liquidacion
**Como** contador
**Quiero** registrar un pago en una liquidacion
**Para** llevar control de los abonos del cliente

**Criterios de Aceptacion:**
- El sistema debe registrar: monto, metodo de pago, moneda, URL de evidencia
- Metodos de pago validos: DEBIT, CREDIT, YAPE, OTHER
- Monedas validas: PEN, USD
- El estado inicial del pago debe ser "PENDING" (pendiente de validacion)
- La URL de evidencia es opcional
- Debe generar notificacion del nuevo pago

**Endpoint:** `POST /liquidations/{liquidationId}/payments`

---

### HU-PAY-002: Actualizar pago
**Como** contador
**Quiero** actualizar la informacion de un pago
**Para** corregir datos o agregar evidencia

**Criterios de Aceptacion:**
- Debe validar que el pago pertenece a la liquidacion
- Debe permitir actualizar: monto, metodo, moneda, evidencia
- Debe actualizar la fecha de modificacion

**Endpoint:** `PUT /liquidations/{liquidationId}/payments/{paymentId}`

---

### HU-PAY-003: Validar estado de pago
**Como** supervisor
**Quiero** cambiar el estado de validacion de un pago
**Para** aprobar o rechazar pagos registrados

**Criterios de Aceptacion:**
- Estados de validacion: PENDING, VALID, INVALID
- Debe requerir revision de la evidencia antes de validar
- Debe generar notificacion del cambio de estado

**Endpoint:** `PUT /liquidations/{liquidationId}/payment-status`

---

### HU-PAY-004: Eliminar pago (soft delete)
**Como** contador
**Quiero** eliminar un pago registrado
**Para** corregir errores de registro

**Criterios de Aceptacion:**
- Debe marcar el pago como inactivo
- Debe actualizar el total pagado de la liquidacion
- Debe generar notificacion de eliminacion

**Endpoint:** `DELETE /liquidations/{liquidationId}/payments/{paymentId}`

---

## 8. Modulo de Incidencias

### HU-INC-001: Registrar incidencia en liquidacion
**Como** agente de operaciones
**Quiero** registrar una incidencia en una liquidacion
**Para** documentar problemas ocurridos durante el viaje

**Criterios de Aceptacion:**
- El sistema debe registrar: razon, fecha de incidencia, monto (si aplica)
- El estado inicial debe ser "PENDING"
- La razon puede tener hasta 500 caracteres
- Debe generar notificacion de la nueva incidencia

**Endpoint:** `POST /liquidations/{liquidationId}/incidencies`

---

### HU-INC-002: Actualizar incidencia
**Como** agente de operaciones
**Quiero** actualizar una incidencia
**Para** agregar informacion o cambiar su estado

**Criterios de Aceptacion:**
- Estados permitidos: PENDING, APPROVED, REJECTED
- Debe permitir actualizar: razon, fecha, monto, estado
- Debe generar notificacion si cambia el estado

**Endpoint:** `PUT /liquidations/{liquidationId}/incidencies/{incidencyId}`

---

### HU-INC-003: Eliminar incidencia (soft delete)
**Como** supervisor
**Quiero** eliminar una incidencia
**Para** remover registros erroneos

**Criterios de Aceptacion:**
- Debe marcar la incidencia como inactiva
- Los datos deben conservarse para auditoria

**Endpoint:** `DELETE /liquidations/{liquidationId}/incidencies/{incidencyId}`

---

## 9. Modulo de Notificaciones

### HU-NOT-001: Suscribirse a notificaciones en tiempo real
**Como** usuario del sistema
**Quiero** recibir notificaciones en tiempo real
**Para** estar al tanto de cambios importantes

**Criterios de Aceptacion:**
- El sistema debe usar Server-Sent Events (SSE)
- La conexion debe mantenerse activa mientras el usuario este conectado
- Debe recibir notificaciones de: liquidaciones, pagos, incidencias, servicios
- Debe poder reconectarse automaticamente si se pierde la conexion

**Endpoint:** `GET /notifications/subscribe/{userId}`

---

### HU-NOT-002: Obtener historial de notificaciones
**Como** usuario del sistema
**Quiero** ver mi historial de notificaciones
**Para** revisar eventos pasados

**Criterios de Aceptacion:**
- El sistema debe devolver notificaciones paginadas
- Debe mostrar: titulo, mensaje, tipo, fecha, estado de lectura
- Debe ordenar por fecha descendente (mas recientes primero)
- Tipos de notificacion incluyen: LIQUIDATION_CREATED, PAYMENT_ADDED, INCIDENCY_CREATED, etc.

**Endpoint:** `GET /notifications/user/{userId}`

---

### HU-NOT-003: Marcar notificacion como leida
**Como** usuario del sistema
**Quiero** marcar una notificacion como leida
**Para** organizar mis notificaciones pendientes

**Criterios de Aceptacion:**
- Debe actualizar el estado de lectura a "true"
- Debe actualizar la fecha de modificacion
- La notificacion marcada no debe aparecer como pendiente

**Endpoint:** `PUT /notifications/{userNotificationId}/mark-as-read`

---

## 10. Modulo de Reportes y PDF

### HU-PDF-001: Generar cotizacion en PDF
**Como** agente de ventas
**Quiero** generar un PDF de cotizacion
**Para** enviar al cliente un documento formal

**Criterios de Aceptacion:**
- Solo disponible para liquidaciones en estado "IN_QUOTE"
- El PDF debe incluir: datos del cliente, detalle de servicios, precios, totales
- Debe incluir logo y datos de la agencia
- El nombre del archivo debe ser: cotizacion_XXXXXX.pdf (donde XXXXXX es el ID)
- El archivo debe descargarse automaticamente

**Endpoint:** `GET /liquidations/{liquidationId}/quote-pdf`

---

## Resumen de Funcionalidades por Modulo

| Modulo | Historias de Usuario | Endpoints |
|--------|---------------------|-----------|
| Autenticacion | 7 | 7 |
| Usuarios | 5 | 5 |
| Personal (Staff) | 7 | 7 |
| Clientes | 5 | 5 |
| Liquidaciones | 8 | 8 |
| Servicios de Viaje | 12 | 12 |
| Pagos | 4 | 4 |
| Incidencias | 3 | 3 |
| Notificaciones | 3 | 3 |
| Reportes/PDF | 1 | 1 |
| **Total** | **55** | **55** |

---

## Tipos de Datos y Enumeraciones

### Estados de Liquidacion (LiquidationStatus)
- `IN_QUOTE` - En cotizacion
- `PENDING` - Pendiente
- `ON_COURSE` - En curso
- `COMPLETED` - Completada

### Estados de Pago (PaymentStatus)
- `PENDING` - Pendiente
- `ON_COURSE` - En curso
- `COMPLETED` - Completado

### Validacion de Pago (PaymentValidity)
- `PENDING` - Pendiente de validacion
- `VALID` - Valido
- `INVALID` - Invalido

### Metodos de Pago (PaymentMethod)
- `DEBIT` - Tarjeta de debito
- `CREDIT` - Tarjeta de credito
- `YAPE` - Yape
- `OTHER` - Otro

### Estados de Servicio (ServiceStatus)
- `PENDING` - Pendiente
- `COMPLETED` - Completado
- `CANCELED` - Cancelado

### Estados de Incidencia (IncidencyStatus)
- `PENDING` - Pendiente
- `APPROVED` - Aprobada
- `REJECTED` - Rechazada

### Roles del Personal (Roles)
- `SALES` - Ventas
- `COUNTER` - Mostrador
- `ACCOUNTING` - Contabilidad
- `OPERATIONS` - Operaciones
- `SUPERADMIN` - Super Administrador
- `SUPPORT` - Soporte

### Tipos de Documento (IdDocumentType)
- `PASSPORT` - Pasaporte
- `DNI` - DNI
- `DRIVER_LICENSE` - Licencia de conducir
- `RUC` - RUC
- `CE` - Carnet de Extranjeria

### Monedas (Currency)
- `PEN` - Soles peruanos
- `USD` - Dolares americanos

---

## Arquitectura Tecnica

- **Framework:** Spring Boot 3.5
- **Arquitectura:** Clean Architecture + CQRS + DDD
- **Base de Datos:** PostgreSQL 16+
- **Autenticacion:** JWT con refresh tokens
- **Notificaciones:** Server-Sent Events (SSE)
- **Documentacion API:** OpenAPI/Swagger
- **Migraciones:** Flyway
