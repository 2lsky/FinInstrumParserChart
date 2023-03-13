import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class Writer {
    String outputPath;

    public Writer(String outputPath) {
        this.outputPath = outputPath;
    }

    public void toWrite(String fileName, String params) throws IOException {
        PrintWriter writer = new PrintWriter(this.outputPath, StandardCharsets.UTF_8);
        writer.println(fileName);
        writer.println(params);
        writer.close();
    }
}
