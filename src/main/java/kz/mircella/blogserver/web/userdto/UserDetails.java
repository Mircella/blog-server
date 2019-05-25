package kz.mircella.blogserver.web.userdto;

import lombok.Value;

import java.time.LocalDate;

@Value
public class UserDetails {
    private String login;
    private LocalDate birthDate;
    private String country;
    private String occupancy;
}
