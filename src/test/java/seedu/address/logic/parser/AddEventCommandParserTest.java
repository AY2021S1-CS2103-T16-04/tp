package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.DESCRIPTION_DESC_LECTURE;
import static seedu.address.logic.commands.CommandTestUtil.DESCRIPTION_DESC_PROJECT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PRIORITY_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TITLE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.PRIORITY_DESC_LECTURE;
import static seedu.address.logic.commands.CommandTestUtil.PRIORITY_DESC_PROJECT;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_LECTURE;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_PROJECT;
import static seedu.address.logic.commands.CommandTestUtil.TASKDATE_DESC_LECTURE;
import static seedu.address.logic.commands.CommandTestUtil.TASKDATE_DESC_PROJECT;
import static seedu.address.logic.commands.CommandTestUtil.TASKTIME_DESC_LECTURE;
import static seedu.address.logic.commands.CommandTestUtil.TASKTIME_DESC_PROJECT;
import static seedu.address.logic.commands.CommandTestUtil.TITLE_DESC_LECTURE;
import static seedu.address.logic.commands.CommandTestUtil.TITLE_DESC_PROJECT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_PROJECT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRIORITY_PROJECT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_LECTURE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_PROJECT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TASKDATE_PROJECT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TASKTIME_PROJECT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TITLE_PROJECT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalTasks.EVENT_PROJECT;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddEventCommand;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Event;
import seedu.address.model.task.Priority;
import seedu.address.model.task.Title;
import seedu.address.testutil.EventBuilder;

public class AddEventCommandParserTest {
    private AddEventCommandParser parser = new AddEventCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Event expectedEvent = new EventBuilder(EVENT_PROJECT).withTags(VALID_TAG_PROJECT).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + TITLE_DESC_PROJECT + DESCRIPTION_DESC_PROJECT
                + PRIORITY_DESC_PROJECT + TASKDATE_DESC_PROJECT + TASKTIME_DESC_PROJECT + TAG_DESC_PROJECT,
                new AddEventCommand(expectedEvent));

        // multiple titles - last title accepted
        assertParseSuccess(parser, TITLE_DESC_LECTURE + TITLE_DESC_PROJECT + DESCRIPTION_DESC_PROJECT
                + PRIORITY_DESC_PROJECT + TASKDATE_DESC_PROJECT + TASKTIME_DESC_PROJECT + TAG_DESC_PROJECT,
                new AddEventCommand(expectedEvent));

        // multiple descriptions - last description accepted
        assertParseSuccess(parser, TITLE_DESC_PROJECT + DESCRIPTION_DESC_LECTURE + DESCRIPTION_DESC_PROJECT
                + PRIORITY_DESC_PROJECT + TASKDATE_DESC_PROJECT + TASKTIME_DESC_PROJECT + TAG_DESC_PROJECT,
                new AddEventCommand(expectedEvent));

        // multiple priorities - last priority accepted
        assertParseSuccess(parser, TITLE_DESC_PROJECT + DESCRIPTION_DESC_PROJECT + PRIORITY_DESC_LECTURE
                + PRIORITY_DESC_PROJECT + TASKDATE_DESC_PROJECT + TASKTIME_DESC_PROJECT + TAG_DESC_PROJECT,
                new AddEventCommand(expectedEvent));

        // multiple dates - last date accepted
        assertParseSuccess(parser, TITLE_DESC_PROJECT + DESCRIPTION_DESC_PROJECT + PRIORITY_DESC_PROJECT
                + TASKDATE_DESC_LECTURE + TASKDATE_DESC_PROJECT + TASKTIME_DESC_PROJECT + TAG_DESC_PROJECT,
                new AddEventCommand(expectedEvent));

        // multiple times - last time accepted
        assertParseSuccess(parser, TITLE_DESC_PROJECT + DESCRIPTION_DESC_PROJECT + PRIORITY_DESC_PROJECT
                + TASKDATE_DESC_PROJECT + TASKTIME_DESC_LECTURE + TASKTIME_DESC_PROJECT + TAG_DESC_PROJECT,
                new AddEventCommand(expectedEvent));

        // multiple tags - all accepted
        Event expectedEventWithMultipleTags = new EventBuilder(EVENT_PROJECT)
                .withTags(VALID_TAG_PROJECT, VALID_TAG_LECTURE).build();
        assertParseSuccess(parser, TITLE_DESC_PROJECT + DESCRIPTION_DESC_PROJECT + PRIORITY_DESC_PROJECT
                + TASKDATE_DESC_PROJECT + TASKTIME_DESC_PROJECT + TAG_DESC_PROJECT + TAG_DESC_LECTURE,
                new AddEventCommand(expectedEventWithMultipleTags));
    }

    // TODO: Add tests for optional fields: parse_optionalFieldsMissing_success()

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEventCommand.MESSAGE_USAGE);

        // missing title prefix
        assertParseFailure(parser, VALID_TITLE_PROJECT + DESCRIPTION_DESC_PROJECT + PRIORITY_DESC_PROJECT
                + TASKDATE_DESC_PROJECT + TASKTIME_DESC_PROJECT + TAG_DESC_PROJECT, expectedMessage);

        // missing description prefix
        // description is optional as of v1.3

        // missing priority prefix
        // priority is optional as of v1.3

        // missing date prefix
        assertParseFailure(parser, TITLE_DESC_PROJECT + DESCRIPTION_DESC_PROJECT + PRIORITY_DESC_PROJECT
                + VALID_TASKDATE_PROJECT + TASKTIME_DESC_PROJECT + TAG_DESC_PROJECT, expectedMessage);

        // missing time prefix
        assertParseFailure(parser, TITLE_DESC_PROJECT + DESCRIPTION_DESC_PROJECT + PRIORITY_DESC_PROJECT
                + TASKDATE_DESC_PROJECT + VALID_TASKTIME_PROJECT + TAG_DESC_PROJECT, expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_TITLE_PROJECT + VALID_DESCRIPTION_PROJECT + VALID_PRIORITY_PROJECT
                + VALID_TASKDATE_PROJECT + VALID_TASKTIME_PROJECT + VALID_TAG_PROJECT, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid title
        assertParseFailure(parser, INVALID_TITLE_DESC + DESCRIPTION_DESC_PROJECT + PRIORITY_DESC_PROJECT
                + TASKDATE_DESC_PROJECT + TASKTIME_DESC_PROJECT + TAG_DESC_PROJECT, Title.MESSAGE_CONSTRAINTS);

        // invalid description
        // descriptions are optional as of v1.3

        // invalid priority
        assertParseFailure(parser, TITLE_DESC_PROJECT + DESCRIPTION_DESC_PROJECT + INVALID_PRIORITY_DESC
                + TASKDATE_DESC_PROJECT + TASKTIME_DESC_PROJECT + TAG_DESC_PROJECT, Priority.MESSAGE_CONSTRAINTS);

        // TODO: Fix Invalid DateTime Checks.

        // invalid date
        // assertParseFailure(parser, TITLE_DESC_PROJECT + DESCRIPTION_DESC_PROJECT + PRIORITY_DESC_PROJECT
        //         + INVALID_TASKDATE_DESC + TASKTIME_DESC_PROJECT + TAG_DESC_PROJECT, TaskDate.MESSAGE_CONSTRAINTS);

        // invalid time
        // assertParseFailure(parser, TITLE_DESC_PROJECT + DESCRIPTION_DESC_PROJECT + PRIORITY_DESC_PROJECT
        //         + TASKDATE_DESC_PROJECT + INVALID_TASKTIME_DESC + TAG_DESC_PROJECT, TaskTime.MESSAGE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, TITLE_DESC_PROJECT + DESCRIPTION_DESC_PROJECT + PRIORITY_DESC_PROJECT
                + TASKDATE_DESC_PROJECT + TASKTIME_DESC_PROJECT + INVALID_TAG_DESC, Tag.MESSAGE_CONSTRAINTS);

        // two invalid values, only first value reported
        assertParseFailure(parser, INVALID_TITLE_DESC + INVALID_PRIORITY_DESC
                + TASKDATE_DESC_PROJECT + TASKTIME_DESC_PROJECT + TAG_DESC_PROJECT, Title.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + TITLE_DESC_PROJECT + DESCRIPTION_DESC_PROJECT
                        + PRIORITY_DESC_PROJECT + TASKDATE_DESC_PROJECT + TASKTIME_DESC_PROJECT + TAG_DESC_PROJECT,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEventCommand.MESSAGE_USAGE));
    }
}
