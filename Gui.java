import com.google.gson.JsonParser;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Objects;

public class Gui extends JFrame {
    public HashMap<String, JPanel> dictJpanel;
    public HashMap<String, String> vals;
    private int flag = 0;
    private int flagExit = 0;
    public HashMap<String, JTextField> dictParams = new HashMap<>();

    String[] labelPlacement = new String[]{"Market",
            "Instrument",
            "From",
            "To",
            "Period"
    };

    public Gui() throws FileNotFoundException {
        super("Parser app");
        setSize(300, 300);
        setResizable(false);
        setLocationRelativeTo(null);
        FlowLayout layout = new FlowLayout();
        setLayout(layout);
        this.dictJpanel = new HashMap<>();
        HashMap<String, String[]> dict = makeDict();
        for (String key : labelPlacement) {
            JPanel panel = createJpanel(key, dict.get(key));
            this.dictJpanel.put(key, panel);
            add(panel);
        }
        JButton button = new JButton("Upload");
        ActionListener actionListener = new pressButton();
        button.addActionListener(actionListener);
        add(button);
        //
        JLabel emaLabelPer = new JLabel("EMA period");
        JLabel emaLabelAlpha = new JLabel("EMA alpha");
        JTextField emaPer = new JTextField(5);
        JTextField emaAlpha = new JTextField(5);
        this.dictParams.put("EMA period", emaPer);
        this.dictParams.put("EMA alpha", emaAlpha);
        emaPer.setText("1");
        emaAlpha.setText("0.5");
        add(emaLabelPer);
        add(emaPer);
        add(emaLabelAlpha);
        add(emaAlpha);
        //
        JLabel rsiLabelPerU = new JLabel("RSI U period");
        JLabel rsiLabelAlphaU = new JLabel("RSI U alpha");
        JTextField rsiPerU = new JTextField(5);
        JTextField rsiAlphaU = new JTextField(5);
        this.dictParams.put("RSI U period", rsiPerU);
        this.dictParams.put("RSI U alpha", rsiAlphaU);
        rsiPerU.setText("1");
        rsiAlphaU.setText("0.5");
        JLabel rsiLabelPerD = new JLabel("RSI D period");
        JLabel rsiLabelAlphaD = new JLabel("RSI D alpha");
        JTextField rsiPerD = new JTextField(5);
        JTextField rsiAlphaD = new JTextField(5);
        this.dictParams.put("RSI D period", rsiPerD);
        this.dictParams.put("RSI D alpha", rsiAlphaD);
        rsiPerD.setText("1");
        rsiAlphaD.setText("0.5");
        add(rsiLabelPerU);
        add(rsiPerU);
        add(rsiLabelAlphaU);
        add(rsiAlphaU);
        add(rsiLabelPerD);
        add(rsiPerD);
        add(rsiLabelAlphaD);
        add(rsiAlphaD);
        //
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                    flagExit = 1;
                }
            }
        );
        setVisible(true);
    }
    private HashMap<String, String[]> makeDict(){
        String[][] dictVals = new String[][]{{"МосБиржа акции"},
                {"ГАЗПРОМ ао"},
                {"23.08.2022"},
                {"24.08.2022"},
                {"тики", "1 мин.", "5 мин.", "10 мин.", "15 мин.", "30 мин.", "1 час", "1 день", "1 неделя", "1 месяц"}
        };
        HashMap<String, String[]> dict = new HashMap<>();
        for (int i = 0; i < labelPlacement.length; i ++){
            dict.put(labelPlacement[i], dictVals[i]);
        }
        return dict;
    }
    private JPanel createJpanel(String name, String[] vals) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(name);
        panel.add(label);
        if (Objects.equals(name, "Period")) {
            JComboBox<String> combo = new JComboBox<>(vals);
            panel.add(combo);

        } else {
            JTextField text = new JTextField(20);
            panel.add(text);
            text.setText(vals[0]);
        }
        return panel;
    }
    public HashMap<String, String> getCurrVals(){
        this.setFlag(0);
        return this.vals;
    }

    private class pressButton implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            HashMap<String, String> currVals = new HashMap<>();
            for (String key: dictJpanel.keySet()){
                JPanel panel = dictJpanel.get(key);
                if (Objects.equals(key, "Period")){
                    JComboBox<String> field = (JComboBox<String>) panel.getComponents()[1];
                    currVals.put(key, Objects.requireNonNull(field.getSelectedItem()).toString());
                }
                else{
                    JTextField field = (JTextField) panel.getComponents()[1];
                    currVals.put(key, field.getText());
                }
            }
            for (String key: dictParams.keySet()){
                JTextField field = (JTextField) dictParams.get(key);
                currVals.put(key, field.getText());
            }
            vals = currVals;
            setFlag(1);
        }
    }
    public int getFlag() {
        return flag;
    }

    void setFlag(int flag) {
        this.flag = flag;
    }
    public int getFlagExit() {
        return flagExit;
    }
}