

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Logics {
    String pathToScript;

    public Logics(String pathToScript) {
        this.pathToScript = pathToScript;
    }

    public boolean transformFile() throws IOException {
        Process runtime = Runtime.getRuntime().exec("cmd /c py " + this.pathToScript);
        String s = null;
        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(runtime.getInputStream()));
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
        }
        return true;
        }
}