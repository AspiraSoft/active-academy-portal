package org.activeacademy.portal.models;

import java.util.List;

/**
 * Course is a model class, which refers to the subject courses taught in school. A subject course
 * has an {@link Instructor}, and is taught in a series of {@link Lecture}s scheduled at different
 * days and times.
 *
 * @author saifkhichi96
 * @version 1.0.0
 * @since 01/07/2018 1:42 PM
 */
public class Course {

    private Long id;
    private String name;
    private String instructorId;

    private List<Lecture> lectures;

    public Long getId() {
        return id;
    }

    public Course setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Course setName(String name) {
        this.name = name;
        return this;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public Course setInstructorId(String instructorId) {
        this.instructorId = instructorId;
        return this;
    }

    public List<Lecture> getLectures() {
        return lectures;
    }

    public Course setLectures(List<Lecture> lectures) {
        this.lectures = lectures;
        return this;
    }

}