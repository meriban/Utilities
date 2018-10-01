package com.mycompany.utilities;

import com.mycompany.utilities.functions.BarcodeQueryMaker;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class FXMLController implements Initializable {
    
    @FXML
    private Text bqmErrorText;
    @FXML
    private Text infoText;
    @FXML
    private TextArea bqmInputArea;
    @FXML
    private TextArea bqmOutputArea;
    
    final private Clipboard clipboard = Clipboard.getSystemClipboard();
    private ClipboardContent clipboardContent = new ClipboardContent();
    private static ResourceBundle language = ResourceBundle.getBundle("lang/eng");
    private BarcodeQueryMaker bqm = null;
    
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

        clipboardContent.clear();
        clipboardContent.putString(bqmOutputArea.getText());
        clipboard.setContent(clipboardContent);
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

    }

}
