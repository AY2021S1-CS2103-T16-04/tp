package seedu.schedar.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.schedar.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.schedar.logic.parser.CliSyntax.PREFIX_PRIORITY;
import static seedu.schedar.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.schedar.logic.parser.CliSyntax.PREFIX_TASK_DATE;
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
import seedu.schedar.model.task.Deadline;
import seedu.schedar.model.task.Description;
import seedu.schedar.model.task.Priority;
import seedu.schedar.model.task.Task;
import seedu.schedar.model.task.TaskDate;
import seedu.schedar.model.task.Title;

//@@author e0316059
//Reused from https://github.com/AY1920S2-CS2103T-W12-4/main with minor modifications
/**
 * Edits the details of an existing deadline in the task manager.
 */
public class EditDeadlineCommand extends Command {

    public static final String COMMAND_WORD = "editDeadline";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the task identified "
            + "by the index number used in the displayed task list. "
            + "Existing values will be overwritten by the input values.\n"
            + PREFIX_TITLE + "TITLE "
            + PREFIX_DESCRIPTION + "DESCRIPTION "
            + PREFIX_PRIORITY + "PRIORITY "
            + PREFIX_TASK_DATE + "DEADLINE DATE "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_TITLE + "tP Tasks "
            + PREFIX_DESCRIPTION + "Refactor tP Code "
            + PREFIX_PRIORITY + "High "
            + PREFIX_TASK_DATE + "2020-10-01 "
            + PREFIX_TAG + "cs2103 "
            + PREFIX_TAG + "project";

    public static final String MESSAGE_EDIT_TASK_SUCCESS = "Edited Task: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the task list.";
    public static final String MESSAGE_WRONG_TYPE = "This task selected is not in the type Deadline.";

    private final Index index;
    private final EditDeadlineDescriptor editDeadlineDescriptor;

    /**
     * @param index                of the task in the filtered task list to edit
     * @param editDeadlineDescriptor details to edit the task with
     */
    public EditDeadlineCommand(Index index, EditDeadlineDescriptor editDeadlineDescriptor) {
        requireNonNull(index);
        requireNonNull(editDeadlineDescriptor);

        this.index = index;
        this.editDeadlineDescriptor = editDeadlineDescriptor;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Task> lastShownList = model.getFilteredTaskList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        Task taskToEdit = lastShownList.get(index.getZeroBased());

        if (!(taskToEdit instanceof Deadline)) {
            throw new CommandException(MESSAGE_WRONG_TYPE);
        }

        Task editedTask = createEditedDeadline((Deadline) taskToEdit, editDeadlineDescriptor);

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
     * Creates and returns a {@code Deadline} with the details of {@code deadlineToEdit}
     * edited with {@code editDeadlineDescriptor}.
     */
    public static Deadline createEditedDeadline(Deadline deadlineToEdit,
                                                EditDeadlineDescriptor editDeadlineDescriptor) {
        assert deadlineToEdit != null;

        Title updatedTitle = editDeadlineDescriptor.getTitle().orElse(deadlineToEdit.getTitle());
        Description updatedDescription = editDeadlineDescriptor.getDescription()
                .orElse(deadlineToEdit.getDescription());
        Priority updatedPriority = editDeadlineDescriptor.getPriority()
                .orElse(deadlineToEdit.getPriority());
        TaskDate updatedTaskDeadline = editDeadlineDescriptor.getTaskDeadline()
                .orElse(deadlineToEdit.getDeadlineDate());
        Set<Tag> updatedTagList = editDeadlineDescriptor.getTagList().orElse(deadlineToEdit.getTags());

        return new Deadline(updatedTitle, updatedDescription, updatedPriority, updatedTaskDeadline, updatedTagList);
    }


    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditDeadlineCommand)) {
            return false;
        }

        // state check
        EditDeadlineCommand e = (EditDeadlineCommand) other;
        return index.equals(e.index)
                && editDeadlineDescriptor.equals(e.editDeadlineDescriptor);
    }

    /**
     * Stores the details to edit the deadline task with. Each non-empty field value will replace the
     * corresponding field value of the deadline task.
     */
    public static class EditDeadlineDescriptor {
        private Title title;
        private Description description;
        private Priority priority;
        private TaskDate taskDeadline;
        private Set<Tag> tagList;

        public EditDeadlineDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditDeadlineDescriptor(EditDeadlineCommand.EditDeadlineDescriptor toCopy) {
            setTitle(toCopy.title);
            setDescription(toCopy.description);
            setPriority(toCopy.priority);
            setTaskDeadline(toCopy.taskDeadline);
            setTagList(toCopy.tagList);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(title, description, priority, taskDeadline, tagList);
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

        public Optional<TaskDate> getTaskDeadline() {
            return Optional.ofNullable(taskDeadline);
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

        public void setTaskDeadline(TaskDate taskDeadline) {
            this.taskDeadline = taskDeadline;
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
            if (!(other instanceof EditDeadlineDescriptor)) {
                return false;
            }

            // state check
            EditDeadlineDescriptor e = (EditDeadlineDescriptor) other;

            return getTitle().equals(e.getTitle())
                    && getDescription().equals(e.getDescription())
                    && getPriority().equals(e.getPriority())
                    && getTaskDeadline().equals(e.getTaskDeadline())
                    && getTagList().equals(e.getTagList());
        }
    }

}
