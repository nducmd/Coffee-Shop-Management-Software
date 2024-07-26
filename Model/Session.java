/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.Alert;

/**
 *
 * @author nducmd
 */
public class Session {

    public static Integer numberOfTable = 100;
    public static LocalDateTime startTime;
    public static LocalDateTime endTime;
    public static Employee currEmployee;

    // start time
    public static void setStartTime() {
        startTime = LocalDateTime.now();
    }

    // end time
    public static void setEndTime() {
        endTime = LocalDateTime.now();
    }

    public static int getDurationTime() {
        Duration duration = Duration.between(startTime, endTime);
        return (int) duration.toHours();
    }

    public static boolean checkAge(LocalDate selectedDate) {
        LocalDate currentDate = LocalDate.now();
        LocalDate minDate = currentDate.minusYears(100);
        LocalDate maxDate = currentDate.minusYears(18);
        //System.out.println(minDate + " " + currentDate + " " + maxDate);
        try {
            if (selectedDate != null) {
                if (!(selectedDate.isAfter(minDate) && selectedDate.isBefore(maxDate))) {
                    throw new AgeException();
                }
            }
        } catch (AgeException ae) {
            return false;
        }
        return true;
    }
}
