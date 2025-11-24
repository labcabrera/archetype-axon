package org.labcabrera.sample.archetype.casefolder.domain.valueobjects;

import org.junit.jupiter.api.Test;

public class IdCardTest {

    @Test
    void testEquals() {
        IdCard idCard1 = new IdCard(IdCardType.NIF, "12345678A");
        IdCard idCard2 = new IdCard(IdCardType.NIF, "12345678A");
        IdCard idCard3 = new IdCard(IdCardType.PASSPORT, "12345678A");
        assert idCard1.equals(idCard2);
        assert !idCard1.equals(idCard3);
    }

    @Test
    void testIdCardNumber() {

    }

    @Test
    void testIdCardType() {

    }

    @Test
    void testToString() {

    }
}
