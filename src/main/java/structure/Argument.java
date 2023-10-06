package structure;

import java.util.Objects;

public class Argument {
    String name;
    ArgumentType type;
    String dataType;

    public Argument(String name, ArgumentType type, String dataType) {
        this.name = name;
        this.type = type;
        this.dataType = dataType;
    }

    public String getName() {
        return name;
    }

    public ArgumentType getType() {
        return type;
    }

    public String getDataType() {
        return dataType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Argument argument = (Argument) o;
        return Objects.equals(name, argument.name) && type == argument.type && Objects.equals(dataType, argument.dataType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, dataType);
    }

    @Override
    public String toString(){
        return String.join(" ", type.toString(), name, dataType);
    }
}
