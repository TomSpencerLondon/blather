package com.github.richardjwild.blather.application;

import com.github.richardjwild.blather.command.factory.CommandFactories;
import com.github.richardjwild.blather.command.factory.FollowCommandFactory;
import com.github.richardjwild.blather.command.factory.PostCommandFactory;
import com.github.richardjwild.blather.command.factory.QuitCommandFactory;
import com.github.richardjwild.blather.command.factory.ReadCommandFactory;
import com.github.richardjwild.blather.command.factory.WallCommandFactory;
import com.github.richardjwild.blather.io.Input;
import com.github.richardjwild.blather.io.Output;
import com.github.richardjwild.blather.message.MessageRepository;
import com.github.richardjwild.blather.parsing.CommandReader;
import com.github.richardjwild.blather.parsing.InputParser;
import com.github.richardjwild.blather.persistence.JdbcMessageRepository;
import com.github.richardjwild.blather.persistence.JdbcUserRepository;
import com.github.richardjwild.blather.time.Clock;
import com.github.richardjwild.blather.time.TimestampFormatter;
import com.github.richardjwild.blather.user.UserRepository;
import java.sql.Connection;
import java.sql.SQLException;

public class ApplicationBuilder {

    public static Application build(Input input, Output output, Clock clock) {

        Connection connection = connection();
        UserRepository userRepository = new JdbcUserRepository(connection);
        MessageRepository messageRepository = new JdbcMessageRepository(connection);

        InputParser inputParser = new InputParser();
        Controller controller = new Controller();

        TimestampFormatter timestampFormatter = new TimestampFormatter(clock);

        CommandFactories commandFactories = new CommandFactories(
                new FollowCommandFactory(userRepository),
                new PostCommandFactory(messageRepository, userRepository, clock),
                new QuitCommandFactory(controller),
                new ReadCommandFactory(
                        messageRepository,
                        userRepository,
                        timestampFormatter,
                        output),
                new WallCommandFactory(
                        userRepository,
                        messageRepository,
                        timestampFormatter,
                        output));

        CommandReader commandReader = new CommandReader(
                input,
                inputParser,
                commandFactories);

        EventLoop eventLoop = new EventLoop(commandReader, controller);
        return new Application(eventLoop, output);
    }

    private static Connection connection() {
        try {
            OracleDataSource ds = new OracleDataSource();
            ds.setURL("jdbc:oracle:thin:blather/blather@//localhost:1521/XE");
            return ds.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
