package me.brownzombie.floppa.command;

public abstract class Command {

    private final String[] names;
    private final String syntax;

    public Command(String[] names, String syntax) {
        this.names = names;
        this.syntax = syntax;
    }

    public abstract void exec(String s) throws Exception;

    public String[] getName() {
        return this.names;
    }

    public String getSyntax() {
        return this.syntax;
    }

    public String getSyntax(Command command) {
        return command.syntax;
    }
}
