
package com.beedigital.beestore.service;

import com.beedigital.beestore.dto.ProduitRequestDTO;
import com.beedigital.beestore.dto.ProduitResponseDTO;
import com.beedigital.beestore.entity.Produit;
import com.beedigital.beestore.exception.ProduitNotFoundException;
import com.beedigital.beestore.repository.ProduitRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProduitServiceImplTest {

    @Mock
    private ProduitRepository produitRepository;

    @InjectMocks
    private ProduitServiceImpl produitService;

    // CREATION

    @Test
    @DisplayName("Créer un produit avec actif null : actif doit être true par défaut")
    void creerProduit_actifNull_doitEtreActifParDefaut() {
        ProduitRequestDTO dto = new ProduitRequestDTO();
        dto.setNom("Laptop");
        dto.setDescription("Ordinateur portable");
        dto.setPrix(1299.99);
        dto.setStock(10);
        dto.setCategorie("Informatique");

        when(produitRepository.save(any(Produit.class))).thenAnswer(invocation -> {
            Produit produit = invocation.getArgument(0);
            produit.setId(1L);
            return produit;
        });

        ProduitResponseDTO resultat = produitService.creerProduit(dto);

        assertThat(resultat.getId()).isEqualTo(1L);
        assertThat(resultat.getNom()).isEqualTo("Laptop");
        assertThat(resultat.getActif()).isTrue();

        verify(produitRepository).save(any(Produit.class));
    }

    @Test
    @DisplayName("Créer un produit en conservant actif à false")
    void creerProduit_actifFalse_doitConserverFalse() {
        ProduitRequestDTO dto = new ProduitRequestDTO();
        dto.setNom("Clavier");
        dto.setActif(false);

        when(produitRepository.save(any(Produit.class))).thenAnswer(invocation -> {
            Produit produit = invocation.getArgument(0);
            produit.setId(2L);
            return produit;
        });

        ProduitResponseDTO resultat = produitService.creerProduit(dto);

        assertThat(resultat.getActif()).isFalse();
    }

    // LECTURE

    @Test
    @DisplayName("Récupérer tous les produits")
    void getTousProduits_doitRetournerListeDeDTO() {
        when(produitRepository.findAll()).thenReturn(List.of(
                Produit.builder().id(1L).nom("Laptop").build(),
                Produit.builder().id(2L).nom("Souris").build()
        ));

        List<ProduitResponseDTO> resultat = produitService.getTousProduits();

        assertThat(resultat).hasSize(2);
        assertThat(resultat).extracting(ProduitResponseDTO::getNom)
                .containsExactly("Laptop", "Souris");
    }

    @Test
    @DisplayName("Retourner une liste vide si aucun produit")
    void getTousProduits_aucunProduit_doitRetournerListeVide() {
        when(produitRepository.findAll()).thenReturn(List.of());

        List<ProduitResponseDTO> resultat = produitService.getTousProduits();

        assertThat(resultat).isEmpty();
    }

    @Test
    @DisplayName("Récupérer un produit existant par son ID")
    void getProduitById_produitExistant_doitRetournerLeProduit() {
        Produit produit = Produit.builder()
                .id(1L)
                .nom("Laptop")
                .build();

        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));

        ProduitResponseDTO resultat = produitService.getProduitById(1L);

        assertThat(resultat.getId()).isEqualTo(1L);
        assertThat(resultat.getNom()).isEqualTo("Laptop");
    }

    @Test
    @DisplayName("Lever une exception si le produit est absent")
    void getProduitById_produitAbsent_doitLeverException() {
        when(produitRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> produitService.getProduitById(99L))
                .isInstanceOf(ProduitNotFoundException.class)
                .hasMessageContaining("99");
    }

    // MODIFICATION

    @Test
    @DisplayName("Modifier un produit existant")
    void modifierProduit_produitExistant_doitModifierLeProduit() {
        Produit produit = Produit.builder()
                .id(1L)
                .nom("Ancien nom")
                .actif(false)
                .build();

        ProduitRequestDTO dto = new ProduitRequestDTO();
        dto.setNom("Nouveau nom");
        dto.setDescription("Nouvelle description");
        dto.setPrix(500.0);
        dto.setStock(20);
        dto.setCategorie("Informatique");
        dto.setActif(true);

        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));
        when(produitRepository.save(any(Produit.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ProduitResponseDTO resultat = produitService.modifierProduit(1L, dto);

        assertThat(resultat.getNom()).isEqualTo("Nouveau nom");
        assertThat(resultat.getDescription()).isEqualTo("Nouvelle description");
        assertThat(resultat.getPrix()).isEqualTo(500.0);
        assertThat(resultat.getStock()).isEqualTo(20);
        assertThat(resultat.getCategorie()).isEqualTo("Informatique");
        assertThat(resultat.getActif()).isTrue();

        verify(produitRepository).save(produit);
    }

    @Test
    @DisplayName("Conserver la valeur de actif si elle est null lors de la modification")
    void modifierProduit_actifNull_doitConserverAncienneValeur() {
        Produit produit = Produit.builder()
                .id(1L)
                .nom("Laptop")
                .actif(false)
                .build();

        ProduitRequestDTO dto = new ProduitRequestDTO();
        dto.setNom("Laptop modifié");
        dto.setActif(null);

        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));
        when(produitRepository.save(any(Produit.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ProduitResponseDTO resultat = produitService.modifierProduit(1L, dto);

        assertThat(resultat.getActif()).isFalse();
    }

    @Test
    @DisplayName("Lever une exception si le produit à modifier est absent")
    void modifierProduit_produitAbsent_doitLeverException() {
        ProduitRequestDTO dto = new ProduitRequestDTO();

        when(produitRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> produitService.modifierProduit(99L, dto))
                .isInstanceOf(ProduitNotFoundException.class)
                .hasMessageContaining("99");

        verify(produitRepository, never()).save(any(Produit.class));
    }

    // SUPPRESSION

    @Test
    @DisplayName("Supprimer un produit existant")
    void supprimerProduit_produitExistant_doitSupprimerLeProduit() {
        when(produitRepository.existsById(1L)).thenReturn(true);

        produitService.supprimerProduit(1L);

        verify(produitRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Lever une exception si le produit à supprimer est absent")
    void supprimerProduit_produitAbsent_doitLeverException() {
        when(produitRepository.existsById(42L)).thenReturn(false);

        assertThatThrownBy(() -> produitService.supprimerProduit(42L))
                .isInstanceOf(ProduitNotFoundException.class);

        verify(produitRepository, never()).deleteById(anyLong());
    }

    // RECHERCHE PAR CATEGORIE

    @Test
    @DisplayName("Rechercher les produits par catégorie")
    void rechercherParCategorie_doitRetournerLesProduits() {
        when(produitRepository.findByCategorieIgnoreCase("Informatique"))
                .thenReturn(List.of(
                        Produit.builder().id(1L).nom("Laptop").build(),
                        Produit.builder().id(2L).nom("Souris").build()
                ));

        List<ProduitResponseDTO> resultat =
                produitService.rechercherParCategorie("Informatique");

        assertThat(resultat).hasSize(2);
    }

    @Test
    @DisplayName("Retourner une liste vide si la catégorie ne contient aucun produit")
    void rechercherParCategorie_aucunResultat_doitRetournerListeVide() {
        when(produitRepository.findByCategorieIgnoreCase("Inconnue"))
                .thenReturn(List.of());

        List<ProduitResponseDTO> resultat =
                produitService.rechercherParCategorie("Inconnue");

        assertThat(resultat).isEmpty();
    }

    // RECHERCHE PAR NOM

    @Test
    @DisplayName("Rechercher les produits par nom")
    void rechercherParNom_doitRetournerLesProduits() {
        when(produitRepository.findByNomContainingIgnoreCase("Lap"))
                .thenReturn(List.of(
                        Produit.builder().id(1L).nom("Laptop").build()
                ));

        List<ProduitResponseDTO> resultat =
                produitService.rechercherParNom("Lap");

        assertThat(resultat).hasSize(1);
        assertThat(resultat.get(0).getNom()).isEqualTo("Laptop");
    }

    @Test
    @DisplayName("Retourner une liste vide si aucun nom ne correspond")
    void rechercherParNom_aucunResultat_doitRetournerListeVide() {
        when(produitRepository.findByNomContainingIgnoreCase("Inconnu"))
                .thenReturn(List.of());

        List<ProduitResponseDTO> resultat =
                produitService.rechercherParNom("Inconnu");

        assertThat(resultat).isEmpty();
    }

    // RUPTURE DE STOCK

    @Test
    @DisplayName("Récupérer les produits en rupture de stock")
    void getProduitsEnRuptureDeStock_doitFiltrerParStock() {
        when(produitRepository.findByStock(0)).thenReturn(List.of(
                Produit.builder().id(1L).nom("Souris").stock(0).build()
        ));

        List<ProduitResponseDTO> resultat =
                produitService.getProduitsEnRuptureDeStock();

        assertThat(resultat).hasSize(1);
        assertThat(resultat.get(0).getStock()).isZero();
    }

    @Test
    @DisplayName("Retourner une liste vide si aucun produit en rupture de stock")
    void getProduitsEnRuptureDeStock_aucunProduit_doitRetournerListeVide() {
        when(produitRepository.findByStock(0)).thenReturn(List.of());

        List<ProduitResponseDTO> resultat =
                produitService.getProduitsEnRuptureDeStock();

        assertThat(resultat).isEmpty();
    }
}
