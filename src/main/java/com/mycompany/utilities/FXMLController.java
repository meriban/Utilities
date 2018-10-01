package com.mycompany.utilities;

import com.mycompany.utilities.controls.FunctionButton;
import com.mycompany.utilities.functions.BarcodeQueryMaker;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class FXMLController implements Initializable {

//    @FXML
//    private FunctionButton bqmButton;
    @FXML
    private StackPane stackPane;
    @FXML
    private Text bqmErrorText, infoText;
    @FXML
    private TextArea bqmInputArea, bqmOutputArea;
    @FXML
    private TitledPane bqmPane, anotherPane;
    @FXML
    private ToggleButton bqmButton, anotherButton;
  
    
    final private Clipboard clipboard = Clipboard.getSystemClipboard();
    private ClipboardContent clipboardContent = new ClipboardContent();
    private static ResourceBundle language = ResourceBundle.getBundle("lang/eng");
    private BarcodeQueryMaker bqm = null;
    private final ToggleGroup functions = new ToggleGroup();

//    @FXML
//    private void handleBQMButtonAction(ActionEvent event){
//        if (!bqmButton.isRunning()){
//            for (FunctionButton button : functionButtons){
//                button.setRunning(false);
//            }
//            bqmButton.setRunning(true);
//            //TODO: show correct pane
//        }
//    }
    
    @FXML
    private void handleBQMFormatButtonAction(ActionEvent event) {
        bqmOutputArea.clear();
        bqm = new BarcodeQueryMaker(bqmInputArea.getText());
        String parsedString = bqm.getParsedString();
        bqmOutputArea.setText(parsedString);
        if (bqm.getErrorStatus()) {
            bqmErrorText.setFill(Color.GREEN);
        } else {
            bqmErrorText.setFill(Color.RED);
        }
        bqmErrorText.setText(bqm.getErrorDetail(language));

        bqm.queryToClipboard(clipboard, parsedString);
        infoText.setText(language.getString("BQM_QUERY_CLIPBOARD"));
    }

    @FXML
    private void handleBQMClearButtonAction(ActionEvent event) {
        bqmInputArea.clear();
        bqmOutputArea.clear();
        bqmErrorText.setText("");
    }

    @FXML
    private void handleBQMQueryToClipboardButtonAction(ActionEvent event) {
        clipboardContent.clear();
        clipboardContent.putString(bqmOutputArea.getText());
        clipboard.setContent(clipboardContent);
        infoText.setText(language.getString("BQM_QUERY_CLIPBOARD"));
    }

    @FXML
    private void handleBQMErrorReportToClipboardButtonAction(ActionEvent event) {
        clipboardContent.clear();
        clipboardContent.putString(bqmErrorText.getText());
        infoText.setText(language.getString("BQM_ERROR_REPORT_CLIPBOARD"));
        clipboard.setContent(clipboardContent);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bqmButton.setToggleGroup(functions);
        anotherButton.setToggleGroup(functions);
        //stackPane.getChildren().addAll(bqmPane, anotherPane);
        //bqmButton.pseudoClassStateChanged(, true);
    }

}
