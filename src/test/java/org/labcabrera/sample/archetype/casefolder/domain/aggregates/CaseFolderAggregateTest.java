package org.labcabrera.sample.archetype.casefolder.domain.aggregates;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.labcabrera.sample.archetype.casefolder.application.cqrs.commands.CreateCaseFolderCommand;
import org.labcabrera.sample.archetype.casefolder.domain.valueobjects.IdCardType;

public class CaseFolderAggregateTest {

    @Test
    void test() {
        CreateCaseFolderCommand command = new CreateCaseFolderCommand(
            "John",
            "Doe",
            "Smith",
            IdCardType.NIF,
            "12345678A",
            "johndoe",
            Set.of("USER"));
        CaseFolderAggregate aggregate = new CaseFolderAggregate(command);
        assertNotNull(aggregate.getId());
        assert aggregate.getName().equals("John");
    }
}
