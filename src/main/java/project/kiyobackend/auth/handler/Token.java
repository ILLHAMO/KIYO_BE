package project.kiyobackend.auth.handler;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Token implements Serializable {
    String token;
}
