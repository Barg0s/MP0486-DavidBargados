package com.project;

import com.project.dao.Manager;
import com.project.domain.Autor;
import com.project.domain.Biblioteca;
import com.project.domain.Exemplar;
import com.project.domain.Llibre;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

public class testBiblioteca {

    @Test
    void testBibliotecaBuit() {
        Biblioteca biblioteca = new Biblioteca();

        assertNull(biblioteca.getBibliotecaId(), "L'id hauria de ser null");
        assertNull(biblioteca.getNom(), "El nom hauria de ser null");
        assertNull(biblioteca.getCiutat(), "La ciutat hauria de ser null");
        assertNull(biblioteca.getAdreca(), "L'adreça hauria de ser null");
        assertNull(biblioteca.getTelefon(), "El telèfon hauria de ser null");
        assertNull(biblioteca.getEmail(), "L'email hauria de ser null");

        assertNotNull(biblioteca.getExemplars(), "La col·lecció d'exemplars no hauria de ser null");
        assertTrue(biblioteca.getExemplars().isEmpty(), "La col·lecció d'exemplars hauria d'estar buida");
    }

    @Test
    void testBibliotecaConstructor() {
        Biblioteca biblioteca = new Biblioteca("Biblioteca Central", "Barcelona", "Carrer Major 1", "934567890", "info@biblioteca.cat");

        assertEquals("Biblioteca Central", biblioteca.getNom());
        assertEquals("Barcelona", biblioteca.getCiutat());
        assertEquals("Carrer Major 1", biblioteca.getAdreca());
        assertEquals("934567890", biblioteca.getTelefon());
        assertEquals("info@biblioteca.cat", biblioteca.getEmail());

        assertNotNull(biblioteca.getExemplars());
        assertTrue(biblioteca.getExemplars().isEmpty());
    }

    @Test
    void testAfegirExemplar() {
        Biblioteca biblioteca = new Biblioteca("Biblioteca Central", "Barcelona", "Carrer Major 1", "934567890", "info@biblioteca.cat");
        Exemplar exemplar = new Exemplar();
        exemplar.setBiblioteca(biblioteca);
        biblioteca.getExemplars().add(exemplar);

        assertTrue(biblioteca.getExemplars().contains(exemplar), "La col·lecció d'exemplars ha de contenir el nou exemplar");
        assertEquals(biblioteca, exemplar.getBiblioteca(), "L'exemplar ha de tenir assignada la biblioteca correcta");
    }


    @Test
    void testToString() {
        Biblioteca biblioteca = new Biblioteca("Biblioteca Central", "Barcelona", "Carrer Major 1", "934567890", "info@biblioteca.cat");
        String str = biblioteca.toString();

        assertTrue(str.contains("Biblioteca Central"), "El toString hauria de contenir el nom");
        assertTrue(str.contains("Barcelona"), "El toString hauria de contenir la ciutat");
    }
}
