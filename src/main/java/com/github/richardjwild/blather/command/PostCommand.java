package com.github.richardjwild.blather.command;

import com.github.richardjwild.blather.message.Message;
import com.github.richardjwild.blather.message.MessageRepository;
import com.github.richardjwild.blather.user.User;
import com.github.richardjwild.blather.user.UserRepository;
import com.github.richardjwild.blather.time.Clock;

import java.sql.SQLException;
import java.util.Optional;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

public class PostCommand implements Command {

    private final String recipientUserName;
    private final String messageText;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final Clock clock;

    public PostCommand(
            String recipientUserName,
            String messageText,
            MessageRepository messageRepository,
            UserRepository userRepository,
            Clock clock)
    {
        this.recipientUserName = recipientUserName;
        this.messageText = messageText;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.clock = clock;
    }

    @Override
    public void execute() throws SQLException {
        User recipient = findOrCreateRecipient();
        Message message = new Message(recipient, messageText, clock.now());
        messageRepository.postMessage(recipient, message);
    }

    private User findOrCreateRecipient() throws SQLException {
        return findRecipient().orElseGet(() -> {
            try {
                return createRecipient();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return null;
        });
    }

    private Optional<User> findRecipient() throws SQLException {
        return userRepository.find(recipientUserName);
    }

    private User createRecipient() throws SQLException {
        User recipient = new User(recipientUserName);
        userRepository.save(recipient);
        return recipient;
    }

    @Override
    public boolean equals(Object o) {
        return reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return reflectionHashCode(this);
    }
}
