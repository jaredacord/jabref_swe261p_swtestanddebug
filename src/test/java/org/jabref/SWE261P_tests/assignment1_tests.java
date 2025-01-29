package org.jabref.SWE261P_tests;

import java.util.Optional;

import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.field.StandardField;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;

@Execution(CONCURRENT)
public class assignment1_tests {
    private BibEntry entry = new BibEntry();

    @Test
    void checkEntryDateDDEmpty() {
        entry.setField(StandardField.DATE, "1984-12");
        assertEquals(Optional.of("1984-12"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEqualTo(Optional.of("1984"), Optional.of("#dec#"), Optional.empty());
    }

    @Test
    void checkEntryDateDDNonNum() {
        entry.setField(StandardField.DATE, "1984-12-a9");
        assertEquals(Optional.of("1984-12-a9"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEmpty();
        entry.setField(StandardField.DATE, "1984-12-5h&8K");
        assertEquals(Optional.of("1984-12-5h&8K"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEmpty();
    }

    @Test
    void checkEntryDateDDLessThan1() {
        entry.setField(StandardField.DATE, "1984-12-0");
        assertEquals(Optional.of("1984-12-0"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEmpty();
        entry.setField(StandardField.DATE, "1984-12--1");
        assertEquals(Optional.of("1984-12--1"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEmpty();
    }

    @Test
    void checkEntryDateDDValidUnder10With0() {
        entry.setField(StandardField.DATE, "1984-12-05");
        assertEquals(Optional.of("1984-12-05"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEqualTo(Optional.of("1984"), Optional.of("#dec#"), Optional.of("5"));
    }

    @Test
    void checkEntryDateDDValidUnder10No0() {
        entry.setField(StandardField.DATE, "1984-12-5");
        assertEquals(Optional.of("1984-12-5"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEqualTo(Optional.of("1984"), Optional.of("#dec#"), Optional.of("5"));
    }

    @Test
    void checkEntryDateDDValidDoubleDigit() {
        entry.setField(StandardField.DATE, "1984-12-21");
        assertEquals(Optional.of("1984-12-21"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEqualTo(Optional.of("1984"), Optional.of("#dec#"), Optional.of("21"));
    }

    @Test
    void checkEntryDateDDGreaterThan31() {
        entry.setField(StandardField.DATE, "1984-12-32");
        assertEquals(Optional.of("1984-12-32"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEmpty();
    }

    @Test
    void checkEntryDateMMEmpty() {
        entry.setField(StandardField.DATE, "1984");
        assertEquals(Optional.of("1984"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEqualTo(Optional.of("1984"), Optional.empty(), Optional.empty());
    }

    @Test
    void checkEntryDateMMNonNum() {
        entry.setField(StandardField.DATE, "2005-A1-12");
        assertEquals(Optional.of("2005-A1-12"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEmpty();
        entry.setField(StandardField.DATE, "2005-W#4f*-12");
        assertEquals(Optional.of("2005-W#4f*-12"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEmpty();
    }

    @Test
    void checkEntryDateMMLessThan1() {
        entry.setField(StandardField.DATE, "1984-0-10");
        assertEquals(Optional.of("1984-0-10"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEmpty();
        entry.setField(StandardField.DATE, "1984--1-10");
        assertEquals(Optional.of("1984--1-10"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEmpty();
    }

    @Test
    void checkEntryDateMMValidUnder10With0() {
        entry.setField(StandardField.DATE, "2022-03");
        assertEquals(Optional.of("2022-03"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEqualTo(Optional.of("2022"), Optional.of("#mar#"), Optional.empty());
    }

    @Test
    void checkEntryDateMMValidUnder10No0() {
        entry.setField(StandardField.DATE, "2022-3-21");
        assertEquals(Optional.of("2022-3-21"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEqualTo(Optional.of("2022"), Optional.of("#mar#"), Optional.of("21"));
    }

    @Test
    void checkEntryDateMMValidDoubleDigit() {
        entry.setField(StandardField.DATE, "1984-11");
        assertEquals(Optional.of("1984-11"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEqualTo(Optional.of("1984"), Optional.of("#nov#"), Optional.empty());
    }

    @Test
    void checkEntryDateMMGreaterThan12() {
        entry.setField(StandardField.DATE, "1984-13-28");
        assertEquals(Optional.of("1984-13-28"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEmpty();
    }

    @Test
    void checkEntryDateYYYYEmpty() {
        entry.setField(StandardField.DATE, "");
        assertEquals(Optional.empty(), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEmpty();
    }

    @Test
    void checkEntryDateYYYYNonNum() {
        entry.setField(StandardField.DATE, "W#4f*-12-21");
        assertEquals(Optional.of("W#4f*-12-21"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEmpty();
        entry.setField(StandardField.DATE, "h6&klt");
        assertEquals(Optional.of("h6&klt"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEmpty();
    }

    @Test
    void checkEntryDateYYYYLessThan1000() {
        entry.setField(StandardField.DATE, "-98");
        assertEquals(Optional.of("-98"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEmpty();
        entry.setField(StandardField.DATE, "999-12-1");
        assertEquals(Optional.of("999-12-1"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEmpty();
    }

    @Test
    void checkEntryDateHolisticValidDate() {
        entry.setField(StandardField.DATE, "2005-11-22");
        assertEquals(Optional.of("2005-11-22"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEqualTo(Optional.of("2005"), Optional.of("#nov#"), Optional.of("22"));
    }

    @Test
    void checkEntryDateHolisticNonExistentDate() {
        entry.setField(StandardField.DATE, "1984-02-30");
        assertEquals(Optional.of("1984-02-30"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEmpty();
    }

    @Test
    void checkEntryDateHolisticFutureDate() {
        entry.setField(StandardField.DATE, "2050-01-28");
        assertEquals(Optional.of("2050-01-28"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEqualTo(Optional.of("2050"), Optional.of("#jan#"), Optional.of("28"));
    }

    void isYearMonthDayEqualTo(Optional op_year, Optional op_mon, Optional op_day) {
        assertEquals(op_year, entry.getFieldOrAlias(StandardField.YEAR));
        assertEquals(op_mon, entry.getFieldOrAlias(StandardField.MONTH));
        assertEquals(op_day, entry.getFieldOrAlias(StandardField.DAY));
    }

    void isYearMonthDayEmpty() {
        assertEquals(Optional.empty(), entry.getFieldOrAlias(StandardField.DAY));
        assertEquals(Optional.empty(), entry.getFieldOrAlias(StandardField.MONTH));
        assertEquals(Optional.empty(), entry.getFieldOrAlias(StandardField.YEAR));
    }
}
