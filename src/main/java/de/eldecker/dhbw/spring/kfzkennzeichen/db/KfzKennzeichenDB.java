package de.eldecker.dhbw.spring.kfzkennzeichen.db;

import java.util.Optional;

import org.springframework.stereotype.Component;

import de.eldecker.dhbw.spring.kfzkennzeichen.model.Unterscheidungszeichen;

/**
 * Diese Klasse ist mit der Spring-Annotation {@code Annotation} versehen und
 * ist daher eine "Bean", die in anderen Bean-Klasse per Autowiring geholt
 * werden kann. 
 */
@Component
public class KfzKennzeichenDB {

    /**
     * 
     * @param kurzelNormalized Unterscheidungs, das gesucht werden soll, z.B. "BAD" (Baden-Baden);
     *                         muss bereits normalisiert sein, d.h. nur Großbuchstaben und
     *                         keine Leerzeichen am Anfang/Ende  
     * @return
     */
    public Optional<Unterscheidungszeichen> sucheUnterscheidungszeichen(String kurzelNormalized) {
        
        
        return Optional.empty();
    }
    
}
