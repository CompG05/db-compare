package structure;

import java.util.*;
import java.util.stream.Collectors;

public class Procedure {
    String name;
    Set<Argument> arguments;

    public Procedure(String name) {
        this(name, new HashSet<>());
    }

    public Procedure(String name, Set<Argument> arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    public String getName() {
        return name;
    }

    public Set<Argument> getArguments() {
        return arguments;
    }

    public void addArgument(Argument argument) {
        arguments.add(argument);
    }

    public void addArguments(Collection<Argument> arguments) {
        this.arguments.addAll(arguments);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Procedure procedure = (Procedure) o;
        return Objects.equals(name, procedure.name) && Objects.equals(arguments, procedure.arguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, arguments);
    }

    @Override
    public String toString() {
        return name + "(" +
                arguments
                        .stream().sorted(Comparator.comparingInt(Argument::getOrder))
                        .map(Argument::toString)
                        .collect(Collectors.joining(", "))
                + ")";
    }
}
