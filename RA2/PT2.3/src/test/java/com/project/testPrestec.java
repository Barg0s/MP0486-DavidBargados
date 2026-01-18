package com.project;

import com.project.domain.Exemplar;
import com.project.domain.Persona;
import com.project.domain.Prestec;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class testPrestec {

    @Test
    void testPrestecBuit() {
        Prestec prestec = new Prestec();

        assertNull(prestec.getPrestecId(), "L'id hauria de ser null");
        assertNull(prestec.getExemplar(), "L'exemplar hauria de ser null");
        assertNull(prestec.getPersona(), "La persona hauria de ser null");
        assertNull(prestec.getDataPrestec(), "La data de préstec hauria de ser null");
        assertNull(prestec.getDataRetornPrevista(), "La data de retorn prevista hauria de ser null");
        assertNull(prestec.getDataRetornReal(), "La data de retorn real hauria de ser null");
        assertFalse(prestec.isActiu(), "Per defecte, actiu hauria de ser false");
    }

    @Test
    void testPrestecConstructor() {
        Exemplar exemplar = new Exemplar();
        Persona persona = new Persona();

        LocalDate avui = LocalDate.now();
        LocalDate retornPrevist = avui.plusDays(14);

        Prestec prestec = new Prestec(exemplar, persona, avui, retornPrevist);

        assertEquals(exemplar, prestec.getExemplar());
        assertEquals(persona, prestec.getPersona());
        assertEquals(avui, prestec.getDataPrestec());
        assertEquals(retornPrevist, prestec.getDataRetornPrevista());
        assertNull(prestec.getDataRetornReal(), "La data de retorn real hauria de ser null al crear-se");
        assertTrue(prestec.isActiu(), "El préstec hauria d'estar actiu per defecte");
    }

    @Test
    void testSettersGetters() {
        Prestec prestec = new Prestec();
        Exemplar exemplar = new Exemplar();
        Persona persona = new Persona();
        LocalDate avui = LocalDate.now();
        LocalDate retornPrevist = avui.plusDays(10);
        LocalDate retornReal = avui.plusDays(9);

        prestec.setExemplar(exemplar);
        prestec.setPersona(persona);
        prestec.setDataPrestec(avui);
        prestec.setDataRetornPrevista(retornPrevist);
        prestec.setDataRetornReal(retornReal);
        prestec.setActiu(false);

        assertEquals(exemplar, prestec.getExemplar());
        assertEquals(persona, prestec.getPersona());
        assertEquals(avui, prestec.getDataPrestec());
        assertEquals(retornPrevist, prestec.getDataRetornPrevista());
        assertEquals(retornReal, prestec.getDataRetornReal());
        assertFalse(prestec.isActiu());
    }

    @Test
    void testToString() {
        Exemplar exemplar = new Exemplar();
        exemplar.setCodiBarres("CB-123");
        Persona persona = new Persona();
        persona.setNom("David");

        LocalDate avui = LocalDate.of(2026, 1, 17);

        Prestec prestec = new Prestec(exemplar, persona, avui, avui.plusDays(7));

        String str = prestec.toString();
        assertTrue(str.contains("CB-123"), "El toString hauria de contenir el codi de l'exemplar");
        assertTrue(str.contains("David"), "El toString hauria de contenir el nom de la persona");
        assertTrue(str.contains(avui.toString()), "El toString hauria de contenir la data de préstec");
        assertTrue(str.contains("actiu=true"), "El toString hauria de contenir l'estat actiu");
    }
}
