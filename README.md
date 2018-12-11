# TTables [![Build Status](https://www.travis-ci.org/plankp/ttables.svg?branch=master)](https://www.travis-ci.org/plankp/ttables)

Text table generator in java.

## How to include?

Follow the instructions from [jitpack](https://jitpack.io/#plankp/ttables).

## Build Instructions

`./gradlew build`, requires minimum java 8.

## Examples

If the data is a 2D+ array or subtype of Map, [`TableBuilder`](https://github.com/plankp/ttables/blob/master/src/main/java/com/ymcmp/ttable/TableBuilder.java)s can be generated using static methods in [`TableUtils`](https://github.com/plankp/ttables/blob/master/src/main/java/com/ymcmp/ttable/TableUtils.java)

Using the [`TableBuilder`](https://github.com/plankp/ttables/blob/master/src/main/java/com/ymcmp/ttable/TableBuilder.java) directly is also an option.

Once the builder is created, call the respected align method on it (with the desired cell alignment). This will create a [`TableFormatter`](https://github.com/plankp/ttables/blob/master/src/main/java/com/ymcmp/ttable/TableFormatter.java) which allows applying custom borders/corners and dividers.

After tweaking its looks, the `.toString()` method will generate the text table.

See the [tests](https://github.com/plankp/ttables/tree/master/src/test/java/com/ymcmp/ttable) for more.

## License

Released under the BSD 3-Clause License