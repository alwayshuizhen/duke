package Parser;
import TaskList.*;
import Ui.TextUi;
import Storage.FileOperation;
import Exception.DukeException;
import java.io.IOException;
import java.util.ArrayList;

public class ParseCommand {
    private Boolean exitStatus = false;
    private String cmdType;
    private String fullCommand;

    public ParseCommand(String fullCommand) throws DukeException {
        this.fullCommand = fullCommand;
        this.cmdType = cmdTypeIdentifier(fullCommand);
    }

    private String cmdTypeIdentifier(String cmd) throws DukeException {
        try {
            if (cmd.equalsIgnoreCase("list")){
                return "LIST";
            } else if (cmd.equalsIgnoreCase("bye")) {
                exitStatus = true;
                return "BYE";
            }else if (cmd.substring(0,4).equalsIgnoreCase("done")) {
                return "DONE";
            } else if (cmd.substring(0,4).equalsIgnoreCase("todo")) {
                return "TODO";
            } else if (cmd.substring(0,5).equalsIgnoreCase("event")) {
                return "EVENT";
            } else if (cmd.substring(0,6).equalsIgnoreCase("delete")) {
                return "DELETE";
            } else if (cmd.substring(0,8).equalsIgnoreCase("deadline")) {
                return "DEADLINE";
            }
        } catch (StringIndexOutOfBoundsException e) {
            throw new DukeException();
        }
        return null;
    }

    public boolean isExit () {
        return exitStatus;
    }

    public void execute(ArrayList<Task> taskList, TextUi ui, FileOperation storage) throws IOException, DukeException {
        if (cmdType != null) {
            switch (cmdType) {
            case "LIST":
                ui.printTaskList(taskList);
                break;
            case "DONE":
                markTaskAsDone(taskList);
                break;
            case "TODO":
                taskList.add(createToDo(taskList));
                break;
            case "EVENT":
                taskList.add(createEvent(taskList));
                break;
            case "DEADLINE":
                taskList.add(createDeadline(taskList));
                break;
            case "DELETE":
                taskList.remove(findObjectToRemove(taskList));
                break;
            default:
                break;
            }
            storage.saveTaskList(taskList);
        }

    }

    public void markTaskAsDone(ArrayList<Task> taskList) throws DukeException {
        int lenOfCmd = fullCommand.length() - 1;
        try {
            int index = Integer.parseInt(fullCommand.substring(lenOfCmd)) - 1;
            taskList.get(index).setDone();
            System.out.println("Nice! I've marked this task as done: " + taskList.get(index).printObject());
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            throw new DukeException();
        }
    }

    public Todo createToDo(ArrayList<Task> taskList) throws DukeException {
        try {
            Todo item = new Todo(fullCommand.substring(5));
            System.out.println("Got it. I've added this task: " + item.printObject());
            System.out.println("Now you have " + (taskList.size() + 1) + " tasks in the list.");
            return item;
        } catch (StringIndexOutOfBoundsException  e) {
            throw new DukeException();
        }
    }

    public Event createEvent(ArrayList<Task> taskList) throws DukeException {
        try {
            int indexOfAt = fullCommand.indexOf("/at");
            String eventDescription = fullCommand.substring(6, indexOfAt - 1);
            String eventDate = fullCommand.substring(indexOfAt + 4);
            Event item = new Event(eventDescription, eventDate);
            System.out.println("Got it. I've added this task: " + item.printObject());
            System.out.println("Now you have " + (taskList.size() + 1) + " tasks in the list.");
            return item;
        } catch (StringIndexOutOfBoundsException e) {
            throw new DukeException();
        }
    }

    public Deadline createDeadline(ArrayList<Task> taskList) throws DukeException {
        try {
            int indexOfBy = fullCommand.indexOf("/by");
            String deadlineDesc = fullCommand.substring(9, indexOfBy-1);
            String deadlineDate = fullCommand.substring(indexOfBy+4);
            Deadline item = new Deadline(deadlineDesc, deadlineDate);
            System.out.println("Got it. I've added this task: " + item.printObject());
            System.out.println("Now you have "+ (taskList.size()+1) +" tasks in the list.");
            return item;
        } catch (StringIndexOutOfBoundsException e) {
            throw new DukeException();
        }
    }

    public int findObjectToRemove(ArrayList<Task> taskList) throws DukeException {
        try {
            int indexOfObject = Integer.parseInt(fullCommand.substring(7)) - 1;
            System.out.println("Noted. I've removed this task: " + taskList.get(indexOfObject).printObject());
            System.out.println("Now you have " + (taskList.size() - 1) + " tasks in the list.");
            return indexOfObject;
        } catch (StringIndexOutOfBoundsException e){
            throw new DukeException();
        } catch (IndexOutOfBoundsException e) {
            throw new DukeException();
        }
    }
}
