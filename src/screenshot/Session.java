package screenshot;

import java.io.Serializable;

public class Session implements Serializable{
    private String test = "hej";

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
    
}
