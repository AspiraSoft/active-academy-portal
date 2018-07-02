package org.activeacademy.portal.models;

public class TimetableEntry {

    private final String lectureTime;
    private final String gradeName;
    private final String courseName;

    public TimetableEntry(Grade grade, Course course, Lecture lecture) {
        courseName = course.getName();
        gradeName = grade.getName();
        lectureTime = lecture.getStartTime() + " - " + lecture.getEndTime();
    }

    public String getLectureTime() {
        return lectureTime;
    }

    public String getGradeName() {
        return gradeName;
    }

    public String getCourseName() {
        return courseName;
    }

}