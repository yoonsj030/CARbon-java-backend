package yoonsj030.CARbon.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Builder
@Getter
public class BaseResponse<T> {
    private HttpStatus httpStatus;          // HTTp Status code

    private String message;          // Response Message

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;          // Response Result
}
