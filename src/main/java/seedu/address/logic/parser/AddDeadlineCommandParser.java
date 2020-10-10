package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRIORITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TASK_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TITLE;

import java.util.Set;
import java.util.stream.Stream;

import seedu.address.logic.commands.AddDeadlineCommand;
import seedu.address.logic.commands.AddTodoCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Description;
import seedu.address.model.task.Priority;
import seedu.address.model.task.TaskDate;
import seedu.address.model.task.Title;

/**
 * Parses input arguments and creates a new AddDeadlineCommand object
 * */
public class AddDeadlineCommandParser implements Parser<AddDeadlineCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddDeadlineCommand
     * and returns a AddDeadlineCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddDeadlineCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_TITLE, PREFIX_DESCRIPTION, PREFIX_PRIORITY,
                        PREFIX_TASK_DATE, PREFIX_TAG);

        if (!arePrefixesPresent(argMultimap, PREFIX_TITLE, PREFIX_DESCRIPTION, PREFIX_PRIORITY,
                PREFIX_TASK_DATE, PREFIX_TAG)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddTodoCommand.MESSAGE_USAGE));
        }

        Title title = ParserUtil.parseTitle(argMultimap.getValue(PREFIX_TITLE).get());
        Description description = ParserUtil.parseDescription(argMultimap.getValue(PREFIX_DESCRIPTION).get());
        Priority priority = ParserUtil.parsePriority(argMultimap.getValue(PREFIX_PRIORITY).get());
        TaskDate taskDeadline = ParserUtil.parseTaskDate(argMultimap.getValue(PREFIX_TASK_DATE).get());
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        Deadline dl = new Deadline(title, description, priority, taskDeadline, tagList);

        return new AddDeadlineCommand(dl);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
