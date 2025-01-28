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
    void checkEntryDateValidReasonable() {
        entry.setField(StandardField.DATE, "1984-12-16");
        assertEquals(Optional.of("1984-12-16"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEqualTo(Optional.of("1984"), Optional.of("#dec#"), Optional.of("16"));
    }

    @Test
    void checkEntryDateValidWithWhitespece() {
        entry.setField(StandardField.DATE, "1984 - 12 - 16");
        assertEquals(Optional.of("1984 - 12 - 16"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEmpty();
    }

    @Test
    void checkEntryDateValidNoDay() {
        entry.setField(StandardField.DATE, "1984-12");
        assertEquals(Optional.of("1984-12"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEqualTo(Optional.of("1984"), Optional.of("#dec#"), Optional.empty());
    }

    @Test
    void checkEntryDateValidNoDayNoMonth() {
        entry.setField(StandardField.DATE, "1984");
        assertEquals(Optional.of("1984"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEqualTo(Optional.of("1984"), Optional.empty(), Optional.empty());
    }

    @Test
    void checkEntryDateWithDateNonNumImproperFormat() {
        entry.setField(StandardField.DATE, "abcabc");
        assertEquals(Optional.of("abcabc"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEmpty();
        entry.setField(StandardField.DATE, "&$#*8HDUu");
        assertEquals(Optional.of("&$#*8HDUu"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEmpty();
    }

    @Test
    void checkEntryDateInvalidDate() {
        entry.setField(StandardField.DATE, "2024-02-30");
        assertEquals(Optional.of("2024-02-30"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEmpty();
    }

    @Test
    void checkEntryDateWithYearTripleDigit() {
        entry.setField(StandardField.DATE, "999-12-20");
        assertEquals(Optional.of("999-12-20"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEmpty();
    }

    @Test
    void checkEntryDateWithYearZero() {
        entry.setField(StandardField.DATE, "0-10-16");
        assertEquals(Optional.of("0-10-16"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEmpty();
    }

    @Test
    void checkEntryDateWithYearNegative() {
        entry.setField(StandardField.DATE, "-500-10-16");
        assertEquals(Optional.of("-500-10-16"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEmpty();
    }

    @Test
    void checkEntryDateWithYearFractional() {
        entry.setField(StandardField.DATE, "2024.5-02-30");
        assertEquals(Optional.of("2024.5-02-30"), entry.getFieldOrAlias(StandardField.DATE));
        this.isYearMonthDayEmpty();
    }

    @Test
    void checkEntryDateWithYearNonNum() {
        entry.setField(StandardField.DATE, "abCD-10-To");
        assertEquals(Optional.of("abCD-10-To"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEmpty();
        entry.setField(StandardField.DATE, "5!@3-10-$");
        assertEquals(Optional.of("5!@3-10-$"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEmpty();
    }

    @Test
    void checkEntryDateWithMonthNonNum() {
        entry.setField(StandardField.DATE, "1984-A9-16");
        assertEquals(Optional.of("1984-A9-16"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEmpty();
        entry.setField(StandardField.DATE, "1984-#-16");
        assertEquals(Optional.of("1984-#-16"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEmpty();
    }

    @Test
    void checkEntryDateWithMonthZeroPadded() {
        entry.setField(StandardField.DATE, "1984-09-16");
        assertEquals(Optional.of("1984-09-16"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEqualTo(Optional.of("1984"), Optional.of("#sep#"), Optional.of("16"));
    }

    @Test
    void checkEntryDateWithMonthZeroUnpadded() {
        entry.setField(StandardField.DATE, "1984-9-16");
        assertEquals(Optional.of("1984-9-16"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEqualTo(Optional.of("1984"), Optional.of("#sep#"), Optional.of("16"));
    }

    @Test
    void checkEntryDateWithMonthZero() {
        entry.setField(StandardField.DATE, "2015-0-22");
        assertEquals(Optional.of("2015-0-22"), entry.getFieldOrAlias(StandardField.DATE));
        this.isYearMonthDayEmpty();
    }

    @Test
    void checkEntryDateWithMonthNegative() {
        entry.setField(StandardField.DATE, "2015--1-22");
        assertEquals(Optional.of("2015--1-22"), entry.getFieldOrAlias(StandardField.DATE));
        this.isYearMonthDayEmpty();
    }

    @Test
    void checkEntryDateWithMonthGreaterThan12() {
        entry.setField(StandardField.DATE, "2005-13-21");
        assertEquals(Optional.of("2005-13-21"), entry.getFieldOrAlias(StandardField.DATE));
        this.isYearMonthDayEmpty();
    }

    @Test
    void checkEntryDateWithMonthFractional() {
        entry.setField(StandardField.DATE, "1999-0.5-18");
        assertEquals(Optional.of("1999-0.5-18"), entry.getFieldOrAlias(StandardField.DATE));
        this.isYearMonthDayEmpty();
    }

    @Test
    void checkEntryDateWithMonthString() {
        entry.setField(StandardField.DATE, "1989-#may#-16");
        assertEquals(Optional.of("1989-#may#-16"), entry.getFieldOrAlias(StandardField.DATE));
        this.isYearMonthDayEmpty();
        entry.setField(StandardField.DATE, "1989-may-16");
        assertEquals(Optional.of("1989-may-16"), entry.getFieldOrAlias(StandardField.DATE));
        this.isYearMonthDayEmpty();
    }

    @Test
    void checkEntryDateWithDayNonNum() {
        entry.setField(StandardField.DATE, "1984-10-To");
        assertEquals(Optional.of("1984-10-To"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEmpty();
        entry.setField(StandardField.DATE, "1984-10-$");
        assertEquals(Optional.of("1984-10-$"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEmpty();
    }

    @Test
    void checkEntryDateWithDayZeroPadded() {
        entry.setField(StandardField.DATE, "1984-09-05");
        assertEquals(Optional.of("1984-09-05"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEqualTo(Optional.of("1984"), Optional.of("#sep#"), Optional.of("5"));
    }

    @Test
    void checkEntryDateWithDayZeroUnpadded() {
        entry.setField(StandardField.DATE, "1984-09-5");
        assertEquals(Optional.of("1984-09-5"), entry.getFieldOrAlias(StandardField.DATE));
        isYearMonthDayEqualTo(Optional.of("1984"), Optional.of("#sep#"), Optional.of("5"));
    }

    @Test
    void checkEntryDateWithDayZero() {
        entry.setField(StandardField.DATE, "2015-10-0");
        assertEquals(Optional.of("2015-10-0"), entry.getFieldOrAlias(StandardField.DATE));
        this.isYearMonthDayEmpty();
    }

    @Test
    void checkEntryDateWithDayNegative() {
        entry.setField(StandardField.DATE, "2015-10--1");
        assertEquals(Optional.of("2015-10--1"), entry.getFieldOrAlias(StandardField.DATE));
        this.isYearMonthDayEmpty();
    }

    @Test
    void checkEntryDateWithDayGreaterThan31() {
        entry.setField(StandardField.DATE, "2024-05-32");
        assertEquals(Optional.of("2024-05-32"), entry.getFieldOrAlias(StandardField.DATE));
        this.isYearMonthDayEmpty();
    }

    @Test
    void checkEntryDateWithDayFractional() {
        entry.setField(StandardField.DATE, "1999-11-.5");
        assertEquals(Optional.of("1999-11-.5"), entry.getFieldOrAlias(StandardField.DATE));
        this.isYearMonthDayEmpty();
    }

    void isYearMonthDayEqualTo(Optional op_year, Optional op_mon, Optional op_day) {
        assertEquals(op_year, entry.getFieldOrAlias(StandardField.YEAR));
        assertEquals(op_mon, entry.getFieldOrAlias(StandardField.MONTH));
        assertEquals(op_day, entry.getFieldOrAlias(StandardField.DAY));
    }

    void isYearMonthDayEmpty() {
        assertEquals(Optional.empty(), entry.getFieldOrAlias(StandardField.YEAR));
        assertEquals(Optional.empty(), entry.getFieldOrAlias(StandardField.MONTH));
        assertEquals(Optional.empty(), entry.getFieldOrAlias(StandardField.DAY));
    }
}
