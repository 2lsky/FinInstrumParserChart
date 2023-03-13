
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static com.fx.jfree.chart.demo.JfreeCandlestickChartDemo.createAndShowGUI;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException{
        Gui inter = new Gui();
        Parser parser = new Parser("C:\\Users\\Vitam\\OneDrive\\Рабочий стол\\chromedriver.exe",
                "https://www.finam.ru/profile/moex-akcii/gazprom/export/",
                "F:\\PARSER_DATA\\");
        while (inter.getFlagExit() == 0) {
            TimeUnit.SECONDS.sleep(1);
            if (inter.getFlag() == 1){
                HashMap<String, String> vals = inter.getCurrVals();
                String[] params = new String[]{vals.get("RSI U period"),
                                vals.get("RSI D period"),
                                vals.get("RSI U alpha"),
                                vals.get("RSI D alpha"),
                                vals.get("EMA period"),
                                vals.get("EMA alpha")};
                File fileToDelete = new File("F:\\PARSER_DATA\\data.csv");
                boolean success = fileToDelete.delete();
                if (success) {
                    System.out.println("FILE WILL BE OVERWRITTEN");
                }
                String[] out = parser.getData(vals);
                String title = out[1];
                String fileName = out[0];
                if (fileName == null){
                    continue;
                }
                Writer writer = new Writer("F:\\PARSER_DATA\\params.txt");
                writer.toWrite(fileName, Arrays.toString(params));
                Logics logic = new Logics("F:\\PARSER_DATA\\logic.py");
                logic.transformFile();
                SwingUtilities.invokeLater(() -> createAndShowGUI(fileName, title));
                inter.setFlag(0);
            }
        }
        parser.driverClose();
        System.exit(1);
    }
}

