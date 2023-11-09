package comparator.reporter;

import comparator.DBComparator;
import javafx.util.Pair;
import structure.Procedure;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

public class Reporter {
    DBComparator comparator;
    String rowFormat, headerFormat;
    int columnWidth;
    String schemaName1, schemaName2;
    Set<String> uniqueTables1, uniqueTables2;
    Set<Pair<String, String>> commonTables;
    Set<String> uniqueProcedures1, uniqueProcedures2;
    Set<Pair<String, String>> commonProcedures;

    public Reporter(DBComparator comparator) {
        this.comparator = comparator;
        this.schemaName1 = comparator.getSchema1().getName();
        this.schemaName2 = comparator.getSchema2().getName();
        this.uniqueTables1 = comparator.getUniqueTables().getKey();
        this.uniqueTables2 = comparator.getUniqueTables().getValue();
        this.commonTables = comparator.getCommonTablesDiffs().stream()
                .map(p -> new Pair<>(p.getKey().toString(), p.getValue().toString()))
                .collect(Collectors.toSet());
        this.uniqueProcedures1 = comparator.getUniqueProcedures().getKey().stream()
                .map(Procedure::toString).collect(Collectors.toSet());
        this.uniqueProcedures2 = comparator.getUniqueProcedures().getValue().stream()
                .map(Procedure::toString).collect(Collectors.toSet());
        this.commonProcedures = comparator.getCommonProceduresDiffs().stream()
                .map(p -> new Pair<>(p.getKey().toString(), p.getValue().toString()))
                .collect(Collectors.toSet());

        computeColumnWidth();
    }

    private void report(PrintStream out) {
        printTopBorder(out);

        printRow(out, schemaName1, schemaName2);

        printHeader(out, "UNIQUE TABLES");
        printAligned(out, uniqueTables1, uniqueTables2);

        printHeader(out, "COMMON TABLES");
        for (Pair<String, String> pair : commonTables) {
            printSideBySide(out, pair.getKey(), pair.getValue());
            printRow(out, "", "");
        }

        printHeader(out, "UNIQUE PROCEDURES");
        // printAligned(out, uniqueProcedures1, uniqueProcedures2);
        Iterator<String> it1 = uniqueProcedures1.iterator();
        Iterator<String> it2 = uniqueProcedures2.iterator();

        while (it1.hasNext() && it2.hasNext()) {
            printSideBySide(out, it1.next(), it2.next());
            printRow(out, "", "");
        }

        while (it1.hasNext()) {
            printSideBySide(out, it1.next(), "");
        printRow(out, "", "");
    }

        while (it2.hasNext()) {
            printSideBySide(out, "", it2.next());
            printRow(out, "", "");
        }

        printHeader(out, "COMMON PROCEDURES");
        for (Pair<String, String> pair : commonProcedures) {
            printSideBySide(out, pair.getKey(), pair.getValue());
            printRow(out, "", "");
        }

        printBottomBorder(out);
    }

    // Prints a report to stdout
    public void report() {
        report(System.out);
    }

    public void report(String outputFilename) {
        try {
            PrintStream stream = new PrintStream(outputFilename);
            report(stream);
            stream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void printAligned(PrintStream out, Collection<String> leftColumn, Collection<String> rightColumn) {
        Iterator<String> it1 = leftColumn.iterator();
        Iterator<String> it2 = rightColumn.iterator();

        while (it1.hasNext() && it2.hasNext())
            printRow(out, it1.next(), it2.next());

        while (it1.hasNext())
            printRow(out, it1.next(), "");

        while (it2.hasNext())
            printRow(out, "", it2.next());
    }

    private void printSideBySide(PrintStream out, String leftColumn, String rightColumn) {
        List<String> leftLineSet = Arrays.asList(leftColumn.replace("\t", "  ").split("\n"));
        List<String> rightLineSet = Arrays.asList(rightColumn.replace("\t", "  ").split("\n"));

        printAligned(out, leftLineSet, rightLineSet);
    }

    private void printRow(PrintStream out, String leftColumn, String rightColumn) {
        String format = " %-" + columnWidth + "s │ %s";
        out.printf(format, leftColumn, rightColumn);
        out.println();
    }

    private void printHeader(PrintStream out, String text) {
        char[] headingSpaceCharArray = new char[columnWidth+1 - text.length()/2];
        Arrays.fill(headingSpaceCharArray, ' ');
        String headingSpace = new String(headingSpaceCharArray);
        String format = headingSpace + " %s\n";

        printBottomBorder(out);
        out.printf(format, text);
        printTopBorder(out);
    }

    private void printTopBorder(PrintStream out) {
        char[] separatorCharArray = new char[columnWidth+2];
        Arrays.fill(separatorCharArray, '─');
        String separatorHalf = new String(separatorCharArray);
        out.println(separatorHalf + '┬' + separatorHalf);
    }

    private void printBottomBorder(PrintStream out) {
        char[] separatorCharArray = new char[columnWidth+2];
        Arrays.fill(separatorCharArray, '─');
        String separatorHalf = new String(separatorCharArray);
        out.println(separatorHalf + '┴' + separatorHalf);
    }

    private void computeColumnWidth() {
        columnWidth = Math.max(schemaName1.length(), schemaName2.length());

        for (String uniqueTable : uniqueTables1)
            columnWidth = Math.max(columnWidth, uniqueTable.length());

        for (String tableString : commonTables.stream().map(Pair::getKey).collect(Collectors.toSet()))
            for (String line : tableString.replace("\t", "  ").split("\n"))
                columnWidth = Math.max(columnWidth, line.length());

        for (String procedure : uniqueProcedures1)
            columnWidth = Math.max(columnWidth, procedure.length());

        for (String procedure : commonProcedures.stream().map(Pair::getKey).collect(Collectors.toSet()))
            for (String line : procedure.replace("\t", "  ").split("\n"))
                columnWidth = Math.max(columnWidth, line.length());
    }
}
