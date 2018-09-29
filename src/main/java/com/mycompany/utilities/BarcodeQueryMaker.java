/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.TextArea;

/**
 *
 * @author fran
 */
public class BarcodeQueryMaker {

    final static String BARCODE_PREFIX = "{\n"
            + "  \"queries\": [\n"
            + "    {\n"
            + "      \"target\": {\n"
            + "        \"record\": {\n"
            + "          \"type\": \"item\"\n"
            + "        },\n"
            + "        \"field\": {\n"
            + "          \"tag\": \"b\"\n"
            + "        }\n"
            + "      },\n"
            + "      \"expr\": [\n"
            + "        {\n"
            + "          \"op\": \"in\",\n"
            + "          \"operands\": [\n";
    final static String BARCODE_SUFFIX = "]\n"
            + "        }\n"
            + "      ]\n"
            + "    }\n"
            + "  ]\n"
            + "}";

    TextArea myIn;
    TextArea myOut;
    String myText;
    boolean lastLineFound = false;

    BarcodeQueryMaker(TextArea in, TextArea out) {
        myIn = in;
        myOut = out;
        myText = myIn.getText();
    }

    ParseResult parse() {

        ArrayList<String> lines = new ArrayList<>();
        ArrayList<String> notBarcodes = new ArrayList<>();
        ParseResult parseResult = new ParseResult();
        
        if (myText.isEmpty()){
            parseResult.setSoundness(ParseResult.NO_INPUT);
            parseResult.setErrorReport(null);
            return parseResult;
        }else{
        
        String[] splitText = myText.split("\r?\n");
        lines.addAll(Arrays.asList(splitText));
        System.out.println("Array size: " + lines.size());
        
        
        while (!lines.isEmpty() && !lastLineFound) {
            int index = lines.size() - 1;
            String lastLine = lines.get(index);
            if (validateBarcode(lastLine)) {
                lastLineFound = true;
                myOut.appendText(BARCODE_PREFIX);
                for (int i = 0; i <= (lines.size() - 2); i++) {
                    String barcode = lines.get(i);
                    if (validateBarcode(barcode)) {
                        myOut.appendText(buildWithComma(barcode));
                    } else {
                        notBarcodes.add(barcode);
                    }
                }
                myOut.appendText(buildWithoutComma(lastLine));
                myOut.appendText(BARCODE_SUFFIX);
            }else{
                notBarcodes.add(lastLine);
                lines.remove(index);
            }
        }
        
        if (!lastLineFound){
            parseResult.setSoundness(ParseResult.ALL_ERRORS);
        }else {
            if (notBarcodes.isEmpty()){
                parseResult.setSoundness(ParseResult.NO_ERRORS);
            }else{
                parseResult.setSoundness(ParseResult.SOME_ERRORS);
            }
        }
        parseResult.setErrorReport(notBarcodes);
        
        return parseResult;
        }
    }

    private static boolean validateBarcode(String string) {
        Pattern pattern10 = Pattern.compile("[xX0-9]{10}");
        Pattern pattern14 = Pattern.compile("[0-9]{14}");
        Matcher matcher10 = pattern10.matcher(string);
        Matcher matcher14 = pattern14.matcher(string);
        System.out.println("Matcher10: " + matcher10.matches());
        System.out.println("Matcher14: " + matcher14.matches());
        return matcher10.matches() || matcher14.matches();
    }

    private static String buildWithComma(String string) {
        StringBuilder builder = new StringBuilder();
        builder.append("\"").append(string).append("\",\r\n");
        String out = builder.toString();
        System.out.println(out);
        return out;
    }

    private static String buildWithoutComma(String string) {
        StringBuilder builder = new StringBuilder();
        builder.append("\"").append(string).append("\"\r\n");
        String out = builder.toString();
        System.out.println(out);
        return out;
    }

}
