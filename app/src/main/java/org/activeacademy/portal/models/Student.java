package org.activeacademy.portal.models;

/**
 * Student is a model class, which refers to the students enrolled in different {@link Course}s at
 * school. A student has an identity, has to pay fee for the courses they take, and may or may not
 * have been referred to the school by an {@link Instructor}.
 *
 * @author saifkhichi96
 * @version 1.0.0
 * @since 1.0.0 01/07/2018 1:45 PM
 */
public class Student {

    private Integer id;
    private String name;
    private Integer discount;
    private Integer referredBy;

    public Integer getId() {
        return id;
    }

    public Student setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Student setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getDiscount() {
        return discount;
    }

    public Student setDiscount(Integer discount) {
        this.discount = discount;
        return this;
    }

    public Integer getReferredBy() {
        return referredBy;
    }

    public Student setReferredBy(Integer referredBy) {
        this.referredBy = referredBy;
        return this;
    }

}