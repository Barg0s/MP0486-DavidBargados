package com.project;

import com.project.dao.Manager;
import com.project.domain.Autor;
import com.project.domain.Exemplar;
import com.project.domain.Llibre;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class testLlibre {

    @Test
    void testLlibreBuit() {
        Llibre llibre = new Llibre();

        assertNull(llibre.getLlibreId());
        assertNull(llibre.getIsbn());
        assertNull(llibre.getTitol());
        assertNull(llibre.getEditorial());
        assertNull(llibre.getAnyPublicacio());

        assertNotNull(llibre.getAutors());
        assertNotNull(llibre.getExemplars());
        assertTrue(llibre.getAutors().isEmpty(), "La col·lecció d'autors hauria d'estar buida");
        assertTrue(llibre.getExemplars().isEmpty(), "La col·lecció d'exemplars hauria d'estar buida");
    }

    @Test
    void testLlibreConstructor() {
        Llibre llibre = new Llibre("ISBN-333", "El corredor del laberinto", "Nocturna-Ediciones", 2009);

        assertEquals("ISBN-333", llibre.getIsbn());
        assertEquals("El corredor del laberinto", llibre.getTitol());
        assertEquals("Nocturna-Ediciones", llibre.getEditorial());
        assertEquals(2009, llibre.getAnyPublicacio());

        assertNotNull(llibre.getAutors());
        assertNotNull(llibre.getExemplars());
    }

    @Test
    void testAfegirExemplar() {
        Llibre llibre = Manager.addLlibre("ISBN-333", "El corredor del laberinto", "Nocturna-Ediciones", 2009);
        Exemplar exemplar = new Exemplar();
        exemplar.setLlibre(llibre);
        llibre.getExemplars().add(exemplar);

        assertTrue(llibre.getExemplars().contains(exemplar), "La col·lecció d'exemplars ha de contenir el nou exemplar");
        assertEquals(llibre, exemplar.getLlibre(), "L'exemplar ha de tenir assignat el llibre correcte");
    }

    @Test
    void testToString() {
        Llibre llibre = new Llibre("ISBN-333", "El corredor del laberinto", "Nocturna-Ediciones", 2009);

        String str = llibre.toString();
        assertTrue(str.contains(llibre.getLlibreId().toString()), "El toString hauria de contenir l'ID del llibre");

        assertTrue(str.contains("ISBN-333"), "El toString hauria de contenir l'ISBN");
        assertTrue(str.contains("El corredor del laberinto"), "El toString hauria de contenir el nom del llibre");
    }
}
