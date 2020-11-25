package seedu.schedar.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.schedar.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.schedar.logic.parser.CliSyntax.PREFIX_PRIORITY;
import static seedu.schedar.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.schedar.logic.parser.CliSyntax.PREFIX_TITLE;
import static seedu.schedar.model.Model.PREDICATE_SHOW_ALL_TASKS;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.schedar.commons.core.Messages;
import seedu.schedar.commons.core.index.Index;
import seedu.schedar.commons.util.CollectionUtil;
import seedu.schedar.logic.CommandHistory;
import seedu.schedar.logic.commands.exceptions.CommandException;
import seedu.schedar.model.Model;
import seedu.schedar.model.tag.Tag;
import seedu.schedar.model.task.Description;
import seedu.schedar.model.task.Priority;
import seedu.schedar.model.task.Task;
import seedu.schedar.model.task.Title;
import seedu.schedar.model.task.ToDo;

//@@author e0316059
//Reused from https://github.com/AY1920S2-CS2103T-W12-4/main with minor modifications
/**
 * Edits the details of an existing todo in the task manager.
 */
public class EditTodoCommand extends Command {

    public static final String COMMAND_WORD = "editTodo";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the task identified "
            + "by the index number used in the displayed task list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_TITLE + "TITLE "
            + PREFIX_DESCRIPTION + "DESCRIPTION "
            + PREFIX_PRIORITY + "PRIORITY "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_TITLE + "tP Tasks "
            + PREFIX_DESCRIPTION + "Refactor tP Code "
            + PREFIX_PRIORITY + "High "
            + PREFIX_TAG + "cs2103 "
            + PREFIX_TAG + "project";

    public static final String MESSAGE_EDIT_TASK_SUCCESS = "Edited Task: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the task list.";
    public static final String MESSAGE_WRONG_TYPE = "This task selected is not in the type Todo.";

    private final Index index;
    private final EditTodoDescriptor editTodoDescriptor;

    /**
     * @param index                of the task in the filtered task list to edit
     * @param editTodoDescriptor details to edit the task with
     */
    public EditTodoCommand(Index index, EditTodoDescriptor editTodoDescriptor) {
        requireNonNull(index);
        requireNonNull(editTodoDescriptor);

        this.index = index;
        this.editTodoDescriptor = new EditTodoDescriptor(editTodoDescriptor);
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Task> lastShownList = model.getFilteredTaskList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        Task taskToEdit = lastShownList.get(index.getZeroBased());

        if (!(taskToEdit instanceof ToDo)) {
            throw new CommandException(MESSAGE_WRONG_TYPE);
        }

        Task editedTask = createEditedTodo((ToDo) taskToEdit, editTodoDescriptor);

        if (!taskToEdit.isSameTask(editedTask) && model.hasTask(editedTask)) {
            throw new CommandException(MESSAGE_DUPLICATE_TASK);
        }

        model.setTask(taskToEdit, editedTask);
        model.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
        model.commitTaskManager();

        return new CommandResult(String.format(MESSAGE_EDIT_TASK_SUCCESS, editedTask));
    }
    
    //@@author

    /**
     * Creates and returns a {@code Todo} with the details of {@code todoToEdit}
     * edited with {@code editTodoDescriptor}.
     */
    public static ToDo createEditedTodo(ToDo todoToEdit, EditTodoDescriptor editTodoDescriptor) {
        assert todoToEdit != null;

        Title updatedTitle = editTodoDescriptor.getTitle().orElse(todoToEdit.getTitle());
        Description updatedDescription = editTodoDescriptor.getDescription()
                .orElse(todoToEdit.getDescription());
        Priority updatedPriority = editTodoDescriptor.getPriority().orElse(todoToEdit.getPriority());
        Set<Tag> updatedTagList = editTodoDescriptor.getTagList().orElse(todoToEdit.getTags());

        return new ToDo(updatedTitle, updatedDescription, updatedPriority, updatedTagList);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditTodoCommand)) {
            return false;
        }

        // state check
        EditTodoCommand e = (EditTodoCommand) other;
        return index.equals(e.index)
                && editTodoDescriptor.equals(e.editTodoDescriptor);
    }

    /**
     * Stores the details to edit the todo task with. Each non-empty field value will replace the
     * corresponding field value of the todo task.
     */
    public static class EditTodoDescriptor {
        private Title title;
        private Description description;
        private Priority priority;
        private Set<Tag> tagList;

        public EditTodoDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditTodoDescriptor(EditTodoDescriptor toCopy) {
            setTitle(toCopy.title);
            setDescription(toCopy.description);
            setPriority(toCopy.priority);
            setTagList(toCopy.tagList);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(title, description, priority, tagList);
        }

        public Optional<Title> getTitle() {
            return Optional.ofNullable(title);
        }

        public Optional<Description> getDescription() {
            return Optional.ofNullable(description);
        }

        public Optional<Priority> getPriority() {
            return Optional.ofNullable(priority);
        }

        public Optional<Set<Tag>> getTagList() {
            return Optional.ofNullable(tagList);
        }

        public void setTitle(Title title) {
            this.title = title;
        }

        public void setDescription(Description description) {
            this.description = description;
        }

        public void setPriority(Priority priority) {
            this.priority = priority;
        }

        public void setTagList(Set<Tag> tagList) {
            this.tagList = tagList;
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditTodoDescriptor)) {
                return false;
            }

            // state check
            EditTodoDescriptor e = (EditTodoDescriptor) other;

            return getTitle().equals(e.getTitle())
                    && getDescription().equals(e.getDescription())
                    && getPriority().equals(e.getPriority())
                    && getTagList().equals(e.getTagList());
        }
    }

}
