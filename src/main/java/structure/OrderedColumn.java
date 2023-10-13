package structure;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class OrderedColumn {
    private String column;
    private int order;

    public OrderedColumn(String column, int order) {
        this.column = column;
        this.order = order;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderedColumn)) return false;
        OrderedColumn pair = (OrderedColumn) o;
        return order == pair.order && Objects.equals(column, pair.column);
    }

    @Override
    public int hashCode() {
        return Objects.hash(column, order);
    }

    @Override
    public String toString() {
        return "("
                + "column='" + column + '\''
                + ", order=" + order
                + ')';
    }

    public static List<String> getSorted(Set<OrderedColumn> orderedColumns) {
        return orderedColumns.stream()
                .sorted(Comparator.comparingInt(OrderedColumn::getOrder))
                .map(OrderedColumn::getColumn)
                .collect(Collectors.toList());
    }
}
