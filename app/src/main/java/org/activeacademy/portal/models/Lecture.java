package org.activeacademy.portal.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Lecture is a model class, which refers to a session during which a particular {@link Course} is
 * taught at school. It is characterized by its scheduled day/time and duration;
 *
 * @author saifkhichi96
 * @version 1.0.0
 * @since 1.0.0 01/07/2018 1:48 PM
 */
public class Lecture extends RemoteObject {

    private transient final SimpleDateFormat inputTimeFormat = new SimpleDateFormat("H:mm");
    private transient final SimpleDateFormat outputTimeFormat = new SimpleDateFormat("K:mm a");

    private String day;
    private Long duration;
    private String startTime;

    public String getDay() {
        return day;
    }

    public Lecture setDay(String day) {
        this.day = day;
        return this;
    }

    public Long getDuration() {
        return duration;
    }

    public Lecture setDuration(Long duration) {
        this.duration = duration;
        return this;
    }

    public String getStartTime() {
        try {
            Date lectureStart = inputTimeFormat.parse(startTime);
            return outputTimeFormat.format(lectureStart);
        } catch (ParseException e) {
            return startTime;
        }
    }

    public Lecture setStartTime(String startTime) {
        this.startTime = startTime;
        return this;
    }

    public String getEndTime() {
        try {
            Date lectureStart = inputTimeFormat.parse(startTime);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(lectureStart);
            calendar.add(Calendar.MINUTE, Integer.parseInt(String.valueOf(duration)));

            return outputTimeFormat.format(calendar.getTime());
        } catch (ParseException e) {
            return "12:00 PM";
        }
    }

}