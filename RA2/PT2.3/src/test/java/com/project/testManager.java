package com.project;
import com.project.dao.Manager;
import com.project.domain.*;


import java.time.LocalDate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class testManager {

    @Test
    void testAfegirAutor(){
        Autor autor = Manager.addAutor("David");
        Autor autorNull = Manager.addAutor("");
        
        assertNull(autorNull,"L'autor hauria de returnar null");
        assertEquals(autor, autorNull);
    }
}
