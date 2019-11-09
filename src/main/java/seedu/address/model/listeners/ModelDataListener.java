package seedu.address.model.listeners;

import seedu.address.model.ModelData;

//@@author bruceskellator

/**
 * Represents a listener that will be notified whenever the ModelData in ModelManager changes.
 */
public interface ModelDataListener {

    void onModelDataChange(ModelData modelData);

}