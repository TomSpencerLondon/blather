package com.github.richardjwild.blather.command;

import com.github.richardjwild.blather.user.User;
import com.github.richardjwild.blather.user.UserRepository;

import java.sql.SQLException;
import java.util.Optional;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

public class FollowCommand implements Command {

    private final String followerUserName;
    private final String toFollowUserName;
    private final UserRepository userRepository;

    public FollowCommand(
            String followerUserName,
            String toFollowUserName,
            UserRepository userRepository)
    {
        this.followerUserName = followerUserName;
        this.toFollowUserName = toFollowUserName;
        this.userRepository = userRepository;
    }

    @Override
    public void execute() throws SQLException {
        findUserToFollow().ifPresent(toFollow -> {
            try {
                addFollower(toFollow);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    private Optional<User> findUserToFollow() throws SQLException {
        return userRepository.find(toFollowUserName);
    }

    private void addFollower(User toFollow) throws SQLException {
        User follower = findOrCreateFollower();
        if (!follower.equals(toFollow)) {
            follower.follow(toFollow);
            userRepository.save(follower);
        }
    }

    private User findOrCreateFollower() throws SQLException {
        return findFollower().orElseGet(this::createFollower);
    }

    private Optional<User> findFollower() throws SQLException {
        return userRepository.find(followerUserName);
    }

    private User createFollower() {
        return new User(followerUserName);
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
