package com.project;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.hibernate.Session; 
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class Manager {

    private static SessionFactory factory;

    public static void createSessionFactory() {
        createSessionFactory("hibernate.properties");
    }


public static void createSessionFactory(String propertiesFileName) {
        try {
            // CONFIGURATION: Configura Hibernate programàticament
            Configuration configuration = new Configuration();
            
            // Registrem les classes @Entity que Hibernate ha de gestionar
            configuration.addAnnotatedClass(Ciutat.class);
            configuration.addAnnotatedClass(Ciutada.class);

            // Carreguem les propietats des del fitxer (URL BBDD, usuari, contrasenya...)
            Properties properties = new Properties();
            try (InputStream input = Manager.class.getClassLoader().getResourceAsStream(propertiesFileName)) {
                if (input == null) {
                    throw new IOException("No s'ha pogut trobar " + propertiesFileName);
                }
                properties.load(input);
            }
            configuration.addProperties(properties);
            
            // SERVICE REGISTRY: Gestiona els serveis interns d'Hibernate
            StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();
                
            // Construïm el SessionFactory (operació costosa, només es fa un cop)
            factory = configuration.buildSessionFactory(serviceRegistry);
            
        } catch (Throwable ex) { 
            System.err.println("Error en crear sessionFactory: " + ex);
            throw new ExceptionInInitializerError(ex); 
        }
    }

    public static void close() {
        if (factory != null) factory.close();
    }
 


    public static <T> String collectionToString(Collection<T> collection) {
        StringBuilder sb = new StringBuilder();
        for (T obj : collection) {
            sb.append(obj.toString()).append("\n");
        }
        return sb.toString();
    }

   //CRUD
    public static Ciutat addCiutat(String nom, String pais, int poblacio) {
        Transaction tx = null;
        // TRY-WITH-RESOURCES: Tanca la Session automàticament al acabar
        // (Session implementa AutoCloseable)
        try (Session session = factory.openSession()) {
            // TRANSACTION: Agrupa operacions. Si falla alguna, es pot fer rollback.
            tx = session.beginTransaction();
            Ciutat ciutat = new Ciutat(nom, pais, poblacio);
            // PERSIST: Guarda l'objecte a la BBDD i li assigna un ID
            session.persist(ciutat);
            // COMMIT: Confirma els canvis a la BBDD
            tx.commit();
            return ciutat;
        } catch (Exception e) {
            // ROLLBACK: Desfà tots els canvis si hi ha error
            if (tx != null && tx.isActive()) tx.rollback();
            System.err.println("Error creant ciutat: " + e.getMessage());
            e.printStackTrace(); 
            return null;
        }
    }

    public static Ciutada addCiutada(String nom,String cognom,int edat){
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            Ciutada ciutada = new Ciutada(nom, cognom, edat);
            session.persist(ciutada);
            tx.commit();
            return ciutada;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            System.err.println("Error creant ciutada: " + e.getMessage());
            return null;
        }
    }


    public static void updateCiutat(long ciutatId, String type,String pais,int poblacio, Set<Ciutada> newCiutadans) {
   Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            Ciutat ciutat = session.get(Ciutat.class, ciutatId);
            
            if (ciutat == null) {
                System.err.println("ciutat no trobat amb id: " + ciutatId);
                return;
            }
            
            ciutat.setNom(type);
            ciutat.setPais(pais);
            ciutat.setPoblacio(poblacio);
            ciutat.setCiutadans(newCiutadans);

            
            if (newCiutadans != null) {
                // PAS 1: Eliminar ciutadas que ja no estan a la nova llista
                // Còpia per evitar ConcurrentModificationException mentre iterem i modifiquem
                Set<Ciutada> currentciutadas = new HashSet<>(ciutat.getCiutadans());
                for (Ciutada dbciutada : currentciutadas) {
                    if (!newCiutadans.contains(dbciutada)) {
                        ciutat.deleteCiutada(dbciutada);
                    }
                }

                // PAS 2: Afegir o actualitzar ciutadas de la nova llista
                for (Ciutada ciutadaInput : newCiutadans) {
                    if (ciutadaInput.getCiutadaId() != null) {
                        // FIND: Recupera l'entitat "managed" (gestionada per la sessió)
                        // Evita errors de "detached entity" quan l'objecte ve de fora la sessió
                        Ciutada managedciutada = session.find(Ciutada.class, ciutadaInput.getCiutadaId());
                        if (managedciutada != null && !ciutat.getCiutadans().contains(managedciutada)) {
                            ciutat.addCiutada(managedciutada);
                        }
                    } else {
                        // ciutada nou sense ID: s'afegeix i es persistirà per CASCADE
                        ciutat.addCiutada(ciutadaInput);
                    }
                }
            } else {
                // Si ciutadas és null, eliminem tots els ciutadas del ciutat
                new HashSet<>(ciutat.getCiutadans()).forEach(ciutat::deleteCiutada);
            }
            
            session.merge(ciutat);
            tx.commit();
            System.out.println("ciutat " + ciutatId + " actualitzat.");
            
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            e.printStackTrace();
        }
    }
    

    public static void updateCiutada(long ciutadaId,String nom,String cognom,int edat){
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            // GET: Recupera l'entitat per ID. Retorna null si no existeix.
            Ciutada ciutada = session.get(Ciutada.class, ciutadaId); 
            if (ciutada != null) {
                ciutada.setNom(nom);
                ciutada.setCognom(cognom);
                ciutada.setEdat(edat);
                // MERGE: Sincronitza l'estat de l'objecte amb la BBDD
                session.merge(ciutada);
                tx.commit();
                System.out.println("ciutada " + ciutadaId + " actualitzat.");   
            }
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            e.printStackTrace(); 
        }
    }
    public static Ciutat getCiutatWithCiutadans(long ciutatId) {
        try (Session session = factory.openSession()) {
            // JOIN FETCH: Soluciona el problema de LAZY LOADING.
            // Carrega ciutat + ciutadas en UNA SOLA consulta SQL.
            // Sense això, accedir a getciutadas() fora de la sessió llançaria
            // LazyInitializationException.
            String hql = "SELECT c FROM Ciutat c LEFT JOIN FETCH c.ciutadans WHERE c.ciutatId = :id";
            return session.createQuery(hql, Ciutat.class)
                          .setParameter("id", ciutatId)
                          .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> void delete(Class<T> clazz, Serializable id) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            T obj = session.get(clazz, id);
            if (obj != null) {
                // REMOVE: Elimina l'entitat de la BBDD
                session.remove(obj);
                tx.commit();
                System.out.println("Eliminat objecte " + clazz.getSimpleName() + " amb id " + id);
            }
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            e.printStackTrace();
        }
    }

}
