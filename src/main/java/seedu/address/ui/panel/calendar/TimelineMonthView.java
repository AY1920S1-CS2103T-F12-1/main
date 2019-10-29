package seedu.address.ui.panel.calendar;

import java.util.List;

import javafx.scene.layout.RowConstraints;

import seedu.address.model.CalendarDate;
import seedu.address.model.DateTime;
import seedu.address.model.events.EventSource;
import seedu.address.ui.card.CardHolder;
import seedu.address.ui.card.EventCard;

/**
 * An UI component that displays the feedback to the user.
 */
public class TimelineMonthView extends TimelineView {

    private static final String FXML = "TimelineMonthView.fxml";

    private CalendarDate calendarDate;

    /**
     * Constructor for TimelineMonthView for a particular month.
     *
     * @param calendarDate Represents the given date.
     * @param eventList Represents the current list of events.
     */
    public TimelineMonthView(CalendarDate calendarDate, List<EventSource> eventList) {
        super(FXML);
        this.calendarDate = calendarDate;
        setTotalRows(calendarDate.lengthOfMonth());

        setTimelineTitle("Timeline: " + calendarDate.getEnglishMonth() + " " + calendarDate.getYear());
        addGrids();
        addLabels(getLabels());
        addEventCardHolders();
        eventChange(eventList);
    }

    /**
     * Returns an array of String containing values of the Month.
     *
     * @return Returns an array of String containing values of the Month.
     */
    private String[] getLabels() {
        String[] labels = new String[getTotalRows()];
        for (Integer day = 0; day < getTotalRows(); day++) {
            labels[day] = String.valueOf(day + 1);
        }
        return labels;
    }

    @Override
    void addEventCard(EventSource event) {
        // Gets the event Hour
        Integer eventDay = event.getStartDateTime().getDay();

        EventCard eventCard = new EventCard(event);
        CardHolder eventCardHolder = getEventCardHolder().get(eventDay - 1);
        eventCardHolder.addEvent(eventCard);

        // Set Constraints for the grid pane
        RowConstraints rowConstraints = getTimelineGrid().getRowConstraints().get(eventDay - 1);
        updateSizeDelay(rowConstraints, eventCardHolder);
    }

    @Override
    boolean isWithinTimeline(EventSource event) {
        DateTime eventDateTime = event.getStartDateTime();
        return calendarDate.sameMonthYear(eventDateTime.getMonth(), eventDateTime.getYear());
    }
}