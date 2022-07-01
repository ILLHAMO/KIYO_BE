package project.kiyobackend.auth.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;


@Data
public class TokenSucessDto {

    private int code;
    private boolean success;
    private Map<String,String> data = new HashMap<>();

    public TokenSucessDto(int code,boolean success,String name, String token)
    {

        this.code = code;
        this.success = success;
        this.data.put(name,token);
    }
}
