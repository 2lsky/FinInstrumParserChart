
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.*;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

public class Parser {
    private final String path;
    private ChromeDriver driver;
    private final String initURL;
    private final ChromeOptions chromeOptions;
    private final HashMap<String, Object> droplistXpathDict;
    private final HashMap<String, Object> arrowXpathDict;

    public Parser(String pathToDriver, String initURL, String outPutPath) {
        this.path = outPutPath;
        ChromeOptions options = new ChromeOptions();
        HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
        chromePrefs.put("download.default_directory", this.path);
        chromePrefs.put("profile.default_content_settings.popups", 0);
        //options.addArguments("--headless");
        options.setExperimentalOption("prefs",chromePrefs);
        System.setProperty("webdriver.chrome.driver", pathToDriver);
        this.driver = null;
        this.chromeOptions = options;
        this.initURL = initURL;
        this.arrowXpathDict = new HashMap<>();
        this.droplistXpathDict = new HashMap<>();
        this.arrowXpathDict.put("MarketDroplistArrow",
                "/html/body/div[3]/div/div[3]/div/table/tbody/tr/td/div/div/div[2]/div[1]/div[1]/div[2]");
        this.arrowXpathDict.put("InstrumDroplistArrow",
                "/html/body/div[3]/div/div[3]/div/table/tbody/tr/td/div/div/div[2]/div[1]/div[2]/div");
        this.droplistXpathDict.put("MarketDroplist",
                "/html/body/div[15]");
        this.droplistXpathDict.put("InstrumDroplist",
                "/html/body/div[16]");

    }
    private void driverConnect(String site) {
        try {
            if (this.driver == null) {
                this.driver = new ChromeDriver(this.chromeOptions);
            }
            this.driver.navigate().to(site);
            TimeUnit.SECONDS.sleep(10);
            System.out.println("Connection to " + site);
        }
        catch (WebDriverException e) {
            this.driver = null;
            System.out.println("No connection");
            System.exit(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void driverClose(){
        if (this.driver != null) {
            this.driver.close();
            this.driver = null;
        }
    }
    private void pressDropListArrow(String arrowType) {
        if (this.driver != null){
            String xpath = (String) this.arrowXpathDict.get(arrowType);
            WebElement arrow = this.driver.findElement(By.xpath(xpath));
            arrow.click();
            System.out.println(arrowType + " was pressed");
        }
        else{
            System.out.println("No connection");
            System.exit(1);
        }
    }
    private void pressDropListElem(String elementName, String droplistType) throws InterruptedException {
        String arrowType = droplistType + "Arrow";
        int flg = 0;
        this.pressDropListArrow(arrowType);
        String xpath = (String) droplistXpathDict.get(droplistType);
        WebElement droplist = this.driver.findElement(By.xpath(xpath));
        WebElement[] items = droplist.findElement(By.xpath("./div")).findElement(By.xpath("./ul")).findElements(By.xpath("./li")).toArray(new WebElement[0]);
        for (WebElement x: items){
            if (Objects.equals(x.findElement(By.xpath("./a")).getAttribute("innerHTML"), elementName)){
                flg = 1;
                x.click();
                break;
            }
        }
        TimeUnit.SECONDS.sleep(2);
        if (flg == 1) {
            System.out.println(elementName + " was pressed in " + droplistType);
        }else{
            System.out.println(elementName);
            System.out.println("Incorrect name for " + droplistType + " field");
            showMessageDialog(null,
                    "Incorrect name for " + droplistType + " field",
                    "INPUT ERROR", ERROR_MESSAGE);
            this.driverClose();
        }
    }
//    private HashMap<String, String> universalDroplistParser(String droplistType){
//        if (this.driver == null){
//            this.driverConnect(this.initURL);
//        }
//        String xpath = (String) this.droplistXpathDict.get(droplistType);
//        WebElement droplist = this.driver.findElement(By.xpath(xpath));
//        String htmlDroplist = droplist.findElement(By.xpath("./div/ul")).getAttribute("innerHTML");
//        System.out.println(htmlDroplist);
//        Pattern pattern = Pattern.compile("value=\"(\\d+)\">([а-яёА-ЯЁa-zA-Z\\s\\d\\-:+=]+)</a");
//        HashMap<String, String> dropListDict = new HashMap<>();
//        Matcher matcher = pattern.matcher(htmlDroplist);
//        while (matcher.find()) {
//            dropListDict.put(matcher.group(2), matcher.group(1));
//        }
//        System.out.println(droplistType + " was written");
//        return dropListDict;
//    }
//    private void writeJSON (JSONObject jsonObject, String path){
//        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
//            out.write(jsonObject.toString());
//            System.out.print("Successfull writting in" + path + "\n");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    public void writeAllData(String marketName) throws InterruptedException {
//        if (this.driver == null){
//            this.driverConnect(this.initURL);
//        }
//        HashMap<String, String> marketDict = this.universalDroplistParser("MarketDroplist");
//        JSONObject jsonMarketDict = new JSONObject(marketDict);
//        this.writeJSON(jsonMarketDict, this.path + "market_id_dict.json");
//        HashMap<String, String> allInstrumDict = new HashMap<>();
//        HashMap<String, String> codeDict = new HashMap<>();
//        HashMap<String, String[]> marketInstrumDict = new HashMap<>();
//        this.pressDropListElem(marketName, "MarketDroplist");
//        HashMap<String, String> instrumDict = this.universalDroplistParser("InstrumDroplist");
//        System.out.println(instrumDict.entrySet());
//        marketInstrumDict.put(marketName, instrumDict.keySet().toArray(String[]::new));
//        for (String instrumName : instrumDict.keySet()) {
//            this.pressDropListElem(instrumName, "InstrumDroplist");
//            TimeUnit.SECONDS.sleep(2);
//            codeDict.put(instrumName, this.getInstrumCode());
//        }
//        allInstrumDict.putAll(instrumDict);
//        JSONObject jsonInstrumDict = new JSONObject(allInstrumDict);
//        JSONObject jsonCodeDict = new JSONObject(codeDict);
//        JSONObject jsonMarketInstrumDict = new JSONObject(marketInstrumDict);
//        this.writeJSON(jsonInstrumDict, this.path + "instrum_id_dict.json");
//        this.writeJSON(jsonMarketInstrumDict, this.path + "market_instrum_dict.json");
//        this.writeJSON(jsonCodeDict, this.path + "instrum_code_dict.json");
//    }
    private String readDIct(String path, String key) throws FileNotFoundException {
        JsonParser parser = new JsonParser();
        JsonElement objDict = parser.parse(new FileReader(this.path + path));
        return objDict.getAsJsonObject().get(key).toString();
    }

    private String[] getInfo(){
        String xpath = "/html/body/div[3]/div/div[3]/div/table/tbody/tr/td/script[1]";
        StringBuilder code = null;
        StringBuilder instrumId = null;
        if (this.driver != null) {
            String text = this.driver.findElement(By.xpath(xpath)).getAttribute("innerHTML");
            String[] newText = text.split("\n")[4].split("\\{")[2].split(":");
            code = new StringBuilder();
            instrumId = new StringBuilder();
            for (char c: newText[2].split(",")[0].toCharArray()){
                if (c != ' ') {
                    if (c != '"') {
                        code.append(c);
                    }
                }
            }
            for (char c: newText[1].split(",")[0].toCharArray()){
                if (c != ' ') {
                    instrumId.append(c);
                }
            }
            return new String[]{code.toString(), instrumId.toString()};
        }
        else{
            System.out.println("No connection");
            System.exit(1);
            return null;
        }
    }
    private String strTransform(String str){
        char[] c = str.toCharArray();
        if (c[0] == '0'){
            return "" + c[1];
        }
        else{
            return str;
        }
    }
    private String getNumberKey(String[] vals, String val) {
        int i = 1;
        String out = null;
        for (String value : vals) {
            if (Objects.equals(value, val)) {
                out = "" + i;
            } else {
                i++;
            }
        }
        return out;
    }
    private String dateTransform(String date){
        char[] c = date.toCharArray();
        StringBuilder out = new StringBuilder();
        for (char value: c){
            if (value != '.'){
                out.append(value);
            }
        }
        return out.toString();
    }
    public String[] getData(HashMap<String, String> vals) throws IOException, InterruptedException {
        String market = vals.get("Market");
        String instrum = vals.get("Instrument");
        String fromDate = vals.get("From");
        String toDate = vals.get("To");
        String period = vals.get("Period");
        String delimeter = "\\.";
        String yf = fromDate.split(delimeter)[2];
        String yt = toDate.split(delimeter)[2];
        String mf = this.strTransform(fromDate.split(delimeter)[1]);
        String mt = this.strTransform(toDate.split(delimeter)[1]);
        String df = this.strTransform(fromDate.split(delimeter)[0]);
        String dt = this.strTransform(toDate.split(delimeter)[0]);
        if (this.driver == null){
            this.driverConnect(this.initURL);
        }
        this.pressDropListElem(market, "MarketDroplist");
        this.pressDropListElem(instrum, "InstrumDroplist");
        String code = this.getInfo()[0];
        String marketId = this.readDIct("market_id_dict.json", market);
        String instrumId = this.getInfo()[1];
        System.out.println(instrumId);
        System.out.println(marketId);
        System.out.println(code);
        String fileName = code + '_' + this.dateTransform(fromDate) + '_' + this.dateTransform(toDate);
        String httpStr = "http://export.finam.ru/" + fileName +
                ".txt?market=" + marketId +
                "&em=" + instrumId +
                "&code=" + code +
                "&apply=0" +
                "&df=" + df +
                "&mf=" + (Integer.parseInt(mf) - 1) +
                "&yf=" + yf +
                "&from=" + fromDate +
                "&dt=" + dt +
                "&mt=" + (Integer.parseInt(mt) - 1) +
                "&yt=" + yt +
                "&to=" + toDate +
                "&p=" + this.getNumberKey(new String[]{"тики", "1 мин.", "5 мин.", "10 мин.", "15 мин.", "30 мин.", "1 час", "1 день", "1 неделя", "1 месяц"}, period) +
                "&f=" + "data" +
                "&e=.csv" +
                "&cn=" + code +
                "&dtf=1" +
                "&tmf=3&MSOR=1&mstimever=1&sep=3&sep2=1&datf=1&at=1";
        this.driverConnect(httpStr);
        return new String[]{this.path + "data", market + ": " + instrum};
    }
}
