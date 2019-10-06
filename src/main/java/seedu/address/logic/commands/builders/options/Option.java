package seedu.address.logic.commands.builders.options;

import java.util.ArrayList;
import java.util.List;

import seedu.address.logic.commands.builders.arguments.Argument;
import seedu.address.logic.commands.builders.arguments.VariableArguments;
import seedu.address.logic.commands.exceptions.ArgumentException;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Represents an option in a Command.
 * It can contain 0..* other arguments, and can contain 0..1 variable arguments.
 */
public class Option {

    // Determines whether or not this option is used in the Command.
    private boolean active;
    private int argumentIndex;
    private final List<Argument> arguments;

    private VariableArguments variableArguments;

    public Option() {
        this.arguments = new ArrayList<>();
    }

    /**
     * Adds an argument to this option.
     * @param argument the argument
     * @return this instance
     */
    public Option addArgument(Argument argument) {
        this.arguments.add(argument);
        return this;
    }

    public Option setVariableArguments(VariableArguments variableArguments) {
        this.variableArguments = variableArguments;
        return this;
    }

    public void acceptArgument(String argument) throws ParseException {
        if (this.argumentIndex < this.arguments.size()) {
            this.arguments.get(this.argumentIndex).accept(argument);
            this.argumentIndex++;
        } else if (this.variableArguments != null) {
            this.variableArguments.accept(argument);
        }
    }

    public void build() throws ArgumentException {
        for (Argument argument : this.arguments) {
            argument.build(this.active);
        }

        if (this.variableArguments != null) {
            this.variableArguments.build();
        }
    }

    public void setActive() {
        this.active = true;
    }

    public boolean isActive() {
        return active;
    }
}