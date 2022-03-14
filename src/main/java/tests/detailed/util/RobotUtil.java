package tests.detailed.util;

import java.awt.*;

public class RobotUtil {

    static Robot Instance=null;

    public static Robot GetInstance() throws AWTException {
        if(Instance==null)
            Instance=new Robot();
        return Instance;
    }

}
