package com.mycompany.utilities;

import com.mycompany.utilities.functions.BarcodeQueryMaker;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

public class FXMLController implements Initializable {

    //private static final PseudoClass ERROR = PseudoClass.getPseudoClass(java.util.ResourceBundle.getBundle("lang/test").getString("ERROR"));
    //private static final PseudoClass NO_ERROR = PseudoClass.getPseudoClass(java.util.ResourceBundle.getBundle("lang/test").getString("NOERROR"));
    @FXML
    private TextArea bqmInputArea;
    @FXML
    private TextArea bqmOutputArea;
    @FXML
    private Text bqmErrorText;
    @FXML
    private Text infoText;

    @FXML
    private void handleBQMFormatButtonAction(ActionEvent event) {
        bqmOutputArea.clear();
        BarcodeQueryMaker bqm = new BarcodeQueryMaker(bqmInputArea.getText());
        bqmOutputArea.setText(bqm.getParsedString());
        bqmErrorText.setFill(bqm.getErrorStatus());
        bqmErrorText.setText(bqm.getErrorReport());
    }

    @FXML
    private void handleBQMClearButtonAction(ActionEvent event) {
        bqmInputArea.clear();
        bqmOutputArea.clear();
        bqmErrorText.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
