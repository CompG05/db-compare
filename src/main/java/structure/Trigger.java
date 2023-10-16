package structure;

import java.util.Objects;

public class Trigger {
    String name;
    String actionTiming;
    String eventManipulation;

    public Trigger(String name, String actionTiming, String eventManipulation) {
        this.name = name;
        this.actionTiming = actionTiming;
        this.eventManipulation = eventManipulation;
    }

    public String getName() {
        return name;
    }

    public String getActionTiming() {
        return actionTiming;
    }

    public String getEventManipulation() {
        return eventManipulation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trigger trigger = (Trigger) o;
        return Objects.equals(name, trigger.name) && Objects.equals(actionTiming, trigger.actionTiming) && Objects.equals(eventManipulation, trigger.eventManipulation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, actionTiming, eventManipulation);
    }

    @Override
    public String toString() {
        return String.join(" ", "TRIGGER", name, actionTiming, eventManipulation);
    }
}
