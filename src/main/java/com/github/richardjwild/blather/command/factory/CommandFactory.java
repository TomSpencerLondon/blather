package com.github.richardjwild.blather.command.factory;

import com.github.richardjwild.blather.command.Command;
import com.github.richardjwild.blather.parsing.ParsedInput;
import java.sql.SQLException;

public interface CommandFactory {
    Command makeCommandFor(ParsedInput input) throws SQLException;
}
