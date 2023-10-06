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
        StringBuilder str = new StringBuilder();
        str.append(name).append("(");

        List<String> argStrings = arguments.stream().map(Argument::toString).collect(Collectors.toList());
        str.append(String.join(", ", argStrings));

        return str.toString();
    }
}
