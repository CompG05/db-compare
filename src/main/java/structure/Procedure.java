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
        return new HashSet<>(arguments);
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
        List<Argument> sortedArguments = arguments.stream()
                .filter(a -> a.type != ArgumentType.RETURN)
                .sorted(Comparator.comparingInt(Argument::getOrder))
                .collect(Collectors.toList());

        List<String> sortedArgumentStrings = new ArrayList<>();
        int lastPosition = 0;
        int position;

        for (Argument arg : sortedArguments) {
            position = arg.order;

            // Fill missing arguments with '_'
            for (int i = lastPosition+1; i < position; i++)
                sortedArgumentStrings.add("_");

            sortedArgumentStrings.add(arg.toString());
            lastPosition = position;
        }

        if (sortedArguments.isEmpty())
            sortedArgumentStrings = Collections.singletonList("...");

        StringBuilder str = new StringBuilder(name + "(");

        str.append(String.join(", ", sortedArgumentStrings) + ")");

        Optional<Argument> returnArg = arguments.stream().filter(a -> a.type == ArgumentType.RETURN).findFirst();
        returnArg.ifPresent(a -> str.append(" returns " + a.dataType));

        return str.toString();
    }
}
