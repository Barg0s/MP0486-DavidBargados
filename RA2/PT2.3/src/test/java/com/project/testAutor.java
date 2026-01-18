package com.project;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import com.project.dao.Manager;
import com.project.domain.Autor;
import com.project.domain.Biblioteca;
import com.project.domain.Exemplar;
import com.project.domain.Llibre;
import com.project.domain.Persona;
import java.time.LocalDate;
import java.util.*;
import com.project.domain.Prestec;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

public class testAutor{
    

    @Test
    void testAutorBuit() {
        Autor autor = new Autor();
        assertNull(autor.getAutorId());
        assertNull(autor.getNom());
        assertNotNull(autor.getLlibres());
        assertTrue(autor.getLlibres().isEmpty());
    }

    @Test
    void testAfegirAutorAmbNomNull(){
        Autor autor = Manager.addAutor(null);
        assertNull(autor,"L'autor hauria de ser null");
    }
    @Test
    void testAfegirAutorAmbNomBuit(){
        Autor autor = Manager.addAutor("");
        assertNull(autor,"L'autor hauria de ser null");
    }

    @Test
    void testAfegirAutorAmbNom(){
        Autor autor = new Autor("David");
        assertEquals("David", autor.getNom());

    }
    @Test
    void TestAfegirLlibre(){
        Autor autor = Manager.addAutor("James Dashner");

        Llibre llibre = Manager.addLlibre("ISBN-333", "El corredor del laberinto", "Nocturna-Ediciones", 2009);

        autor.addLlibre(llibre);

        assertTrue(autor.getLlibres().contains(llibre));
    }
    

    @Test
    void testAfegirAutor() {
        Autor autor = Manager.addAutor("David");

        assertNotNull(autor);
        assertTrue(autor.getAutorId() > 0);
        assertNotNull(autor.getNom());
    }


    @Test
    void testUpdateAutor(){
        Autor autor = Manager.addAutor("Autor a actualitzar");

        Llibre llibre1 = Manager.addLlibre("ISBN-111","Llibre 1","Editorial 1",2020);

        Set<Llibre> llibresAutor = new HashSet<>();
        llibresAutor.add(llibre1);
        Manager.updateAutor(autor.getAutorId(), "Autor Actualitzat", llibresAutor);


        Autor autorBD = Manager.getById(Autor.class, autor.getAutorId());
        assertEquals("Autor Actualitzat", autorBD.getNom(),"El nom no s'actualitzat correctament");
        Llibre llibreBD = Manager.getById(Llibre.class, llibre1.getLlibreId());
        assertTrue(llibreBD.getAutors().contains(autorBD),"El llibre no es troba a aquest autor");
    }

    @Test
    void testToString() {
        Autor autor = Manager.addAutor("David");

        String str = autor.toString();
        assertTrue(str.contains(autor.getAutorId().toString()), "El toString hauria de contenir l'ID del autor");

        assertTrue(str.contains("David"), "El toString hauria de contenir el nom de l'autor");
    }

}
