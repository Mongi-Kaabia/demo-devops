
package com.beedigital.beestore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ProduitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // CREATE

    @Test
    @DisplayName("POST : créer un produit valide")
    void creerProduit_donneesValides_doitRetourner201() throws Exception {
        String json = """
                {
                  "nom": "Clavier mecanique",
                  "prix": 89.90,
                  "stock": 25,
                  "categorie": "Informatique"
                }""";

        mockMvc.perform(post("/api/v1/produits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nom").value("Clavier mecanique"));
    }

    @Test
    @DisplayName("POST : nom vide doit retourner 400")
    void creerProduit_nomVide_doitRetourner400() throws Exception {
        String json = """
                { "nom": "", "prix": 10.0, "stock": 1 }
                """;

        mockMvc.perform(post("/api/v1/produits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.nom").exists());
    }

    @Test
    @DisplayName("POST : prix négatif doit retourner 400")
    void creerProduit_prixNegatif_doitRetourner400() throws Exception {
        String json = """
                { "nom": "Clavier", "prix": -10.0, "stock": 1 }
                """;

        mockMvc.perform(post("/api/v1/produits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    // READ

    @Test
    @DisplayName("GET : récupérer tous les produits")
    void getTousProduits_doitRetourner200() throws Exception {
        creerProduitPourTest("Laptop", 1200.0, 5, "Informatique");
        creerProduitPourTest("Souris", 25.0, 10, "Accessoires");

        mockMvc.perform(get("/api/v1/produits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("GET : retourner une liste vide")
    void getTousProduits_aucunProduit_doitRetournerListeVide()
            throws Exception {

        mockMvc.perform(get("/api/v1/produits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("GET : récupérer un produit avec un ID inconnu")
    void getProduitById_idInconnu_doitRetourner404() throws Exception {
        mockMvc.perform(get("/api/v1/produits/{id}", 999999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    // UPDATE

    @Test
    @DisplayName("PUT : modifier un produit avec succès")
    void modifierProduit_donneesValides_doitRetourner200()
            throws Exception {

        Long id = creerProduitPourTest(
                "Ancien nom", 100.0, 10, "Informatique");

        String modification = """
                {
                  "nom": "Nouveau nom",
                  "prix": 150.0,
                  "stock": 20,
                  "categorie": "Informatique"
                }
                """;

        mockMvc.perform(put("/api/v1/produits/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(modification))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Nouveau nom"))
                .andExpect(jsonPath("$.prix").value(150.0));
    }

    @Test
    @DisplayName("PUT : modifier un produit inexistant doit retourner 404")
    void modifierProduit_idInconnu_doitRetourner404()
            throws Exception {

        String modification = """
                {
                  "nom": "Nouveau nom",
                  "prix": 150.0,
                  "stock": 20
                }
                """;

        mockMvc.perform(put("/api/v1/produits/{id}", 999999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(modification))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT : nom vide doit retourner 400")
    void modifierProduit_nomVide_doitRetourner400()
            throws Exception {

        String modification = """
                {
                  "nom": "",
                  "prix": 150.0,
                  "stock": 20
                }
                """;

        mockMvc.perform(put("/api/v1/produits/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(modification))
                .andExpect(status().isBadRequest());
    }

    // DELETE

    @Test
    @DisplayName("DELETE : supprimer un produit inexistant")
    void supprimerProduit_idInconnu_doitRetourner404()
            throws Exception {

        mockMvc.perform(delete("/api/v1/produits/{id}", 999999))
                .andExpect(status().isNotFound());
    }

    // SEARCH

    @Test
    @DisplayName("SEARCH : rechercher par catégorie")
    void rechercher_parCategorie_doitRetournerResultats()
            throws Exception {

        creerProduitPourTest("Laptop", 1200.0, 5, "Informatique");
        creerProduitPourTest("Souris", 25.0, 10, "Accessoires");

        mockMvc.perform(get("/api/v1/produits/search")
                        .param("categorie", "Informatique"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nom").value("Laptop"));
    }

    @Test
    @DisplayName("SEARCH : rechercher par nom")
    void rechercher_parNom_doitRetournerResultats()
            throws Exception {

        creerProduitPourTest("Laptop Pro", 1200.0, 5, "Informatique");
        creerProduitPourTest("Souris", 25.0, 10, "Accessoires");

        mockMvc.perform(get("/api/v1/produits/search")
                        .param("nom", "Laptop"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nom").value("Laptop Pro"));
    }

    @Test
    @DisplayName("SEARCH : sans paramètre retourner tous les produits")
    void rechercher_sansParametre_doitRetournerTousLesProduits()
            throws Exception {

        creerProduitPourTest("Laptop", 1200.0, 5, "Informatique");
        creerProduitPourTest("Souris", 25.0, 10, "Accessoires");

        mockMvc.perform(get("/api/v1/produits/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // CYCLE CRUD COMPLET

    @Test
    @DisplayName("Cycle complet : créer, lire, modifier et supprimer")
    void cycleCompletCRUD() throws Exception {

        String creation = """
                { "nom": "Souris", "prix": 19.90, "stock": 50 }
                """;

        String reponse = mockMvc.perform(post("/api/v1/produits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(creation))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(reponse).get("id").asLong();

        mockMvc.perform(get("/api/v1/produits/{id}", id))
                .andExpect(status().isOk());

        String modification = """
                { "nom": "Souris sans fil", "prix": 24.90, "stock": 40 }
                """;

        mockMvc.perform(put("/api/v1/produits/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(modification))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Souris sans fil"));

        mockMvc.perform(delete("/api/v1/produits/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/produits/{id}", id))
                .andExpect(status().isNotFound());
    }

    // MÉTHODE UTILITAIRE

    private Long creerProduitPourTest(
            String nom, double prix, int stock, String categorie)
            throws Exception {

        String json = objectMapper.writeValueAsString(
                creerDTO(nom, prix, stock, categorie));

        String reponse = mockMvc.perform(post("/api/v1/produits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(reponse).get("id").asLong();
    }

    private com.beedigital.beestore.dto.ProduitRequestDTO creerDTO(
            String nom, double prix, int stock, String categorie) {

        com.beedigital.beestore.dto.ProduitRequestDTO dto =
                new com.beedigital.beestore.dto.ProduitRequestDTO();

        dto.setNom(nom);
        dto.setPrix(prix);
        dto.setStock(stock);
        dto.setCategorie(categorie);

        return dto;
    }
}
