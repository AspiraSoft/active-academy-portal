package org.activeacademy.portal.models;

import java.util.ArrayList;
import java.util.List;

public class CourseEntry {

    private final String gradeName;
    private final String courseName;
    private final List<TimetableEntry> lectures;

    public CourseEntry(Grade grade, Course course) {
        courseName = course.getName();
        gradeName = grade.getName();

        lectures = new ArrayList<>();
        if (course.getLectures() != null) {
            for (Lecture lecture : course.getLectures()) {
                if (lecture != null) {
                    lectures.add(new TimetableEntry(grade, course, lecture));
                }
            }
        }
    }

    public String getGradeName() {
        return gradeName;
    }

    public String getCourseName() {
        return courseName;
    }

    public List<TimetableEntry> getLectures() {
        return lectures;
    }

}