package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PROJECT_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalProfiler;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.testutil.PersonBuilder;

public class ProfilerTest {

    private final Profiler profiler = new Profiler();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), profiler.getPersonList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> profiler.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyProfiler_replacesData() {
        Profiler newData = getTypicalProfiler();
        profiler.resetData(newData);
        assertEquals(newData, profiler);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        // Two persons with the same identity fields
        Person editedAlice = new PersonBuilder(ALICE).withProject(VALID_PROJECT_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        List<Person> newPersons = Arrays.asList(ALICE, editedAlice);
        ProfilerStub newData = new ProfilerStub(newPersons);

        assertThrows(DuplicatePersonException.class, () -> profiler.resetData(newData));
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> profiler.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInProfiler_returnsFalse() {
        assertFalse(profiler.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInProfiler_returnsTrue() {
        profiler.addPerson(ALICE);
        assertTrue(profiler.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personWithSameIdentityFieldsInProfiler_returnsTrue() {
        profiler.addPerson(ALICE);
        Person editedAlice = new PersonBuilder(ALICE).withProject(VALID_PROJECT_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(profiler.hasPerson(editedAlice));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> profiler.getPersonList().remove(0));
    }

    @Test
    public void toStringMethod() {
        String expected = Profiler.class.getCanonicalName() + "{persons=" + profiler.getPersonList() + "}";
        assertEquals(expected, profiler.toString());
    }

    /**
     * A stub ReadOnlyProfiler whose persons list can violate interface constraints.
     */
    private static class ProfilerStub implements ReadOnlyProfiler {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();

        ProfilerStub(Collection<Person> persons) {
            this.persons.setAll(persons);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }
    }

}
