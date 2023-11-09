package structure;

import java.util.Objects;

public class Argument {
    String name;
    int order;
    ArgumentType type;
    String dataType;

    public Argument(String name, int order, ArgumentType type, String dataType) {
        this.name = name;
        this.order = order;
        this.type = type;
        this.dataType = dataType;
    }

    public String getName() {
        return name;
    }

    public int getOrder() {
        return order;
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
        return order == argument.order && type == argument.type && Objects.equals(dataType, argument.dataType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, type, dataType);
    }

    @Override
    public String toString(){
        return String.join(" ", type.toString(), name, dataType);
    }
}
