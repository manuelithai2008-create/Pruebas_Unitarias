# Pruebas unitarias en Java con JUnit 5

- **Nombre:** Manuela Dimate Ruiz
- **Ficha:** 3231666
- **Cobertura de líneas en CarritoCompras:** ESCRIBE EL % QUE VISTE EN EL PANEL COVERAGE

## Contenido

- `CalculadoraNotas` y `CalculadoraNotasTest`: 15 pruebas. Bug corregido: `estaAprobado` usaba `>` y reprobaba la nota 3.0 (debía ser `>=`).
- `CarritoCompras` y `CarritoComprasTest`: pruebas de las reglas R1–R6. Bug corregido: `agregarProducto` validaba `cantidad < 0` y dejaba pasar la cantidad 0 (debía ser `cantidad <= 0`).
- `evidencias/`: capturas de las pruebas en verde y de la cobertura.

## Cómo ejecutar

```
mvn test
```

El reporte de JaCoCo queda en `target/site/jacoco/index.html`.
