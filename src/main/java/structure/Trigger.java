package structure;

import java.util.Objects;

public class Trigger {
    String name;
    String triggerCondition;

    public Trigger(String name, String triggerCondition) {
        this.name = name;
        this.triggerCondition = triggerCondition;
    }

    public String getName() {
        return name;
    }

    public String getTriggerCondition() {
        return triggerCondition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trigger trigger = (Trigger) o;
        return Objects.equals(name, trigger.name) && Objects.equals(triggerCondition, trigger.triggerCondition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, triggerCondition);
    }

    @Override
    public String toString() {
        return "TRIGGER " + name + " " + triggerCondition;
    }
}
