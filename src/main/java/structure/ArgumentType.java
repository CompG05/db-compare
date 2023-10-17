package structure;

public enum ArgumentType {
    IN,
    OUT,
    INOUT,
    RETURN;


    public static ArgumentType from(int columnType) {
        switch (columnType) {
            case 1: return IN;
            case 2: return INOUT;
            case 4: return OUT;
            case 5: return RETURN;
            default:
                throw new IllegalArgumentException("Unknown column type: " + columnType);
        }
    }
}
