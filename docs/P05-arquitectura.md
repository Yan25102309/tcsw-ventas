# ADR 01: Adopción de Arquitectura Hexagonal (Puertos y Adaptadores)
* **Estatus:** Aprobado
* **Fecha:** Septiembre 2026
* **Autores:** Equipo de Desarrollo TCSW (Ana María Reyes Hernández y
colaboradores)
## 1. Contexto y Problema
El prototipo de sistema de ventas requiere evolucionar para permitir múltiples
interfaces de usuario (como Swing o APIs) y mecanismos de almacenamiento futuros
(persistencia en base de datos o almacenamiento en memoria) sin alterar las reglas
centrales del negocio.
## 2. Decisión Arquitectónica
Se adopta el patrón de **Arquitectura Hexagonal (Puertos y Adaptadores)**.
* **Núcleo (Core):** Aísla las entidades de dominio (`Producto`) y los servicios de
aplicación (`ProductoService`).
* **Puertos de Entrada:** Interfaz `RegistrarProductoUseCase` con comandos DTO
neutros.
* **Puertos de Salida:** Interfaz `ProductoRepository`.
* **Adaptadores:** Implementación externa `InMemoryProductoRepository`.
## 3. Alternativas Descartadas
1. **Monolito por Capas Tradicional:** Se descarta debido al acoplamiento directo
entre la capa de negocio y la infraestructura de almacenamiento.
2. **Modelo Anémico:** Se descarta para evitar colocar la lógica del dominio dentro
de los controladores o servicios.
## 4. Consecuencias
* **Beneficios:** Prueba aislada de reglas de negocio sin levantar infraestructura;
sustitución limpia de adaptadores; cumplimiento de inversión de dependencias.
* **Costos:** Introducción de interfaces y abstracciones DTO adicionales.