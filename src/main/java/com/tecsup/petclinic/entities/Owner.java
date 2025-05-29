package com.tecsup.petclinic.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity(name = "owners")
@Data
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name") // Mapea a la columna 'first_name'
    private String firstName;

    @Column(name = "last_name") // Mapea a la columna 'last_name'
    private String lastName;

    @Column(name = "address") // Mapea a la columna 'address'
    private String address;

    @Column(name = "city") // Mapea a la columna 'city'
    private String city;

    @Column(name = "telephone") // Mapea a la columna 'telephone'
    private String telephone;

    // Constructor vacío (requerido por JPA)
    public Owner() {
    }

    // Constructor para crear nuevos Owners (sin ID, ya que se autogenera)
    public Owner(String firstName, String lastName, String address, String city, String telephone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.telephone = telephone;
    }

    // Constructor con todos los campos (útil para pruebas o mapeo desde otras fuentes)
    public Owner(Integer id, String firstName, String lastName, String address, String city, String telephone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.telephone = telephone;
    }
}
