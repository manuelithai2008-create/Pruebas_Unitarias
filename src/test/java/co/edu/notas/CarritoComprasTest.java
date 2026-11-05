package co.edu.notas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/*
 * ENTREGA DEL TALLER
 * Nombre completo: Manuela Dimate Ruiz (Ficha 3231666)
 * 1. Bug encontrado (método y línea de CarritoCompras.java):
 *    Método agregarProducto, validación de cantidad (aprox. línea 23): usaba
 *    "if (cantidad < 0)" y debía ser "if (cantidad <= 0)". Con cantidad = 0 el
 *    producto se agregaba sin error, violando la regla R1 (cantidad mayor a 0).
 * 2. Prueba que lo detectó (nombre del método):
 *    agregarProducto_cantidadCero_lanzaExcepcion
 * 3. Sugerencia de la IA que rechacé o corregí, y por qué
 *    (si todas eran correctas: cómo verifiqué una de ellas con las reglas):
 *    Todas las pruebas generadas con ayuda de la IA las verifiqué contra las
 *    reglas, no contra el código. Ejemplo: calcularTotal_bajoMinimoEnvioGratis_
 *    incluyeEnvio. Por R6, 100.000 × 1,19 = 119.000, que es menor a 150.000,
 *    así que se suma el envío de 12.000 y el total esperado es 131.000, valor
 *    que calculé a mano. Además, la IA no debe aceptar cantidad 0: la regla R1
 *    exige cantidad mayor a 0, por eso la prueba agregarProducto_cantidadCero_
 *    lanzaExcepcion espera una excepción, aunque el código original no la
 *    lanzaba (ahí estaba el bug).
 */
class CarritoComprasTest {

    private CarritoCompras carrito;

    @BeforeEach
    void setUp() {
        carrito = new CarritoCompras();
    }

   

    @Test
    @DisplayName("Subtotal = precio × cantidad de cada producto")
    void calcularSubtotal_variosProductos_sumaCorrecta() {
        
        carrito.agregarProducto("Cuaderno", 8_000, 3);
        carrito.agregarProducto("Esfero", 2_000, 5);
        // Act
        double subtotal = carrito.calcularSubtotal();
        
        assertEquals(34_000, subtotal, 0.001);
    }

    @Test
    @DisplayName("Carrito vacío: subtotal 0")
    void calcularSubtotal_carritoVacio_retornaCero() {
        assertEquals(0, carrito.calcularSubtotal(), 0.001);
    }

    @Test
    @DisplayName("Carrito vacío: total 0 (sin cobrar envío)")
    void calcularTotal_carritoVacio_retornaCero() {
        assertEquals(0, carrito.calcularTotal(), 0.001);
    }

    @Test
    @DisplayName("Un producto válido se agrega al carrito")
    void agregarProducto_datosValidos_incrementaCantidadDeItems() {
        carrito.agregarProducto("Cuaderno", 8_000, 1);
        assertEquals(1, carrito.cantidadDeItems());
    }

    @Test
    @DisplayName("Rechaza nombre nulo")
    void agregarProducto_nombreNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> carrito.agregarProducto(null, 8_000, 1));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    @DisplayName("Rechaza nombre vacío o solo espacios")
    void agregarProducto_nombreVacio_lanzaExcepcion(String nombre) {
        assertThrows(IllegalArgumentException.class,
                () -> carrito.agregarProducto(nombre, 8_000, 1));
    }

    @Test
    @DisplayName("Rechaza precio 0")
    void agregarProducto_precioCero_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> carrito.agregarProducto("Cuaderno", 0, 1));
    }

    @Test
    @DisplayName("Rechaza precio negativo")
    void agregarProducto_precioNegativo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> carrito.agregarProducto("Cuaderno", -1, 1));
    }

    @Test
    @DisplayName("Caso límite: cantidad 0 se rechaza (R1)")
    void agregarProducto_cantidadCero_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> carrito.agregarProducto("Cuaderno", 8_000, 0));
    }

    @Test
    @DisplayName("Rechaza cantidad negativa")
    void agregarProducto_cantidadNegativa_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> carrito.agregarProducto("Cuaderno", 8_000, -1));
    }

    

    @Test
    @DisplayName("Sin cupón no hay descuento")
    void calcularDescuento_sinCupon_retornaCero() {
        carrito.agregarProducto("Libro", 100_000, 1);
        assertEquals(0, carrito.calcularDescuento(), 0.001);
    }

    @Test
    @DisplayName("SENA10 descuenta 10 % del subtotal")
    void calcularDescuento_cuponSena10_retornaDiezPorCiento() {
        carrito.agregarProducto("Libro", 100_000, 1);
        carrito.aplicarCupon("SENA10");
        assertEquals(10_000, carrito.calcularDescuento(), 0.001);
    }

    @Test
    @DisplayName("Frontera: BIENVENIDO con subtotal exacto de 50.000 descuenta 5.000")
    void calcularDescuento_bienvenidoSubtotalExactoCincuentaMil_descuentaCincoMil() {
        carrito.agregarProducto("Kit", 50_000, 1);
        carrito.aplicarCupon("BIENVENIDO");
        assertEquals(5_000, carrito.calcularDescuento(), 0.001);
    }

    @Test
    @DisplayName("Frontera: BIENVENIDO con subtotal 49.999 no descuenta")
    void calcularDescuento_bienvenidoSubtotalBajoElMinimo_noDescuenta() {
        carrito.agregarProducto("Kit", 49_999, 1);
        carrito.aplicarCupon("BIENVENIDO");
        assertEquals(0, carrito.calcularDescuento(), 0.001);
    }

    @Test
    @DisplayName("Cupón inválido lanza excepción")
    void aplicarCupon_codigoDesconocido_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> carrito.aplicarCupon("GRATIS"));
    }

    @Test
    @DisplayName("Cupón nulo lanza excepción")
    void aplicarCupon_codigoNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> carrito.aplicarCupon(null));
    }

    @Test
    @DisplayName("Total con envío pagado: 100.000 × 1,19 + 12.000 = 131.000")
    void calcularTotal_bajoMinimoEnvioGratis_incluyeEnvio() {
        carrito.agregarProducto("Libro", 100_000, 1);
        assertEquals(131_000, carrito.calcularTotal(), 0.001);
    }

    @Test
    @DisplayName("Total con envío gratis: 500.000 × 1,19 = 595.000")
    void calcularTotal_sobreMinimoEnvioGratis_noIncluyeEnvio() {
        carrito.agregarProducto("Monitor", 500_000, 1);
        assertEquals(595_000, carrito.calcularTotal(), 0.001);
    }

    @Test
    @DisplayName("Frontera envío: con IVA 149.940 (< 150.000) paga envío → 161.940")
    void calcularTotal_justoBajoElMinimo_pagaEnvio() {
        carrito.agregarProducto("Pedido", 126_000, 1); // 126.000 × 1,19 = 149.940
        assertEquals(161_940, carrito.calcularTotal(), 0.001);
    }

    @Test
    @DisplayName("Frontera envío: con IVA 151.130 (≥ 150.000) envío gratis")
    void calcularTotal_justoSobreElMinimo_envioGratis() {
        carrito.agregarProducto("Pedido", 127_000, 1); // 127.000 × 1,19 = 151.130
        assertEquals(151_130, carrito.calcularTotal(), 0.001);
    }

    @Test
    @DisplayName("Total con SENA10 y envío gratis: (500.000 − 50.000) × 1,19 = 535.500")
    void calcularTotal_conSena10EnvioGratis_aplicaDescuentoAntesDeIva() {
        carrito.agregarProducto("Monitor", 500_000, 1);
        carrito.aplicarCupon("SENA10");
        assertEquals(535_500, carrito.calcularTotal(), 0.001);
    }

    @Test
    @DisplayName("Total con SENA10 y envío pagado: 90.000 × 1,19 + 12.000 = 119.100")
    void calcularTotal_conSena10EnvioPagado_aplicaDescuentoIvaYEnvio() {
        carrito.agregarProducto("Libro", 100_000, 1);
        carrito.aplicarCupon("SENA10");
        assertEquals(119_100, carrito.calcularTotal(), 0.001);
    }

    @Test
    @DisplayName("Total con BIENVENIDO: (60.000 − 5.000) × 1,19 + 12.000 = 77.450")
    void calcularTotal_conBienvenido_restaCincoMilAntesDeIva() {
        carrito.agregarProducto("Kit", 60_000, 1);
        carrito.aplicarCupon("BIENVENIDO");
        assertEquals(77_450, carrito.calcularTotal(), 0.001);
    }

    @Test
    @DisplayName("BIENVENIDO bajo el mínimo no descuenta: 40.000 × 1,19 + 12.000 = 59.600")
    void calcularTotal_bienvenidoBajoMinimo_noRestaDescuento() {
        carrito.agregarProducto("Kit", 40_000, 1);
        carrito.aplicarCupon("BIENVENIDO");
        assertEquals(59_600, carrito.calcularTotal(), 0.001);
    }
}
