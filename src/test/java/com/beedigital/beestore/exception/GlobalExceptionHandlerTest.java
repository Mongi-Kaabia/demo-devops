
package com.beedigital.beestore.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    // TEST 1 : Erreur de validation (400)

    @Test
    @DisplayName("Validation : retourner 400 avec les détails des erreurs")
    void handleValidation_doitRetourner400() {

        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(new Object(), "produit");

        bindingResult.addError(
                new FieldError(
                        "produit",
                        "nom",
                        "",
                        false,
                        null,
                        null,
                        "Le nom du produit est obligatoire"
                )
        );

        MethodArgumentNotValidException exception =
                mock(MethodArgumentNotValidException.class);

        when(exception.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<?> response =
                exceptionHandler.handleValidation(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        GlobalExceptionHandler.ErrorResponse body =
                (GlobalExceptionHandler.ErrorResponse) response.getBody();

        assertEquals(400, body.status());
        assertEquals("Données invalides", body.message());
        assertNotNull(body.timestamp());

        @SuppressWarnings("unchecked")
        Map<String, String> details =
                (Map<String, String>) body.details();

        assertEquals(
                "Le nom du produit est obligatoire",
                details.get("nom")
        );
    }

    // TEST 2 : Produit introuvable (404)

    @Test
    @DisplayName("Not Found : retourner 404")
    void handleNotFound_doitRetourner404() {

        ProduitNotFoundException exception =
                new ProduitNotFoundException("Produit introuvable");

        ResponseEntity<?> response =
                exceptionHandler.handleNotFound(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        GlobalExceptionHandler.ErrorResponse body =
                (GlobalExceptionHandler.ErrorResponse) response.getBody();

        assertNotNull(body);
        assertEquals(404, body.status());
        assertEquals("Produit introuvable", body.message());
        assertNotNull(body.timestamp());
        assertNull(body.details());
    }

    // TEST 3 : Erreur générique (500)

    @Test
    @DisplayName("Erreur générique : retourner 500")
    void handleGeneric_doitRetourner500() {

        Exception exception = new RuntimeException("Erreur technique");

        ResponseEntity<?> response =
                exceptionHandler.handleGeneric(exception);

        assertEquals(
                HttpStatus.INTERNAL_SERVER_ERROR,
                response.getStatusCode()
        );

        GlobalExceptionHandler.ErrorResponse body =
                (GlobalExceptionHandler.ErrorResponse) response.getBody();

        assertNotNull(body);
        assertEquals(500, body.status());
        assertEquals(
                "Une erreur interne est survenue. Contactez l'administrateur.",
                body.message()
        );
        assertNotNull(body.timestamp());
        assertNull(body.details());
    }
}
