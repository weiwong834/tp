package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PROJECT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Arrays;
import java.util.function.Predicate;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;
import seedu.address.model.person.ProjectContainsKeywordsPredicate;
import seedu.address.model.person.TagContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FilterCommand object
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FilterCommand
     * and returns a FilterCommand object for execution.
     * @param args the input argument
     * @return  a FilterCommand object for execution
     * @throws ParseException if the user input does not conform the expected format
     */
    public FilterCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argumentMultimap = ArgumentTokenizer.tokenize(args, PREFIX_PROJECT, PREFIX_TAG);

        if (!hasRequiredPrefix(argumentMultimap)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        argumentMultimap.verifyNoDuplicatePrefixesFor(PREFIX_PROJECT, PREFIX_TAG);

        return new FilterCommand(getStudentPredicate(argumentMultimap));
    }

    private Predicate<Person> getStudentPredicate(ArgumentMultimap argumentMultimap)
            throws ParseException {
        String[] projectKeywords;
        if (argumentMultimap.getValue(PREFIX_PROJECT).isPresent()) {
            checkFilterCondition(argumentMultimap, PREFIX_PROJECT);
            projectKeywords = argumentMultimap.getValue(PREFIX_PROJECT).get().split("\\s+");
            return new ProjectContainsKeywordsPredicate(Arrays.asList(projectKeywords));
        }

        if (argumentMultimap.getValue(PREFIX_TAG).isPresent()) {
            checkFilterCondition(argumentMultimap, PREFIX_TAG);
            projectKeywords = argumentMultimap.getValue(PREFIX_TAG).get().split("\\s+");
            return new TagContainsKeywordsPredicate(Arrays.asList(projectKeywords));
        }
        return null;
    }

    private void checkFilterCondition(ArgumentMultimap argumentMultimap, Prefix prefix) throws ParseException {
        if (argumentMultimap.getValue(prefix == PREFIX_PROJECT ? PREFIX_TAG : PREFIX_PROJECT).isPresent()) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_ONLY_PROJECT_OR_TAG
            ));
        }
        if (argumentMultimap.getValue(prefix).isEmpty()
                || !argumentMultimap.getPreamble().isEmpty()
                || argumentMultimap.getValue(prefix).get().isEmpty()) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE
            ));
        }
    }

    private boolean hasRequiredPrefix(ArgumentMultimap argumentMultimap) {
        return !argumentMultimap.getAllValues(PREFIX_TAG).isEmpty()
                || !argumentMultimap.getAllValues(PREFIX_PROJECT).isEmpty();
    }
}

