# URL Shortener

Un acortador de URLs. Recibe una dirección larga, te devuelve un código corto. Después cuando alguien visita ese código corto, el programa lo redirige a la URL original.
Hay que guardar la relación código <--> URL original.

## Entidad ShortUrl
Forma de una fila en la base de datos, escrita como una clase Java. Tiene 3 campos: id (pk auto-increment), url(dirección original) y urlShort(código corto único y máximo 10ch). Hibernate lee la clase y crea la tabla short_url.

## Cofiguración en application.properties
La idea central es que Spring es declarativo, no se le dan ordenes paso a paso, se le pegan etiquetas (las anotaciones, @Entity, @Id...) y el framework las lee y hace el trabajo. Hay que describir que se quiere; Spring y Hibernate resuelven el cómo.

## Hibernate
Traductor entre objetos Java en memoria y filas en una tabla SQL. Genera el esquema y el SQL a partir de la clase.

## La inyección/ generación automática:
Se declaran contratos (una clase entidad y una interfaz repositorio) y Spring, al arrancar, los detecta, fabrica lo que falta y lo conecta todo solo. Por eso el repositorio va a ser una interfaz vacía que funciona.