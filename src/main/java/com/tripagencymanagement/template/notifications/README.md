# Sistema de Notificaciones

## Descripción

Sistema de notificaciones en tiempo real usando Server-Sent Events (SSE) que permite enviar notificaciones a usuarios específicos o a todos los usuarios del sistema mediante Domain Events.

## Características

- **Notificaciones en tiempo real** vía SSE
- **Arquitectura basada en Domain Events**
- **Tres tipos de alcance (scope)**:
  - `ALL`: Notificación para todos los usuarios conectados
  - `SELF`: Notificación solo para el usuario que generó la acción
  - `OTHERS`: Notificación para todos excepto el usuario que generó la acción
- **Persistencia automática** en base de datos
- **Consulta paginada** de notificaciones por usuario
- **Marcar como leída** funcionalidad
- **Limpieza automática** de notificaciones antiguas (30 días)

## Endpoints

### 1. Suscribirse a notificaciones (SSE)
```
GET /ptc/api/notifications/subscribe/{userId}
Content-Type: text/event-stream
```

**Ejemplo de uso desde el frontend:**
```javascript
const eventSource = new EventSource(`/ptc/api/notifications/subscribe/${userId}`);

eventSource.addEventListener('notification', (event) => {
    const notification = JSON.parse(event.data);
    console.log('Nueva notificación:', notification);
    // Mostrar notificación en la UI
});

eventSource.onerror = (error) => {
    console.error('Error en SSE:', error);
    eventSource.close();
};
```

### 2. Obtener notificaciones paginadas
```
GET /ptc/api/notifications/user/{userId}?page=1&size=10
```

### 3. Marcar notificación como leída
```
PUT /ptc/api/notifications/{userNotificationId}/mark-as-read
```

## Uso con Domain Events (Patrón Recomendado)

El patrón correcto es usar **Domain Events** y manejar las notificaciones en **Event Handlers**. Esto mantiene la separación de responsabilidades y sigue los principios de arquitectura event-driven.

### Paso 1: Crear el Domain Event

```java
package com.tripagency.ptc.ptcagencydemo.mimodulo.application.events;

import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
public record MiDomainEvent(
    Long entityId,
    Long userId,
    String message
) {
}
```

### Paso 2: Publicar el evento desde el Command Handler

```java
@Service
public class MiCommandHandler {
    
    private final ApplicationEventPublisher eventPublisher;
    
    public MiCommandHandler(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
    
    @Transactional
    public void execute(MiCommand command) {
        // Tu lógica de negocio...
        
        // Publicar evento de dominio
        eventPublisher.publishEvent(new MiDomainEvent(
            entityId,
            userId,
            "Mensaje de la notificación"
        ));
    }
}
```

### Paso 3: Crear el Event Handler que envía la notificación

```java
package com.tripagency.ptc.ptcagencydemo.mimodulo.application.events;

import java.util.Optional;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.tripagency.ptc.ptcagencydemo.notifications.application.services.NotificationService;
import com.tripagency.ptc.ptcagencydemo.notifications.domain.enums.DNotificationScope;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MiEventHandler {
    
    private final NotificationService notificationService;

    public MiEventHandler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @EventListener
    public void handleMiEvento(MiDomainEvent event) {
        log.info("Handling event for entity: {}", event.entityId());
        
        // Enviar notificación
        notificationService.sendNotification(
            event.message(),
            DNotificationScope.ALL,  // o SELF, OTHERS
            Optional.of(event.entityId().toString()),
            event.userId()
        );
        
        log.info("Notification sent for entity: {}", event.entityId());
    }
}
```

## Ejemplos Reales

### Ejemplo 1: Notificación cuando se agrega una incidencia

**Domain Event:**
```java
@DomainEvent
public record IncidencyAddedDomainEvent(
    Long liquidationId,
    Long staffId,
    String reason,
    String message
) {
}
```

**Command Handler:**
```java
@Service
public class AddIncidencyCommandHandler {
    
    private final ApplicationEventPublisher eventPublisher;
    
    @Transactional
    public DLiquidation execute(AddIncidencyCommand command) {
        // Lógica de negocio...
        
        eventPublisher.publishEvent(new IncidencyAddedDomainEvent(
            liquidationId,
            staffId,
            reason,
            "Nueva incidencia agregada a la liquidación #" + liquidationId
        ));
        
        return savedLiquidation;
    }
}
```

**Event Handler:**
```java
@Component
@Slf4j
public class IncidencyAddedEventHandler {
    
    private final NotificationService notificationService;

    @EventListener
    public void handleIncidencyAdded(IncidencyAddedDomainEvent event) {
        // Notificar a todos excepto el que creó la incidencia
        notificationService.sendNotification(
            event.message(),
            DNotificationScope.OTHERS,
            Optional.of(event.liquidationId().toString()),
            event.staffId()
        );
    }
}
```

### Ejemplo 2: Notificación cuando se crea un cliente

**Domain Event:**
```java
@DomainEvent
public record CustomerCreatedDomainEvent(
    Long customerId,
    String message
) {
}
```

**Event Handler:**
```java
@Component
@Slf4j
public class CustomerCreatedEventHandler {
    
    private final NotificationService notificationService;

    @EventListener
    public void handleCustomerCreated(CustomerCreatedDomainEvent event) {
        // Notificar a todos los usuarios
        notificationService.sendNotification(
            event.message(),
            DNotificationScope.ALL,
            Optional.of(event.customerId().toString()),
            null
        );
    }
}
```

## Scopes de Notificación

### ALL
Envía la notificación a todos los usuarios conectados. Útil para:
- Anuncios del sistema
- Nuevos clientes registrados
- Actualizaciones importantes

### SELF
Envía la notificación solo al usuario que realizó la acción. Útil para:
- Confirmaciones de acciones
- Resultados de operaciones
- Notificaciones personales

### OTHERS
Envía la notificación a todos los usuarios excepto el que realizó la acción. Útil para:
- Notificar a otros usuarios sobre cambios
- Alertas de colaboración
- Actualizaciones de estado compartidas

## Limpieza Automática

El sistema ejecuta una tarea programada todos los días a las 2:00 AM que elimina:
- Notificaciones con más de 30 días de antigüedad
- Las entradas correspondientes en `user_notifications`

Para modificar el intervalo, edita la anotación `@Scheduled` en `NotificationCleanupService`:

```java
@Scheduled(cron = "0 0 2 * * *")  // Formato: segundo minuto hora día mes día-semana
```

## Modelo de Datos

### Tabla: notifications
- `id`: Long (PK)
- `message`: String (500 caracteres)
- `scope`: Enum (ALL, SELF, OTHERS)
- `reference_id`: String (opcional)
- `created_at`: LocalDateTime
- `updated_at`: LocalDateTime
- `is_active`: Boolean

### Tabla: user_notifications
- `id`: Long (PK)
- `read`: Boolean
- `user_id`: Long (FK a users)
- `notification_id`: Long (FK a notifications)
- `created_at`: LocalDateTime
- `updated_at`: LocalDateTime
- `is_active`: Boolean

## Arquitectura

```
Command Handler
    ↓
Publica Domain Event
    ↓
Event Handler (escucha el evento)
    ↓
NotificationService (envía notificación)
    ↓
├─ Guarda en BD (Notification + UserNotification)
└─ Envía vía SSE a usuarios conectados
```

## Notas Técnicas

- Las conexiones SSE se mantienen abiertas indefinidamente (`Long.MAX_VALUE`)
- Los emitters se limpian automáticamente en caso de error, timeout o completion
- Las notificaciones se persisten antes de enviarse vía SSE
- El sistema es thread-safe usando `ConcurrentHashMap` para los emitters
- Los Event Handlers se ejecutan de forma asíncrona después del commit de la transacción
