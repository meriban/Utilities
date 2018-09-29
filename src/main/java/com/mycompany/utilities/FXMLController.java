package com.mycompany.utilities;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class FXMLController implements Initializable {
    
    private static final PseudoClass ERROR = PseudoClass.getPseudoClass("error");
    private static final PseudoClass NO_ERROR = PseudoClass.getPseudoClass("noerror");
    
    @FXML
    private Label label;
    @FXML
    private Button formatBtn;
    @FXML
    private Button clearBtn;
    @FXML
    private TextArea inputArea;
    @FXML
    private TextArea outputArea;
    @FXML
    private Text errorText;
    
    @FXML
    private void handleFormatButtonAction(ActionEvent event) {
        outputArea.clear();
        BarcodeQueryMaker bqm = new BarcodeQueryMaker(inputArea, outputArea);
        ParseResult result = bqm.parse();
        switch(result.getSoundness()){
            case ParseResult.NO_INPUT:
                    errorText.setFill(Color.RED);
                    errorText.setText("No input detected.");
                break;
            case ParseResult.NO_ERRORS:
                    errorText.setFill(Color.GREEN);
                    errorText.setText("No errors.");
                break;
            case ParseResult.SOME_ERRORS:
                    errorText.setFill(Color.RED);
                    StringBuilder builder = new StringBuilder();
                    builder.append("The following invalid barcodes were omitted from the query:\n");
                    for (String barcode : result.getErrorReport()){
                        builder.append(barcode).append("\n");
                    }
                    errorText.setText(builder.toString());
                break;
            case ParseResult.ALL_ERRORS:
                    errorText.setFill(Color.RED);
                    errorText.setText("None of the barcodes entered are valid.");
                break;
            default:
        }
        //outputArea.setText(input);
        
        //System.out.println("You clicked me!");
        //label.setText("Hello World!");
    }
    
    @FXML
    private void handleClearButtonAction(ActionEvent event) {
        inputArea.clear();
        outputArea.clear();
        errorText.setText("");
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
