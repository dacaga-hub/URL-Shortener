# URL Shortener

Acorta URLs largas a un código corto y redirige de vuelta al visitar ese código.
Proyecto de aprendizaje de Spring Boot con foco en TDD y decisiones de diseño explícitas.

## Stack
- Java 21
- Spring Boot 4.1 (Web, Data JPA, Validation)
- H2 (base de datos en memoria)
- Maven, JUnit 5

## Ejecutar
```bash
./mvnw spring-boot:run    # arranca en http://localhost:8080
./mvnw test               # ejecuta los tests
```

## Decisiones de diseño
- **Código corto vía Base62 desde el id**: Se usa Base62 sobre la ID secuencial por ser determinista, libre de colisiones y rápida de calcular. Una estrategia aleatoria requeriría comprobar colisiones en la base de datos a cada intento. Migraría a un código aleatorio si la secuencialidad permitiera adivinar URLs ajenas recorriendo los códigos (problema de enumeración / privacidad). Escenarios de generación de ids en sistemas distribuidos quedan fuera del alcance de este proyecto.
- **GenerationType.IDENTITY**: Permite delegar la autogeneración de claves a la BD de forma sencilla, ideal para prototipos sin inserciones masivas, aunque deshabilita el batch insert en JPA.

## Estado
En desarrollo. Hecho: entidad, repositorio, encoder base62 (testeado).
Pendiente: capa de servicio, controlador REST, redirección.