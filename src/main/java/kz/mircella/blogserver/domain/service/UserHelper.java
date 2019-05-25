package kz.mircella.blogserver.domain.service;

import kz.mircella.blogserver.domain.model.user.User;
import kz.mircella.blogserver.domain.model.user.UserDto;
import kz.mircella.blogserver.web.userdto.UserDetails;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

class UserHelper {
    private final Clock clock;

    UserHelper() {
        this.clock = Clock.systemDefaultZone();
    }

    UserDto mapToUserProfile(User user) {
        String login = user.getLogin();
        String country = user.getCountry();
        String occupancy = user.getOccupancy();
        LocalDate now = LocalDate.now(clock);
        LocalDate birthDate = user.getBirthDate();
        int age = Period.between(birthDate, now).getYears();
        LocalDate registeredAt = user.getRegisteredAt().toLocalDate();
        return new UserDto(login, age, country, occupancy, user.getUserStatus(), registeredAt);
    }

    User mapToUser(UserDetails userDetails) {
        String login = userDetails.getLogin();
        String country = userDetails.getCountry();
        String occupancy = userDetails.getOccupancy();
        LocalDate birthDate = userDetails.getBirthDate();
        return User.create(login, birthDate, country, occupancy, LocalDateTime.now(clock));
    }

}
