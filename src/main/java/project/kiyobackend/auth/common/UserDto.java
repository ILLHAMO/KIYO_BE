package project.kiyobackend.auth.common;

import jdk.jfr.DataAmount;
import lombok.Data;
import project.kiyobackend.user.domain.User;

import java.util.HashMap;
import java.util.Map;

@Data
public class UserDto {
    private int code;
    private boolean success;
    private Map<String,User> data = new HashMap<>();

    public UserDto(int code, boolean success, String name, User user)
    {

        this.code = code;
        this.success = success;
        this.data.put(name,user);
    }
}
