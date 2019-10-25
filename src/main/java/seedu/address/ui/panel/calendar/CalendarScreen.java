package seedu.address.ui.panel.calendar;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import javafx.scene.layout.StackPane;
import seedu.address.model.events.EventSource;
import seedu.address.ui.UiParser;
import seedu.address.ui.UiPart;

/**
 * An Ui that represents an actual calendar of a month.
 */
public class CalendarScreen extends UiPart<Region> {

    private static final String FXML = "CalendarScreen.fxml";

    private Integer month;
    private Integer year;
    private LocalDate localDate;
    private YearMonth yearMonth;
    private ArrayList<ArrayList<CalendarGridDay>> weekList;
    private ArrayList<CalendarGridDay> dayIndexList;

    private UiParser uiParser;

    @FXML
    private GridPane calendarGrid;

    @FXML
    private Label monthYearTitle;

    @FXML
    private StackPane details;

    /**
     * Constructor for CalendarScreen that adds a month and year to form the calendar.
     *
     * @param month Represents the month of the calendar.
     * @param year Represents the year of the calendar.
     * @param uiParser Represents a parser to convert certain types of objects into other types of objects.
     *
     */
    public CalendarScreen(Integer month, Integer year, UiParser uiParser) {
        super(FXML);
        this.month = month;
        this.year = year;
        this.uiParser = uiParser;
        this.localDate = LocalDate.of(year, month, 1);
        this.yearMonth = YearMonth.of(year, month);
        this.dayIndexList = new ArrayList<>();
        this.weekList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            this.weekList.add(new ArrayList<CalendarGridDay>());
        }
        this.monthYearTitle.setText(uiParser.getEnglishDate(month, year));

        resetCalendar();
    }

    /**
     * Fills the index of the calendar and resets when needed to.
     */
    private void resetCalendar() {
        Integer startingDay = resetPreviousMonth();
        resetCurrentNextMonth(startingDay);
    }

    /**
     * Resets the calendar to add in the current and next month values.
     */
    private void resetCurrentNextMonth(Integer startingDay) {
        // Adds Current Month Calendar.
        boolean nextMonth = false;
        int index = 1;
        int totalDays = this.yearMonth.lengthOfMonth();
        for (int weeks = 0; weeks < 6; weeks++) {
            for (int days = 0; days < 7; days++) {
                if (weeks == 0 && days == 0) {
                    days = startingDay;
                }
                if (index > totalDays) {
                    // Resets to next month.
                    index = 1;
                    nextMonth = true;
                }
                if (!nextMonth) {
                    CalendarGridDay calendarGridDay = new CalendarGridDay(index, month, year, 0);
                    calendarGrid.add(calendarGridDay.getRoot(), days, weeks);
                    weekList.get(weeks).add(calendarGridDay);
                    dayIndexList.add(calendarGridDay);
                    index++;
                } else {
                    CalendarGridDay calendarGridDay = new CalendarGridDay(
                            index, uiParser.getNextMonth(month), uiParser.getNextYear(month, year), 0);
                    calendarGrid.add(calendarGridDay.getRoot(), days, weeks);
                    calendarGridDay.reduceOpacity();
                    weekList.get(weeks).add(calendarGridDay);
                    dayIndexList.add(calendarGridDay);
                    index++;

                }

            }
        }
    }

    /**
     * Resets the calendar to add in the previous month values.
     */
    private Integer resetPreviousMonth() {
        // Adds Previous Month Calendar;
        int days = this.localDate.withDayOfMonth(1).getDayOfWeek().getValue() - 2;
        Integer previousMonthDay = uiParser.getPreviousMonthDays(this.month, this.year) - days;
        int previousMonth = uiParser.getPreviousMonth(this.month);
        int previousYear = uiParser.getPreviousYear(this.month, this.year);
        int startingDay = 0;
        while (startingDay <= days) {
            CalendarGridDay calendarGridDay = new CalendarGridDay(previousMonthDay, previousMonth, previousYear, 0);
            calendarGrid.add(calendarGridDay.getRoot(), startingDay, 0);
            calendarGridDay.reduceOpacity();
            weekList.get(0).add(calendarGridDay);
            startingDay++;
            previousMonthDay++;
        }
        return startingDay;
    }

    /**
     * Changes the color scheme for each day of the calendar screen.
     * @param events The list of events.
     */
    public void eventChange(List<EventSource> events) {
        changeColor(events);
    }

    /**
     * Returns an array of days, each containing a day, month and year.
     *
     * @param week The given week.
     * @return an array of days, each containing a day, month and year.
     */
    public Integer[][] getStartingDay(int week) {
        ArrayList<CalendarGridDay> currentWeek = weekList.get(week - 1);
        // 7 days a week
        // Each day contains a day, month and year.
        Integer[][] days = new Integer[7][3];
        int dayIndex = 0;
        for (CalendarGridDay day : currentWeek) {
            Integer[] dayMonthYear = day.getDayMonthYear();
            days[dayIndex] = dayMonthYear;
            dayIndex++;
        }
        return days;
    }

    /**
     * Changes the color of the current month to indicate the color of the event by adding
     * events to the given CalendarGridDay.
     *
     * @param events The given list of events.
     * @see CalendarGridDay
     */
    private void changeColor(List<EventSource> events) {
        // We do not want to change the color of next and previous month.
        for (EventSource event : events) {
            if (sameMonthYear(event)) {
                Integer day = uiParser.getDay(event.getStartDateTime().toInstant());
                dayIndexList.get(day - 1).addAnEvent();
            }
        }
    }

    /**
     * Returns a boolean to check if the date is the sane as the current month and year.
     *
     * @param event The given event.
     * @return Returns a boolean to check if the date is the sane as the current month and year.
     */
    private boolean sameMonthYear(EventSource event) {
        Integer[] monthYear = uiParser.getDateToNumbers(event.getStartDateTime().toInstant());
        return monthYear[1].equals(this.month) && monthYear[2].equals(this.year);
    }
}
