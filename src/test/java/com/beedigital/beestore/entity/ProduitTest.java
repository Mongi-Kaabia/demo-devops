
package com.beedigital.beestore.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProduitTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        Produit produit = new Produit();

        produit.setId(1L);
        produit.setNom("Ordinateur");
        produit.setDescription("PC portable");
        produit.setPrix(2500.0);
        produit.setStock(10);
        produit.setCategorie("Informatique");
        produit.setActif(true);

        assertEquals(1L, produit.getId());
        assertEquals("Ordinateur", produit.getNom());
        assertEquals("PC portable", produit.getDescription());
        assertEquals(2500.0, produit.getPrix());
        assertEquals(10, produit.getStock());
        assertEquals("Informatique", produit.getCategorie());
        assertTrue(produit.getActif());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime date = LocalDateTime.now();

        Produit produit = new Produit(
                1L,
                "Clavier",
                "Clavier mécanique",
                150.0,
                20,
                "Informatique",
                true,
                date
        );

        assertEquals(1L, produit.getId());
        assertEquals("Clavier", produit.getNom());
        assertEquals(date, produit.getDateCreation());
    }

    @Test
    void testBuilder() {
        Produit produit = Produit.builder()
                .id(1L)
                .nom("Souris")
                .description("Souris sans fil")
                .prix(80.0)
                .stock(15)
                .categorie("Informatique")
                .build();

        assertEquals("Souris", produit.getNom());
        assertEquals(80.0, produit.getPrix());
        assertEquals(true, produit.getActif());
    }

    @Test
    void testBuilderWithActifFalse() {
        Produit produit = Produit.builder()
                .nom("Écran")
                .prix(600.0)
                .stock(5)
                .actif(false)
                .build();

        assertFalse(produit.getActif());
    }

    @Test
    void testOnCreate() {
        Produit produit = new Produit();
        produit.onCreate();

        assertNotNull(produit.getDateCreation());
        assertTrue(produit.getActif());
    }

    @Test
    void testOnCreateWithActifNull() {
        Produit produit = new Produit();
        produit.setActif(null);

        produit.onCreate();

        assertNotNull(produit.getDateCreation());
        assertTrue(produit.getActif());
    }

    @Test
    void testOnCreateWithActifFalse() {
        Produit produit = new Produit();
        produit.setActif(false);

        produit.onCreate();

        assertNotNull(produit.getDateCreation());
        assertFalse(produit.getActif());
    }

    @Test
    void testEqualsAndHashCode() {
        Produit produit1 = Produit.builder()
                .id(1L)
                .nom("Clavier")
                .prix(150.0)
                .stock(10)
                .build();

        Produit produit2 = Produit.builder()
                .id(1L)
                .nom("Clavier")
                .prix(150.0)
                .stock(10)
                .build();

        assertEquals(produit1, produit2);
        assertEquals(produit1.hashCode(), produit2.hashCode());
    }

    @Test
    void testToString() {
        Produit produit = Produit.builder()
                .id(1L)
                .nom("Clavier")
                .prix(150.0)
                .stock(10)
                .build();

        String result = produit.toString();

        assertTrue(result.contains("Clavier"));
        assertTrue(result.contains("150.0"));
    }
}
