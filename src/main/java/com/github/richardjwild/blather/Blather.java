package com.github.richardjwild.blather;

import com.github.richardjwild.blather.application.Application;
import com.github.richardjwild.blather.application.ApplicationBuilder;
import com.github.richardjwild.blather.io.ConsoleInput;
import com.github.richardjwild.blather.io.ConsoleOutput;
import com.github.richardjwild.blather.time.SystemClock;
import java.sql.SQLException;

public class Blather {

    public static void main(String[] args) throws SQLException {
        Application application = ApplicationBuilder.build(new ConsoleInput(), new ConsoleOutput(), new SystemClock());
        application.run();
    }
}
