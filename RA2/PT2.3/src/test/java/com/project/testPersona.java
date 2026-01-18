package com.project;

import com.project.domain.Persona;
import com.project.domain.Prestec;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class testPersona {

    @Test
    void testPersonaBuit() {
        Persona persona = new Persona();

        assertNull(persona.getPersonaId(), "L'id hauria de ser null");
        assertNull(persona.getDni(), "El DNI hauria de ser null");
        assertNull(persona.getNom(), "El nom hauria de ser null");
        assertNull(persona.getTelefon(), "El telèfon hauria de ser null");
        assertNull(persona.getEmail(), "L'email hauria de ser null");

        assertNotNull(persona.getPrestecs(), "La col·lecció de préstecs no hauria de ser null");
        assertTrue(persona.getPrestecs().isEmpty(), "La col·lecció de préstecs hauria d'estar buida");
    }

    @Test
    void testPersonaConstructor() {
        Persona persona = new Persona("12345678A", "David", "600123456", "david@test.com");

        assertEquals("12345678A", persona.getDni());
        assertEquals("David", persona.getNom());
        assertEquals("600123456", persona.getTelefon());
        assertEquals("david@test.com", persona.getEmail());

        assertNotNull(persona.getPrestecs());
        assertTrue(persona.getPrestecs().isEmpty());
    }

    @Test
    void testSettersGetters() {
        Persona persona = new Persona();

        persona.setDni("87654321B");
        persona.setNom("Joan");
        persona.setTelefon("611223344");
        persona.setEmail("joan@test.com");

        assertEquals("87654321B", persona.getDni());
        assertEquals("Joan", persona.getNom());
        assertEquals("611223344", persona.getTelefon());
        assertEquals("joan@test.com", persona.getEmail());
    }

    @Test
    void testAfegirPrestec() {
        Persona persona = new Persona("12345678A", "David", "600123456", "david@test.com");
        Prestec prestec = new Prestec();
        prestec.setPersona(persona);

        persona.getPrestecs().add(prestec);

        assertTrue(persona.getPrestecs().contains(prestec), "La col·lecció de préstecs ha de contenir el nou préstec");
        assertEquals(persona, prestec.getPersona(), "El préstec ha de tenir assignada la persona correcta");
    }



    @Test
    void testToString() {
        Persona persona = new Persona("12345678A", "David", "600123456", "david@test.com");

        String str = persona.toString();
        assertTrue(str.contains("12345678A"), "El toString hauria de contenir el DNI");
        assertTrue(str.contains("David"), "El toString hauria de contenir el nom");
    }
}
