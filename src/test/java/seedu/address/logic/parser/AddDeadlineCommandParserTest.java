package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.DESCRIPTION_DESC_LECTURE;
import static seedu.address.logic.commands.CommandTestUtil.DESCRIPTION_DESC_PROJECT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DESCRIPTION_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PRIORITY_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TASKDATE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TITLE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.PRIORITY_DESC_LECTURE;
import static seedu.address.logic.commands.CommandTestUtil.PRIORITY_DESC_PROJECT;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_LECTURE;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_PROJECT;
import static seedu.address.logic.commands.CommandTestUtil.TASKDATE_DESC_LECTURE;
import static seedu.address.logic.commands.CommandTestUtil.TASKDATE_DESC_PROJECT;
import static seedu.address.logic.commands.CommandTestUtil.TITLE_DESC_LECTURE;
import static seedu.address.logic.commands.CommandTestUtil.TITLE_DESC_PROJECT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_PROJECT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRIORITY_PROJECT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_LECTURE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_PROJECT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TASKDATE_PROJECT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TITLE_PROJECT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalTasks.DEADLINE_PROJECT;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddDeadlineCommand;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Description;
import seedu.address.model.task.Priority;
import seedu.address.model.task.TaskDate;
import seedu.address.model.task.Title;
import seedu.address.testutil.DeadlineBuilder;

public class AddDeadlineCommandParserTest {
    private AddDeadlineCommandParser parser = new AddDeadlineCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Deadline expectedDeadline = new DeadlineBuilder(DEADLINE_PROJECT).withTags(VALID_TAG_PROJECT).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + TITLE_DESC_PROJECT + DESCRIPTION_DESC_PROJECT
                + PRIORITY_DESC_PROJECT + TASKDATE_DESC_PROJECT + TAG_DESC_PROJECT,
                new AddDeadlineCommand(expectedDeadline));

        // multiple titles - last title accepted
        assertParseSuccess(parser, TITLE_DESC_LECTURE + TITLE_DESC_PROJECT + DESCRIPTION_DESC_PROJECT
                + PRIORITY_DESC_PROJECT + TASKDATE_DESC_PROJECT + TAG_DESC_PROJECT,
                new AddDeadlineCommand(expectedDeadline));

        // multiple descriptions - last description accepted
        assertParseSuccess(parser, TITLE_DESC_PROJECT + DESCRIPTION_DESC_LECTURE + DESCRIPTION_DESC_PROJECT
                + PRIORITY_DESC_PROJECT + TASKDATE_DESC_PROJECT + TAG_DESC_PROJECT,
                new AddDeadlineCommand(expectedDeadline));

        // multiple priorities - last priority accepted
        assertParseSuccess(parser, TITLE_DESC_PROJECT + DESCRIPTION_DESC_PROJECT + PRIORITY_DESC_LECTURE
                + PRIORITY_DESC_PROJECT + TASKDATE_DESC_PROJECT + TAG_DESC_PROJECT,
                new AddDeadlineCommand(expectedDeadline));

        // multiple dates - last date accepted
        assertParseSuccess(parser, TITLE_DESC_PROJECT + DESCRIPTION_DESC_PROJECT + PRIORITY_DESC_PROJECT
                + TASKDATE_DESC_LECTURE + TASKDATE_DESC_PROJECT + TAG_DESC_PROJECT,
                new AddDeadlineCommand(expectedDeadline));

        // multiple tags - all accepted
        Deadline expectedDeadlineWithMultipleTags = new DeadlineBuilder(DEADLINE_PROJECT)
                .withTags(VALID_TAG_PROJECT, VALID_TAG_LECTURE).build();
        assertParseSuccess(parser, TITLE_DESC_PROJECT + DESCRIPTION_DESC_PROJECT + PRIORITY_DESC_PROJECT
                + TASKDATE_DESC_PROJECT + TAG_DESC_PROJECT + TAG_DESC_LECTURE,
                new AddDeadlineCommand(expectedDeadlineWithMultipleTags));
    }

    // TODO: Add tests for optional fields: parse_optionalFieldsMissing_success()

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddDeadlineCommand.MESSAGE_USAGE);

        // missing title prefix
        assertParseFailure(parser, VALID_TITLE_PROJECT + DESCRIPTION_DESC_PROJECT + PRIORITY_DESC_PROJECT
                + TASKDATE_DESC_PROJECT + TAG_DESC_PROJECT, expectedMessage);

        // missing description prefix
        // description is optional as of v1.3

        // missing priority prefix
        // priority is optional as of v1.3

        // missing date prefix
        assertParseFailure(parser, TITLE_DESC_PROJECT + DESCRIPTION_DESC_PROJECT + PRIORITY_DESC_PROJECT
                + VALID_TASKDATE_PROJECT + TAG_DESC_PROJECT, expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_TITLE_PROJECT + VALID_DESCRIPTION_PROJECT + VALID_PRIORITY_PROJECT
                + VALID_TASKDATE_PROJECT + VALID_TAG_PROJECT, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid title
        assertParseFailure(parser, INVALID_TITLE_DESC + DESCRIPTION_DESC_PROJECT + PRIORITY_DESC_PROJECT
                + TASKDATE_DESC_PROJECT + TAG_DESC_PROJECT, Title.MESSAGE_CONSTRAINTS);

        // invalid description
        assertParseFailure(parser, TITLE_DESC_PROJECT + INVALID_DESCRIPTION_DESC + PRIORITY_DESC_PROJECT
                + TASKDATE_DESC_PROJECT + TAG_DESC_PROJECT, Description.MESSAGE_CONSTRAINTS);

        // invalid priority
        assertParseFailure(parser, TITLE_DESC_PROJECT + DESCRIPTION_DESC_PROJECT + INVALID_PRIORITY_DESC
                + TASKDATE_DESC_PROJECT + TAG_DESC_PROJECT, Priority.MESSAGE_CONSTRAINTS);

        // invalid date
        assertParseFailure(parser, TITLE_DESC_PROJECT + DESCRIPTION_DESC_PROJECT + PRIORITY_DESC_PROJECT
                + INVALID_TASKDATE_DESC + TAG_DESC_PROJECT, TaskDate.MESSAGE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, TITLE_DESC_PROJECT + DESCRIPTION_DESC_PROJECT + PRIORITY_DESC_PROJECT
                + TASKDATE_DESC_PROJECT + INVALID_TAG_DESC, Tag.MESSAGE_CONSTRAINTS);

        // two invalid values, only first value reported
        assertParseFailure(parser, INVALID_TITLE_DESC + INVALID_PRIORITY_DESC
                + TASKDATE_DESC_PROJECT + TAG_DESC_PROJECT, Title.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + TITLE_DESC_PROJECT + DESCRIPTION_DESC_PROJECT
                        + PRIORITY_DESC_PROJECT + TASKDATE_DESC_PROJECT + TAG_DESC_PROJECT,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddDeadlineCommand.MESSAGE_USAGE));
    }
}
