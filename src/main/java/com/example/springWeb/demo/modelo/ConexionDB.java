/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.springWeb.demo.modelo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class ConexionDB {

    // Variable estática para almacenar la instancia única de la clase
    private static ConexionDB instance;
    private EntityManagerFactory entityManagerFactory;

    // Nombre de la unidad de persistencia (definido en persistence.xml si no estás usando Spring Boot)
    private final String persistenceUnitName = "default";

    // Constructor privado para evitar instancias externas
    private ConexionDB() {
        try {
            // Crear la fábrica de EntityManager
            this.entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
            System.out.println("EntityManagerFactory creado exitosamente.");
        } catch (Exception e) {
            System.err.println("Error al inicializar Hibernate: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // Método estático para obtener la única instancia de la clase
    public static ConexionDB getInstance() {
        if (instance == null) {
            synchronized (ConexionDB.class) {
                if (instance == null) {
                    instance = new ConexionDB();
                }
            }
        }
        return instance;
    }

    // Método para obtener el EntityManager
    public EntityManager getEntityManager() {
        if (entityManagerFactory == null) {
            throw new IllegalStateException("EntityManagerFactory no inicializado.");
        }
        return entityManagerFactory.createEntityManager();
    }

    // Método para cerrar la EntityManagerFactory al finalizar la aplicación
    public void closeEntityManagerFactory() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
            System.out.println("EntityManagerFactory cerrado correctamente.");
        }
    }
}
