/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.utilities.functions;

import com.mycompany.utilities.utils.ParseResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

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

    String myText;
    ParseResult myParseResult;
    ClipboardContent myClipboardContent = new ClipboardContent();

    /**
     * Initialises a new <code>BarcodeQueryMaker</code> object and triggers
     * parsing.
     * 
     * @param text the String to be parsed
     */
    public BarcodeQueryMaker(String text) {
        myText = text;
        myParseResult = parse();
    }

    /**
     * Parses {@link #myText} into a valid JSON query for use with Sierra.
     *
     * If <code>myText</code> is empty the {@link ParseResult}'s
     * {@link ParseResult#soundness} is set to {@link ParseResult#NO_INPUT} and
     * it's {@link ParseResult#errorReport} to <code>null</code>. Else the
     * <code>soundness<\code> will be set to the respective
     * <code>ParseResult</code> constant and the <code>errorReport</code> either
     * be empty or contain all invalid barcodes encountered.
     *
     * @return the {@link com.mycompany.utilities.utils.ParseResult}
     */
    private ParseResult parse() {
        boolean lastLineFound = false;

        ArrayList<String> lines = new ArrayList<>();
        ArrayList<String> notBarcodes = new ArrayList<>();
        ParseResult parseResult = new ParseResult();
        // <editor-fold defaultstate="collapsed" desc="comment">
        /*Checks for an empty input. If found sets respective ParseResult 
            values and returns the ParseResult.*/
        //</editor-fold>
        if (myText.isEmpty()) {
            parseResult.setSoundness(ParseResult.NO_INPUT);
            parseResult.setErrorReport(null);
            return parseResult;
        } else {
            // <editor-fold defaultstate="collapsed" desc="comment">
            /*Splits the input String around line breaks into Substrings that 
            can be validated and parsed */
            // </editor-fold>
            String[] splitText = myText.split("\r?\n");
            lines.addAll(Arrays.asList(splitText));
            // <editor-fold defaultstate="collapsed" desc="comment">
            /*Loops through lines looking for valid String, starting at the end.
            As parsing between the last line and all others is different, this 
            approach prevents having to go back into the ArrayList and 
            changing the last entry if subsequently no other valid line is 
            found. 
            If a valid line is encountered it is stored at this point.
            Invalid last lines are added to the ArrayList of invalid lines and 
            removed from lines
            TODO: All errors encountered before first valid last line are listed first in reverse order, then all other errors encountered in correct order. Does that even matter?
             */
            // </editor-fold>
            while (!lines.isEmpty() && !lastLineFound) {
                int index = lines.size() - 1;
                String lastLine = lines.get(index);

                if (validateBarcode(lastLine)) {
                    // <editor-fold defaultstate="collapsed" desc="comment">
                    /*If a valid line is found lastLineFound is set to true to 
                    prevent while loop from running again.*/
                    // </editor-fold>
                    lastLineFound = true;
                    StringBuilder outBuilder = new StringBuilder();
                    outBuilder.append(BARCODE_PREFIX);
                    // <editor-fold defaultstate="collapsed" desc="comment">
                    /*Loops through lines, processing all Strings except the 
                    last as it needs different treatment*/
                    // </editor-fold>
                    for (int i = 0; i <= (lines.size() - 2); i++) {
                        String barcode = lines.get(i);
                        // <editor-fold defaultstate="collapsed" desc="comment">
                        /*validate String, only parse and add to StringBuilder 
                        if the String is valid, else add to ArraryList of  
                        invalid Strings*/
                        // </editor-fold>
                        if (validateBarcode(barcode)) {
                            outBuilder.append(buildWithComma(barcode));
                        } else {
                            notBarcodes.add(barcode);
                        }
                    }
                    // <editor-fold defaultstate="collapsed" desc="comment">                    
                    /*parse the last line and add to StringBuilder. Add suffix.*/
                    //</editor-fold>
                    outBuilder.append(buildWithoutComma(lastLine));
                    outBuilder.append(BARCODE_SUFFIX);
                    parseResult.setParsedString(outBuilder.toString());
                } else {
                    notBarcodes.add(lastLine);
                    lines.remove(index);
                }
            }

            // <editor-fold defaultstate="collapsed" desc="comment">
            /*No valid barcodes were found. ParseResult soundness set to 
            corresponding value*/
            //</editor-fold>
            if (!lastLineFound) {
                parseResult.setSoundness(ParseResult.ALL_ERRORS);
            } else {
                if (notBarcodes.isEmpty()) {
                    // <editor-fold defaultstate="collapsed" desc="comment">
                    /*All barcodes are valid. ParseResult soundness set to 
                    corresponding value*/
                    //</editor-fold>
                    parseResult.setSoundness(ParseResult.NO_ERRORS);
                } else {
                    // <editor-fold defaultstate="collapsed" desc="comment">
                    /*Some barcodes were invalid. ParseResult soundness set to
                    corresponding value.*/
                    //</editor-fold>
                    parseResult.setSoundness(ParseResult.SOME_ERRORS);
                }
            }
            // <editor-fold defaultstate="collapsed" desc="comment">
            /*Set list of invalid barcoes as ParseReslt errorReport.*/
            //</editor-fold>
            parseResult.setErrorReport(notBarcodes);

            return parseResult;
        }
    }

    /**
     * Validates a barcode.
     *
     * Valid barcodes consists of either 10 or 14 characters, all of which need
     * to be either 0-9 or x or X.
     *
     * @param string the barcode to be validated.
     * @return <code>true</code> if barcode is valid.
     */
    private static boolean validateBarcode(String string) {
        Pattern pattern10 = Pattern.compile("[xX0-9]{10}");
        Pattern pattern14 = Pattern.compile("[0-9]{14}");
        Matcher matcher10 = pattern10.matcher(string);
        Matcher matcher14 = pattern14.matcher(string);
        System.out.println("Matcher10: " + matcher10.matches());
        System.out.println("Matcher14: " + matcher14.matches());
        return matcher10.matches() || matcher14.matches();
    }

    /**
     * Parses the JSON for a valid barcode that is not the last to be parsed.
     *
     * If the barcode is not not the last in the list of barcodes to be parsed
     * it needs a comma inserted between the surrounding "" and carriage
     * return/line break. E.g. 1918111445 becomes "1918111445",\r\n
     *
     * @param string the barcode to be parsed
     * @return the parsed barcode
     */
    private static String buildWithComma(String string) {
        StringBuilder builder = new StringBuilder();
        builder.append("\"").append(string).append("\",\r\n");
        String out = builder.toString();
        System.out.println(out);
        return out;
    }

    /**
     * Parses the JSON for the last valid barcode to be parsed.
     *
     * If the barcode is the last to be parsed no comma is added between the
     * surrounding "" and carriage return/line break. E.g. 1918111445 becomes
     * "1918111445"\r\n
     *
     * @param string the barcode to the parsed
     * @return the parsed barcode
     */
    private static String buildWithoutComma(String string) {
        StringBuilder builder = new StringBuilder();
        builder.append("\"").append(string).append("\"\r\n");
        String out = builder.toString();
        System.out.println(out);
        return out;
    }

    /**
     * Gets the parsed JSON query.
     *
     * If no valid barcodes were entered or found this is <code>null</code>.
     *
     * @return The parsed JSON query
     */
    public String getParsedString() {
        return myParseResult.getParsedString();
    }

    /**
     * Gets the error status of the {@link ParseResult}.
     *
     * @return Returns <code>true</code> if no errors occurred during parsing.
     */
    public boolean getErrorStatus() {
        return myParseResult.getSoundness() == ParseResult.NO_ERRORS;
    }

    /**
     * Gets the error report.
     *
     * If any invalid barcodes were encountered during parsing they are included
     * in this report.
     * 
     * @param language the {@link ResourceBundle} for the language the report
 is to be compiled in.
     *
     * @return the error report
     */
    public String getErrorDetail(ResourceBundle language) {
        switch (myParseResult.getSoundness()) {
            case ParseResult.NO_INPUT:
                return language.getString("NO_INPUT");
            case ParseResult.NO_ERRORS:
                return language.getString("NO_ERROR");
            case ParseResult.SOME_ERRORS:
                StringBuilder builder = new StringBuilder();
                builder.append(language.getString("SOME_ERRORS")).append("\n");
                for (String barcode : myParseResult.getErrorReport()) {
                    builder.append(barcode).append("\n");
                }
                return builder.toString();
            case ParseResult.ALL_ERRORS:
                return language.getString("NO_VALID_INPUT");
            default:
                return null;
        }
    }
}
