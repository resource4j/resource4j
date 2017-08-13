package example.el;

import com.github.resource4j.spring.annotations.InjectValue;

public class User {

    @InjectValue
    private String name;

    @InjectValue
    private String salutation;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }
}
