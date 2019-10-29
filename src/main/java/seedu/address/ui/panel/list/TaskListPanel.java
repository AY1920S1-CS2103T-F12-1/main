package seedu.address.ui.panel.list;

import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.tasks.TaskSource;
import seedu.address.ui.UiPart;
import seedu.address.ui.card.TaskCard;

import java.util.List;

/**
 * Represents the a Task List to be displayed to the user.
 */
public class TaskListPanel extends UiPart<Region> {
    private static final String FXML = "TaskListPanel.fxml";

    @FXML
    private VBox taskList;

    public TaskListPanel() {
        super(FXML);
    }

    /**
     * Changes the Event List Panel according to the given List of Tasks.
     *
     * @param tasks The given List of Tasks.
     */
    public void onTaskListChange(List<TaskSource> tasks) {
        this.taskList.getChildren().clear();
        for (TaskSource task : tasks) {
            TaskCard taskCard = new TaskCard(task);
            taskList.getChildren().add(taskCard.getRoot());
        }
    }

}