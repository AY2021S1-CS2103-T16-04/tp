package seedu.schedar.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.schedar.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.schedar.logic.parser.CliSyntax.PREFIX_PRIORITY;
import static seedu.schedar.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.schedar.logic.parser.CliSyntax.PREFIX_TASK_DATE;
import static seedu.schedar.logic.parser.CliSyntax.PREFIX_TASK_TIME;
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
import seedu.schedar.model.task.Event;
import seedu.schedar.model.task.Priority;
import seedu.schedar.model.task.Task;
import seedu.schedar.model.task.TaskDate;
import seedu.schedar.model.task.TaskTime;
import seedu.schedar.model.task.Title;

//@@author e0316059
//Reused from https://github.com/AY1920S2-CS2103T-W12-4/main with minor modifications
/**
 * Edits the details of an existing event in the task manager.
 */
public class EditEventCommand extends Command {

    public static final String COMMAND_WORD = "editEvent";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the task identified "
            + "by the index number used in the displayed task list. "
            + "Existing values will be overwritten by the input values.\n"
            + PREFIX_TITLE + "TITLE "
            + PREFIX_DESCRIPTION + "DESCRIPTION "
            + PREFIX_PRIORITY + "PRIORITY "
            + PREFIX_TASK_DATE + "EVENT DATE "
            + PREFIX_TASK_TIME + "EVENT TIME "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_TITLE + "CS2103 party "
            + PREFIX_DESCRIPTION + "Coding Party! "
            + PREFIX_PRIORITY + "High "
            + PREFIX_TASK_DATE + "2020-10-01 "
            + PREFIX_TASK_TIME + "19:00 "
            + PREFIX_TAG + "cs2103 "
            + PREFIX_TAG + "project";

    public static final String MESSAGE_EDIT_TASK_SUCCESS = "Edited Task: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the task list.";
    public static final String MESSAGE_WRONG_TYPE = "This task selected is not in the type Event.";

    private final Index index;
    private final EditEventDescriptor editEventDescriptor;

    /**
     * @param index                of the task in the filtered task list to edit
     * @param editEventDescriptor details to edit the task with
     */
    public EditEventCommand(Index index, EditEventDescriptor editEventDescriptor) {
        requireNonNull(index);
        requireNonNull(editEventDescriptor);

        this.index = index;
        this.editEventDescriptor = editEventDescriptor;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Task> lastShownList = model.getFilteredTaskList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        Task taskToEdit = lastShownList.get(index.getZeroBased());

        if (!(taskToEdit instanceof Event)) {
            throw new CommandException(MESSAGE_WRONG_TYPE);
        }

        Task editedTask = createEditedEvent((Event) taskToEdit, editEventDescriptor);

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
    public static Event createEditedEvent(Event eventToEdit, EditEventDescriptor editEventDescriptor) {
        assert eventToEdit != null;

        Title updatedTitle = editEventDescriptor.getTitle().orElse(eventToEdit.getTitle());
        Description updatedDescription = editEventDescriptor.getDescription()
                .orElse(eventToEdit.getDescription());
        Priority updatedPriority = editEventDescriptor.getPriority().orElse(eventToEdit.getPriority());
        TaskDate updatedEventDate = editEventDescriptor.getEventDate().orElse(eventToEdit.getEventDate());
        TaskTime updatedEventTime = editEventDescriptor.getEventTime().orElse(eventToEdit.getEventTime());
        Set<Tag> updatedTagList = editEventDescriptor.getTagList().orElse(eventToEdit.getTags());

        return new Event(updatedTitle, updatedDescription, updatedPriority, updatedEventDate,
                updatedEventTime, updatedTagList);
    }


    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditEventCommand)) {
            return false;
        }

        // state check
        EditEventCommand e = (EditEventCommand) other;
        return index.equals(e.index)
                && editEventDescriptor.equals(e.editEventDescriptor);
    }

    /**
     * Stores the details to edit the event task with. Each non-empty field value will replace the
     * corresponding field value of the event task.
     */
    public static class EditEventDescriptor {
        private Title title;
        private Description description;
        private Priority priority;
        private TaskDate eventDate;
        private TaskTime eventTime;
        private Set<Tag> tagList;

        public EditEventDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditEventDescriptor(EditEventDescriptor toCopy) {
            setTitle(toCopy.title);
            setDescription(toCopy.description);
            setPriority(toCopy.priority);
            setEventDate(toCopy.eventDate);
            setEventTime(toCopy.eventTime);
            setTagList(toCopy.tagList);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(title, description, priority, eventDate, eventTime, tagList);
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

        public Optional<TaskDate> getEventDate() {
            return Optional.ofNullable(eventDate);
        }

        public Optional<TaskTime> getEventTime() {
            return Optional.ofNullable(eventTime);
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

        public void setEventDate(TaskDate eventDate) {
            this.eventDate = eventDate;
        }

        public void setEventTime(TaskTime eventTime) {
            this.eventTime = eventTime;
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
            if (!(other instanceof EditEventDescriptor)) {
                return false;
            }

            // state check
            EditEventDescriptor e = (EditEventDescriptor) other;

            return getTitle().equals(e.getTitle())
                    && getDescription().equals(e.getDescription())
                    && getPriority().equals(e.getPriority())
                    && getEventDate().equals(e.getEventDate())
                    && getEventTime().equals(e.getEventTime())
                    && getTagList().equals(e.getTagList());
        }
    }

}
