package com.project;

import com.project.domain.Biblioteca;
import com.project.domain.Exemplar;
import com.project.domain.Llibre;
import com.project.domain.Prestec;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class testExemplar {

    @Test
    void testExemplarBuit() {
        Exemplar exemplar = new Exemplar();

        assertNull(exemplar.getExemplarId(), "L'id hauria de ser null");
        assertNull(exemplar.getCodiBarres(), "El codi de barres hauria de ser null");
        assertNull(exemplar.getLlibre(), "El llibre hauria de ser null");
        assertNull(exemplar.getBiblioteca(), "La biblioteca hauria de ser null");
        assertNotNull(exemplar.getHistorialPrestecs(), "La col·lecció de préstecs no hauria de ser null");
        assertTrue(exemplar.getHistorialPrestecs().isEmpty(), "La col·lecció de préstecs hauria d'estar buida");
        assertFalse(exemplar.isDisponible(), "Per defecte, no hi ha valor, podria ser false");
    }

    @Test
    void testExemplarConstructor() {
        Llibre llibre = new Llibre("ISBN-001", "Títol Test", "Editorial Test", 2023);
        Biblioteca biblioteca = new Biblioteca("Biblioteca Test", "Ciutat Test", "Adreça Test", "900000000", "test@biblioteca.cat");

        Exemplar exemplar = new Exemplar("CB-123", llibre, biblioteca);

        assertEquals("CB-123", exemplar.getCodiBarres());
        assertEquals(llibre, exemplar.getLlibre());
        assertEquals(biblioteca, exemplar.getBiblioteca());
        assertTrue(exemplar.isDisponible(), "Els exemplars nous han de ser disponibles per defecte");
        assertNotNull(exemplar.getHistorialPrestecs());
        assertTrue(exemplar.getHistorialPrestecs().isEmpty());
    }

    @Test
    void testSettersGetters() {
        Exemplar exemplar = new Exemplar();
        Llibre llibre = new Llibre("ISBN-002", "Alt Títol", "Alt Editorial", 2022);
        Biblioteca biblioteca = new Biblioteca("Biblioteca 2", "Ciutat 2", "Adreça 2", "911111111", "info2@biblioteca.cat");

        exemplar.setCodiBarres("CB-456");
        exemplar.setDisponible(false);
        exemplar.setLlibre(llibre);
        exemplar.setBiblioteca(biblioteca);

        assertEquals("CB-456", exemplar.getCodiBarres());
        assertFalse(exemplar.isDisponible());
        assertEquals(llibre, exemplar.getLlibre());
        assertEquals(biblioteca, exemplar.getBiblioteca());
    }

    @Test
    void testAfegirPrestec() {
        Exemplar exemplar = new Exemplar();
        Prestec prestec = new Prestec();
        prestec.setExemplar(exemplar);

        exemplar.getHistorialPrestecs().add(prestec);

        assertTrue(exemplar.getHistorialPrestecs().contains(prestec), "La col·lecció de préstecs ha de contenir el nou préstec");
        assertEquals(exemplar, prestec.getExemplar(), "El préstec ha de tenir assignat l'exemplar correcte");
    }

    @Test
    void testToString() {
        Llibre llibre = new Llibre("ISBN-003", "Títol ToString", "Editorial TS", 2021);
        Biblioteca biblioteca = new Biblioteca("Biblioteca TS", "Ciutat TS", "Adreça TS", "922222222", "ts@biblioteca.cat");

        Exemplar exemplar = new Exemplar("CB-789", llibre, biblioteca);
        String str = exemplar.toString();

        assertTrue(str.contains("CB-789"), "El toString hauria de contenir el codi de barres");
        assertTrue(str.contains("Títol ToString"), "El toString hauria de contenir el títol del llibre");
    }
}
