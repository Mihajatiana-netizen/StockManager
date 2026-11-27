
package com.stockmanager.service;

import com.stockmanager.model.Movement;
import com.stockmanager.repository.MovementRepository;

import java.sql.SQLException;
import java.util.List;

public class MovementService {

    private final MovementRepository movementRepository;

    public MovementService() {
        this.movementRepository = new MovementRepository();
    }

    // Enregistrer une entrée ou une sortie
    public void addMovement(Movement movement) throws SQLException {

        validateMovement(movement);

        if (movement.getDescription() != null) {
            movement.setDescription(
                    movement.getDescription().trim()
            );

            if (movement.getDescription().isEmpty()) {
                movement.setDescription(null);
            }
        }

        movementRepository.save(movement);
    }

    // Récupérer tous les mouvements
    public List<Movement> getAllMovements() throws SQLException {
        return movementRepository.findAll();
    }

    // Vérifier les données du mouvement
    private void validateMovement(Movement movement) {

        if (movement == null) {
            throw new IllegalArgumentException(
                    "Le mouvement ne peut pas être vide."
            );
        }

        if (movement.getArticleId() <= 0) {
            throw new IllegalArgumentException(
                    "Veuillez sélectionner un article valide."
            );
        }

        if (movement.getQuantity() <= 0) {
            throw new IllegalArgumentException(
                    "La quantité doit être supérieure à zéro."
            );
        }

        if (!"ENTRY".equals(movement.getType())
                && !"EXIT".equals(movement.getType())) {

            throw new IllegalArgumentException(
                    "Le type de mouvement doit être ENTRY ou EXIT."
            );
        }
    }

    public List<Movement> getMovementsByFilters(Integer articleId, String type)
            throws SQLException {

        return movementRepository.findByFilters(articleId, type);
    }

}
