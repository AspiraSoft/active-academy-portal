package org.activeacademy.portal.models;

import java.util.List;

/**
 * Grade is a model class, which refers to the school grades. A school grade is a stage in a
 * {@link Student}'s education. A list of {@link Course}s specific to this particular school
 * grade is taught to all the students enrolled in this grade.
 *
 * @author saifkhichi96
 * @version 1.0.0
 * @since 1.0.0 01/07/2018 1:38 PM
 */
public class Grade extends RemoteObject {

    private Long id;
    private String name;
    private Long feePerStudent;

    private List<Course> courses;

    public Long getId() {
        return id;
    }

    public Grade setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Grade setName(String name) {
        this.name = name;
        return this;
    }

    public Long getFeePerStudent() {
        return feePerStudent;
    }

    public Grade setFeePerStudent(Long feePerStudent) {
        this.feePerStudent = feePerStudent;
        return this;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public Grade setCourses(List<Course> courses) {
        this.courses = courses;
        return this;
    }
}
