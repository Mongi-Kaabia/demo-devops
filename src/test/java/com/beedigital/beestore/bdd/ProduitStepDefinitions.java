package com.beedigital.beestore.bdd;

import com.beedigital.beestore.dto.ProduitRequestDTO;
import com.beedigital.beestore.dto.ProduitResponseDTO;
import com.beedigital.beestore.service.ProduitService;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.assertj.core.api.Assertions.assertThat;
@Component
public class ProduitStepDefinitions {

    @Autowired
    private ProduitService produitService;

    private ProduitRequestDTO requeteEnCours;
    private ProduitResponseDTO produitCree;

    @Given("a product named {string} priced at {double} with a stock of {int}")
    public void aProductWithTheseCharacteristics(
            String nom, Double prix, Integer stock) {

        requeteEnCours = new ProduitRequestDTO();
        requeteEnCours.setNom(nom);
        requeteEnCours.setPrix(prix);
        requeteEnCours.setStock(stock);
    }

    @When("I submit the product creation request")
    public void iSubmitTheProductCreationRequest() {
        produitCree = produitService.creerProduit(requeteEnCours);
    }

    @Then("the product is created successfully")
    public void theProductIsCreatedSuccessfully() {
        assertThat(produitCree.getId()).isNotNull();
    }

    @Then("the created product has the name {string}")
    public void theCreatedProductHasTheName(String expectedName) {
        assertThat(produitCree.getNom()).isEqualTo(expectedName);
    }
}