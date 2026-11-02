
package com.beedigital.beestore.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProduitRequestDTOTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        factory.close();
    }

    @Test
    void creerDTO_valeursValides_aucuneErreur() {
        ProduitRequestDTO dto = new ProduitRequestDTO();

        dto.setNom("Laptop");
        dto.setDescription("Ordinateur portable");
        dto.setPrix(1200.0);
        dto.setStock(10);
        dto.setCategorie("Informatique");

        assertTrue(validator.validate(dto).isEmpty());
        assertEquals("Laptop", dto.getNom());
        assertEquals(1200.0, dto.getPrix());
        assertEquals(10, dto.getStock());
        assertTrue(dto.getActif());
    }

    @Test
    void creerDTO_nomVide_erreurValidation() {
        ProduitRequestDTO dto = creerDTOValide();
        dto.setNom("");

        assertFalse(validator.validate(dto).isEmpty());
    }

    @Test
    void creerDTO_nomTropCourt_erreurValidation() {
        ProduitRequestDTO dto = creerDTOValide();
        dto.setNom("A");

        assertFalse(validator.validate(dto).isEmpty());
    }

    @Test
    void creerDTO_prixNegatif_erreurValidation() {
        ProduitRequestDTO dto = creerDTOValide();
        dto.setPrix(-10.0);

        assertFalse(validator.validate(dto).isEmpty());
    }

    @Test
    void creerDTO_stockNegatif_erreurValidation() {
        ProduitRequestDTO dto = creerDTOValide();
        dto.setStock(-1);

        assertFalse(validator.validate(dto).isEmpty());
    }

    @Test
    void creerDTO_prixNull_erreurValidation() {
        ProduitRequestDTO dto = creerDTOValide();
        dto.setPrix(null);

        assertFalse(validator.validate(dto).isEmpty());
    }

    @Test
    void creerDTO_stockNull_erreurValidation() {
        ProduitRequestDTO dto = creerDTOValide();
        dto.setStock(null);

        assertFalse(validator.validate(dto).isEmpty());
    }

    private ProduitRequestDTO creerDTOValide() {
        ProduitRequestDTO dto = new ProduitRequestDTO();
        dto.setNom("Laptop");
        dto.setPrix(1200.0);
        dto.setStock(10);
        return dto;
    }
    
@Test
void creerDTO_toutesLesProprietes() {
    ProduitRequestDTO dto = new ProduitRequestDTO();

    dto.setNom("Laptop");
    dto.setDescription("Ordinateur portable");
    dto.setPrix(1200.0);
    dto.setStock(10);
    dto.setCategorie("Informatique");
    dto.setActif(false);

    assertEquals("Laptop", dto.getNom());
    assertEquals("Ordinateur portable", dto.getDescription());
    assertEquals(1200.0, dto.getPrix());
    assertEquals(10, dto.getStock());
    assertEquals("Informatique", dto.getCategorie());
    assertFalse(dto.getActif());

    assertNotNull(dto.toString());
}

@Test
void creerDTO_nomTropLong_erreurValidation() {
    ProduitRequestDTO dto = creerDTOValide();
    dto.setNom("A".repeat(101));

    assertFalse(validator.validate(dto).isEmpty());
}

@Test
void creerDTO_descriptionTropLongue_erreurValidation() {
    ProduitRequestDTO dto = creerDTOValide();
    dto.setDescription("A".repeat(501));

    assertFalse(validator.validate(dto).isEmpty());
}

@Test
void creerDTO_categorieTropLongue_erreurValidation() {
    ProduitRequestDTO dto = creerDTOValide();
    dto.setCategorie("A".repeat(101));

    assertFalse(validator.validate(dto).isEmpty());
}

@Test
void creerDTO_nomLongueurMinimale_valide() {
    ProduitRequestDTO dto = creerDTOValide();
    dto.setNom("PC");

    assertTrue(validator.validate(dto).isEmpty());
}

@Test
void creerDTO_nomLongueurMaximale_valide() {
    ProduitRequestDTO dto = creerDTOValide();
    dto.setNom("A".repeat(100));

    assertTrue(validator.validate(dto).isEmpty());
}

@Test
void creerDTO_stockZero_valide() {
    ProduitRequestDTO dto = creerDTOValide();
    dto.setStock(0);

    assertTrue(validator.validate(dto).isEmpty());
}

@Test
void creerDTO_equalsHashCode() {
    ProduitRequestDTO dto1 = creerDTOValide();
    ProduitRequestDTO dto2 = creerDTOValide();

    assertEquals(dto1, dto2);
    assertEquals(dto1.hashCode(), dto2.hashCode());

    dto2.setNom("Autre produit");

    assertNotEquals(dto1, dto2);
}

@Test
void equalsAvecObjetNull() {
    ProduitRequestDTO dto = creerDTOValide();

    assertNotEquals(dto, null);
}

@Test
void equalsAvecAutreType() {
    ProduitRequestDTO dto = creerDTOValide();

    assertNotEquals(dto, "Laptop");
}

@Test
void equalsAvecValeursDifferentes() {
    ProduitRequestDTO dto1 = creerDTOValide();
    ProduitRequestDTO dto2 = creerDTOValide();

    dto2.setPrix(1500.0);

    assertNotEquals(dto1, dto2);
}

@Test
void equalsAvecValeursNulles() {
    ProduitRequestDTO dto1 = new ProduitRequestDTO();
    ProduitRequestDTO dto2 = new ProduitRequestDTO();

    assertEquals(dto1, dto2);
}

@Test
void hashCodeAvecValeursIdentiques() {
    ProduitRequestDTO dto1 = creerDTOValide();
    ProduitRequestDTO dto2 = creerDTOValide();

    assertEquals(dto1.hashCode(), dto2.hashCode());
}

@Test
void toStringContientLesInformations() {
    ProduitRequestDTO dto = creerDTOValide();

    assertTrue(dto.toString().contains("Laptop"));
}


}
