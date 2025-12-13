package com.project;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.function.Function;

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
            Configuration configuration = new Configuration();
            configuration.addAnnotatedClass(Ciutat.class);
            configuration.addAnnotatedClass(Ciutada.class);
            Properties properties = new Properties();
            try (InputStream input = Manager.class.getClassLoader().getResourceAsStream(propertiesFileName)) {
                if (input == null) {
                    throw new IOException("No s'ha pogut trobar " + propertiesFileName);
                }
                properties.load(input);
            }
            configuration.addProperties(properties);
                        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();
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

        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            Ciutat ciutat = new Ciutat(nom, pais, poblacio);
            session.persist(ciutat);

            tx.commit();
            return ciutat;
        } catch (Exception e) {
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

                Set<Ciutada> currentciutadas = new HashSet<>(ciutat.getCiutadans());
                for (Ciutada dbciutada : currentciutadas) {
                    if (!newCiutadans.contains(dbciutada)) {
                        ciutat.deleteCiutada(dbciutada);
                    }
                }
                for (Ciutada ciutadaInput : newCiutadans) {
                    if (ciutadaInput.getCiutadaId() != null) {
                        Ciutada managedciutada = session.find(Ciutada.class, ciutadaInput.getCiutadaId());
                        if (managedciutada != null && !ciutat.getCiutadans().contains(managedciutada)) {
                            ciutat.addCiutada(managedciutada);
                        }
                    } else {
                        ciutat.addCiutada(ciutadaInput);
                    }
                }
            } else {
                new HashSet<>(ciutat.getCiutadans()).forEach(ciutat::deleteCiutada);
            }
            
            session.merge(ciutat);
            tx.commit();
            
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            e.printStackTrace();
        }
    }
    

    public static void updateCiutada(long ciutadaId,String nom,String cognom,int edat){
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            Ciutada ciutada = session.get(Ciutada.class, ciutadaId); 
            if (ciutada != null) {
                ciutada.setNom(nom);
                ciutada.setCognom(cognom);
                ciutada.setEdat(edat);
                session.merge(ciutada);
                tx.commit();
            }
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            e.printStackTrace(); 
        }
    }
    public static Ciutat getCiutatWithCiutadans(long ciutatId) {
        try (Session session = factory.openSession()) {
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
                session.remove(obj);
                tx.commit();
                System.out.println("Eliminat objecte " + clazz.getSimpleName() + " amb id " + id);
            }
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            e.printStackTrace();
        }
    }


    
    public static void mostrarCiutat(long ciutatId){
        Ciutat ciutat = getCiutatWithCiutadans(ciutatId);
        if (ciutat != null) {
            System.out.println("Ciutadans de la ciutat '" + ciutat.getNom() + "':");
            Set<Ciutada> ciutadans = ciutat.getCiutadans();
            if (ciutadans != null && !ciutadans.isEmpty()) {
                for (Ciutada ciutada : ciutadans) {
                    System.out.println("- " + ciutada.getNom() + " " + ciutada.getCognom());
                }
            } else {
                System.out.println("La ciutat no té ciutadans");
            }
        } else {
            System.out.println("No s'ha trobat la ciutat");
        }
    }
    private static <T> T executeInTransactionWithResult(Function<Session, T> action) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            T result = action.apply(session);
            tx.commit();
            return result;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new RuntimeException("Error en transacció Hibernate", e);
        }
    }
    public static <T> List<T> listCollection(Class<T> clazz, String whereClause) {
        return executeInTransactionWithResult(session -> {
            String hql;
            if (clazz == Ciutat.class) {
                hql = "SELECT c FROM Ciutat c LEFT JOIN FETCH c.ciutadans";
            } else {
                hql = "FROM " + clazz.getName();
            }

            if (whereClause != null && !whereClause.trim().isEmpty()) {
                hql += " WHERE " + whereClause;
            }

            return session.createQuery(hql, clazz).list();
        });
    }

}
