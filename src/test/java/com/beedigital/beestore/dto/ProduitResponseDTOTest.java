
package com.beedigital.beestore.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProduitResponseDTOTest {

    @Test
    void constructeurEtGettersSetters() {
        ProduitResponseDTO dto = new ProduitResponseDTO();

        LocalDateTime date = LocalDateTime.now();

        dto.setId(1L);
        dto.setNom("Laptop");
        dto.setDescription("Ordinateur portable");
        dto.setPrix(1200.0);
        dto.setStock(10);
        dto.setCategorie("Informatique");
        dto.setActif(true);
        dto.setDateCreation(date);

        assertEquals(1L, dto.getId());
        assertEquals("Laptop", dto.getNom());
        assertEquals("Ordinateur portable", dto.getDescription());
        assertEquals(1200.0, dto.getPrix());
        assertEquals(10, dto.getStock());
        assertEquals("Informatique", dto.getCategorie());
        assertTrue(dto.getActif());
        assertEquals(date, dto.getDateCreation());
    }

    @Test
    void constructeurAvecTousLesParametres() {
        LocalDateTime date = LocalDateTime.now();

        ProduitResponseDTO dto = new ProduitResponseDTO(
                1L,
                "Laptop",
                "Ordinateur portable",
                1200.0,
                10,
                "Informatique",
                true,
                date
        );

        assertEquals(1L, dto.getId());
        assertEquals("Laptop", dto.getNom());
        assertEquals(date, dto.getDateCreation());
    }

    @Test
    void builder_doitConstruireDTO() {
        LocalDateTime date = LocalDateTime.now();

        ProduitResponseDTO dto = ProduitResponseDTO.builder()
                .id(1L)
                .nom("Laptop")
                .description("Ordinateur portable")
                .prix(1200.0)
                .stock(10)
                .categorie("Informatique")
                .actif(true)
                .dateCreation(date)
                .build();

        assertEquals(1L, dto.getId());
        assertEquals("Laptop", dto.getNom());
        assertEquals(1200.0, dto.getPrix());
        assertEquals(date, dto.getDateCreation());
    }

    @Test
    void equalsHashCodeEtToString() {
        ProduitResponseDTO dto1 = ProduitResponseDTO.builder()
                .id(1L)
                .nom("Laptop")
                .build();

        ProduitResponseDTO dto2 = ProduitResponseDTO.builder()
                .id(1L)
                .nom("Laptop")
                .build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertTrue(dto1.toString().contains("Laptop"));
    }
    
@Test
void equalsAvecObjetNull() {
    ProduitResponseDTO dto = ProduitResponseDTO.builder()
            .id(1L)
            .nom("Laptop")
            .build();

    assertNotEquals(dto, null);
}

@Test
void equalsAvecAutreType() {
    ProduitResponseDTO dto = ProduitResponseDTO.builder()
            .id(1L)
            .nom("Laptop")
            .build();

    assertNotEquals(dto, "Laptop");
}

@Test
void equalsAvecValeursDifferentes() {
    ProduitResponseDTO dto1 = ProduitResponseDTO.builder()
            .id(1L)
            .nom("Laptop")
            .build();

    ProduitResponseDTO dto2 = ProduitResponseDTO.builder()
            .id(2L)
            .nom("Laptop")
            .build();

    assertNotEquals(dto1, dto2);
}

@Test
void equalsAvecValeursNulles() {
    ProduitResponseDTO dto1 = new ProduitResponseDTO();
    ProduitResponseDTO dto2 = new ProduitResponseDTO();

    assertEquals(dto1, dto2);
}

@Test
void builderSansValeurs() {
    ProduitResponseDTO dto = ProduitResponseDTO.builder().build();

    assertNotNull(dto);
    assertNull(dto.getId());
    assertNull(dto.getNom());
}

@Test
void builderAvecValeursNulles() {
    ProduitResponseDTO dto = ProduitResponseDTO.builder()
            .id(null)
            .nom(null)
            .description(null)
            .prix(null)
            .stock(null)
            .categorie(null)
            .actif(null)
            .dateCreation(null)
            .build();

    assertNull(dto.getId());
    assertNull(dto.getNom());
    assertNull(dto.getDateCreation());
}

@Test
void hashCodeAvecValeursIdentiques() {
    ProduitResponseDTO dto1 = ProduitResponseDTO.builder()
            .id(1L)
            .nom("Laptop")
            .build();

    ProduitResponseDTO dto2 = ProduitResponseDTO.builder()
            .id(1L)
            .nom("Laptop")
            .build();

    assertEquals(dto1.hashCode(), dto2.hashCode());
}

}
