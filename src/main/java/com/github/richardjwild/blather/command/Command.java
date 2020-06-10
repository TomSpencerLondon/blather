package com.github.richardjwild.blather.command;

import java.sql.SQLException;

public interface Command {

    void execute() throws SQLException;
}
