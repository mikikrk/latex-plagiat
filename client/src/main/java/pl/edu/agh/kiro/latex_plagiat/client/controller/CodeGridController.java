/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.agh.kiro.latex_plagiat.client.controller;

import pl.edu.agh.kiro.latex_plagiat.commons.database.DocumentType;
import pl.edu.agh.kiro.latex_plagiat.commons.protocol.plagiarism.PlagiarismFragment;
import pl.edu.agh.kiro.latex_plagiat.commons.protocol.plagiarism.PlagiarismResult;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.util.converter.NumberStringConverter;

/**
 * FXML Controller class
 *
 * @author Agat
 */
public class CodeGridController implements Initializable {

    @FXML
    private GridPane inputArticleGrid;
    @FXML
    private Button prevCodeOut;
    @FXML
    private Button nextCodeOut;
    @FXML
    private Label currentCodeNrOut;
    @FXML
    private Label totalCodeCntOut;
    @FXML
    private TextArea foundCode;
    @FXML
    private Button prevCodeIn;
    @FXML
    private Button nextCodeIn;
    @FXML
    private Label currentCodeNrIn;
    @FXML
    private Label totalCodeCntIn;
    @FXML
    private TextArea newCode;
    private List<List<PlagiarismResult>> allResults;
    List<PlagiarismResult> currentCodeList;
    private SimpleIntegerProperty currCodeIndexIn = new SimpleIntegerProperty(1), totalCodeIndexIn = new SimpleIntegerProperty(1);
    private SimpleIntegerProperty currCodeIndexOut = new SimpleIntegerProperty(1), totalCodeIndexOut = new SimpleIntegerProperty(1);

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        allResults = ResultSceneController.getSeparatedDocuments();
        allResults = separateCodes();
        if (allResults.size() > 0) {
            currentCodeList = allResults.get(0);
        }
        if (currentCodeList!=null && currentCodeList.size() > 0) {
            newCode.setText(currentCodeList.get(0).getNewDocument());
            foundCode.setText(currentCodeList.get(0).getExistingDocument());
            totalCodeIndexIn.set(allResults.size());
            totalCodeIndexOut.set(currentCodeList.size());
        }else{
        	newCode.setText("");
        	foundCode.setText("");
        	totalCodeIndexIn.set(0);
            totalCodeIndexOut.set(0);
        }
        Bindings.bindBidirectional(currentCodeNrIn.textProperty(),
                currCodeIndexIn,
                new NumberStringConverter());
        Bindings.bindBidirectional(totalCodeCntIn.textProperty(),
                totalCodeIndexIn,
                new NumberStringConverter());
        Bindings.bindBidirectional(currentCodeNrOut.textProperty(),
                currCodeIndexOut,
                new NumberStringConverter());
        Bindings.bindBidirectional(totalCodeCntOut.textProperty(),
                totalCodeIndexOut,
                new NumberStringConverter());
    }

    private List<List<PlagiarismResult>> separateCodes() {
        LinkedList<List<PlagiarismResult>> filteredArticles = new LinkedList<List<PlagiarismResult>>();
        List<PlagiarismResult> tempList = new LinkedList<PlagiarismResult>();
        for (List<PlagiarismResult> resultList : allResults) {
            System.out.println(resultList.toString());
            tempList.clear();
            for (PlagiarismResult singleResult : resultList) {
                if (singleResult.getType().equals(DocumentType.CODE)) {
                    tempList.add(singleResult);
                }
            }
            if (!tempList.isEmpty()){
            	filteredArticles.add(tempList);
            }
        }
        return filteredArticles;
    }

    @FXML
    private void handlePrevCodeOutAction(ActionEvent event) {
        if (currCodeIndexOut.get() > 1) {
            currCodeIndexOut.set(currCodeIndexOut.get() - 1);
            foundCode.setText(currentCodeList.get(currCodeIndexOut.get() - 1).getExistingDocument());
        }
    }

    @FXML
    private void handleNextCodeOutAction(ActionEvent event) {
        if (currCodeIndexOut.get() < totalCodeIndexOut.get()) {
            currCodeIndexOut.set(currCodeIndexOut.get() + 1);
            foundCode.setText(currentCodeList.get(currCodeIndexOut.get() - 1).getExistingDocument());
        }
    }

    @FXML
    private void handlePrevCodeInAction(ActionEvent event) {
        if (currCodeIndexIn.get() > 1) {
            currCodeIndexIn.set(currCodeIndexIn.get() - 1);
            currentCodeList = allResults.get(currCodeIndexIn.get() - 1);
            currCodeIndexOut.set(1);
            totalCodeIndexOut.set(currentCodeList.size());
            newCode.setText(currentCodeList.get(0).getNewDocument());
            foundCode.setText(currentCodeList.get(0).getExistingDocument());
        }
    }

    @FXML
    private void handleNextCodeInAction(ActionEvent event) {
        if (currCodeIndexIn.get() < totalCodeIndexIn.get()) {
            currCodeIndexIn.set(currCodeIndexIn.get() + 1);
            currentCodeList = allResults.get(currCodeIndexIn.get() - 1);
            currCodeIndexOut.set(1);
            totalCodeIndexOut.set(currentCodeList.size());
            newCode.setText(currentCodeList.get(0).getNewDocument());
            foundCode.setText(currentCodeList.get(0).getExistingDocument());
        }
    }
    
    private void colorSimilarSentences(PlagiarismResult result) {
        newCode.setStyle("-fx-highlight-fill: orange; -fx-highlight-text-fill: firebrick;");
        foundCode.setStyle("-fx-highlight-fill: red; -fx-highlight-text-fill: firebrick;");
        Map<PlagiarismFragment, PlagiarismFragment> map = result.getPlagiarisedFragments();
        for (PlagiarismFragment inValue : map.keySet()) {
            newCode.selectRange(inValue.getBegin(), inValue.getEnd());
        }
        for (PlagiarismFragment outValue : map.values()) {
            foundCode.selectRange(outValue.getBegin(), outValue.getEnd());
        }
    }
}
