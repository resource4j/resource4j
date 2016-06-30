package example.el;

import com.github.resource4j.spring.annotations.InjectValue;

public class Warning {

    @InjectValue
    private String message;

    @InjectValue
    private String name;

    @InjectValue
    private String escaped;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEscaped() {
        return escaped;
    }

    public void setEscaped(String escaped) {
        this.escaped = escaped;
    }
}
