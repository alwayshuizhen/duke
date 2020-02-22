package TaskList;
public class Task {
    protected String description;
    protected boolean isDone;
    protected char itemType;
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "\u2713" : "\u2718"); //return tick or X symbols
    }

    public void setDone() {
        this.isDone = true;
    }

    public String printObject(){
        return "\0";
    }

    public String createStrForSaving() { return ""; }

    public String convertBoolean(){
        return (isDone == true) ? "1" : "0";
    }
}
