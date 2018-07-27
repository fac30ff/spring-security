package sample.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "user with requested id not found")
public class UserNotFoundException extends RuntimeException {
}
