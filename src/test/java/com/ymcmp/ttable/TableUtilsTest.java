package com.ymcmp.ttable;

import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.LinkedHashMap;

import org.junit.Test;

import static com.ymcmp.ttable.border.Border.ASCII_BORDER;

import static org.junit.Assert.*;

public class TableUtilsTest {

    @Test
    public void fromArrayHandlesNull() {
        final Object[][] data = {
            {"A", "B", "C"},
            {1, 2, 3},
            {1.0, 'q', null}
        };
        final TableBuilder fmt = TableUtils.fromArray(data);
        assertEquals(3, fmt.rows);
        assertEquals(3, fmt.columns);
        assertEquals(
                " A  B  C  \n" +
                " 1  2  3  \n" +
                "1.0 q null", fmt.align().toString());
    }

    @Test
    public void fromArrayPadsArray() {
        final Object[][] data = {
            {"A", "B", "C"},
            {1, 2, 3},
            {1.0, 'q'}
        };
        final TableBuilder fmt = TableUtils.fromArray(data);
        assertEquals(3, fmt.rows);
        assertEquals(3, fmt.columns);
        assertEquals(
                " A  B C\n" +
                " 1  2 3\n" +
                "1.0 q  ", fmt.align().toString());
    }

    @Test
    public void fromArrayWorksWithEmptyArrays() {
        final Object[][] data = {};
        final TableBuilder fmt = TableUtils.fromArray(data);
        assertEquals(0, fmt.rows);
        assertEquals(0, fmt.columns);
        assertEquals(
            "", fmt.align().toString());
    }

    @Test
    public void fromMapMakesTableWithTwoRows() {
        final Map<String, Integer> data = new LinkedHashMap<>();
        data.put("Foo", 1);
        data.put("Bar", -8);
        data.put("Baz", 10);
        final TableBuilder fmt = TableUtils.fromMap(data);
        assertEquals(2, fmt.rows);
        assertEquals(3, fmt.columns);
        assertEquals(
                "Foo Bar Baz\n" +
                " 1  -8  10 ", fmt.align().toString());
    }

    @Test
    public void fromMultimapMakesTableWithNRows() {
        final Map<String, String[]> data = new LinkedHashMap<>();
        data.put("Foo", new String[]{ "F", "o" });
        data.put("Bar", new String[]{ "B", "a", "r" });
        data.put("Baz", new String[]{ "z" });
        final TableBuilder fmt = TableUtils.fromMultimap(data, Arrays::asList);
        assertEquals(4, fmt.rows);
        assertEquals(3, fmt.columns);
        assertEquals(
                "Foo Bar Baz\n" +
                " F   B   z \n" +
                " o   a     \n" +
                "     r     " , fmt.align().toString());
    }

    @Test
    public void fromTableHasDataAsRows() {
        final Person joe = new Person("Joe", 20, false);
        final Person leo = new Person("Leo", 19, true);
        final Person tim = new Person("Tim", 38, false);

        final TableFormatter fmt = TableUtils
                .fromTable(Person.fieldNames(), Arrays.asList(joe, leo, tim), Person::collect)
                .align(TableBuilderTest.LEFT_CENTER_ALIGN);

        assertEquals(
                "name age student?\n" +
                "Joe  20  false   \n" +
                "Leo  19  true    \n" +
                "Tim  38  false   ", fmt.toString());
    }
}

final class Person {

    public final String name;
    public final int age;
    public final boolean student;

    public Person(String name, int age, boolean student) {
        this.name = name;
        this.age = age;
        this.student = student;
    }

    public static List<String> fieldNames() {
        return Arrays.asList("name", "age", "student?");
    }

    public List<String> collect() {
        return Arrays.asList(name, Integer.toString(age), Boolean.toString(student));
    }
}