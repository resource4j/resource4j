package example.el;

import com.github.resource4j.spring.annotations.InjectValue;

public class Warning {

    @InjectValue
    private String message;

    @InjectValue
    private String escaped;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEscaped() {
        return escaped;
    }

    public void setEscaped(String escaped) {
        this.escaped = escaped;
    }
}
