package kz.mircella.blogserver.domain.model.user;

import lombok.Value;

import java.time.LocalDate;

@Value
public class UserDto {
    private String login;
    private int age;
    private String country;
    private String occupancy;
    private UserStatus userStatus;
    private LocalDate registeredAt;
}
