/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.agh.kiro.latex_plagiat.client.controller;

import pl.edu.agh.kiro.latex_plagiat.commons.database.DocumentType;
import pl.edu.agh.kiro.latex_plagiat.commons.protocol.plagiarism.FragmentsPair;
import pl.edu.agh.kiro.latex_plagiat.commons.protocol.plagiarism.PlagiarismFragment;
import pl.edu.agh.kiro.latex_plagiat.commons.protocol.plagiarism.PlagiarismResult;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
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
public class ArticleGridController implements Initializable {

    @FXML
    private GridPane inputArticleGrid;
    @FXML
    private TextArea inputTitle;
    @FXML
    private TextArea inputData;
    @FXML
    private TextArea inputKeywords;
    @FXML
    private Label currentArticleNr;
    @FXML
    private Label totalArticleCnt;
    @FXML
    private Label currentLineNr;
    @FXML
    private Label totalLineCnt;
    @FXML
    private TextArea outputData;
    @FXML
    private Button prevArticle, nextArticle;
    private List<List<PlagiarismResult>> allResults;
    private List<PlagiarismResult> allData = new LinkedList<PlagiarismResult>();
    private SimpleIntegerProperty currArticleIndex = new SimpleIntegerProperty(1), totalArticleIndex = new SimpleIntegerProperty(1);
    private SimpleIntegerProperty currLineIndex = new SimpleIntegerProperty(1), totalLineIndex = new SimpleIntegerProperty(1);

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        allResults = ResultSceneController.getSeparatedDocuments();
        if (allResults.size() > 0) {
            allData = separateArticles().get(0);
        }
        if (allData.size() > 0) {
            inputData.setText(allData.get(0).getNewDocument());
            outputData.setText(allData.get(0).getExistingDocument());
        }

        totalArticleIndex.set(allData.size());
        totalLineIndex.set(allData.get(0).getPlagiarisedFragments().size());
        Bindings.bindBidirectional(currentArticleNr.textProperty(),
                currArticleIndex,
                new NumberStringConverter());
        Bindings.bindBidirectional(totalArticleCnt.textProperty(),
                totalArticleIndex,
                new NumberStringConverter());
        Bindings.bindBidirectional(currentLineNr.textProperty(),
                currLineIndex,
                new NumberStringConverter());
        Bindings.bindBidirectional(totalLineCnt.textProperty(),
                totalLineIndex,
                new NumberStringConverter());
        inputTitle.setText(MainSceneController.getTitle());
        inputKeywords.setText(MainSceneController.getKeywords());

        colorSimilarSentence(allData.get(0), 0);
    }

    private List<List<PlagiarismResult>> separateArticles() {
        LinkedList<List<PlagiarismResult>> filteredArticles = new LinkedList<List<PlagiarismResult>>();
        List<PlagiarismResult> tempList = new LinkedList<PlagiarismResult>();
        for (List<PlagiarismResult> resultList : allResults) {
            System.out.println("resultListSize: " + resultList.size());
            tempList.clear();
            for (PlagiarismResult singleResult : resultList) {
                if (singleResult.getType().equals(DocumentType.TEXT)) {
                    tempList.add(singleResult);
                }
            }
            if (!tempList.isEmpty()) {
                filteredArticles.add(tempList);
            }
        }
        return filteredArticles;
    }

    @FXML
    private void handlePrevArticleAction() {
        if (currArticleIndex.get() > 1) {
            PlagiarismResult plagiarismResult = allData.get(currArticleIndex.get() - 2);
            currArticleIndex.set(currArticleIndex.get() - 1);
            outputData.setText(plagiarismResult.getExistingDocument());
            currLineIndex.set(1);
            totalLineIndex.set(plagiarismResult.getPlagiarisedFragments().size());
            colorSimilarSentence(plagiarismResult, 0);
        }
    }

    @FXML
    private void handleNextArticleAction() {
        if (currArticleIndex.get() < totalArticleIndex.get()) {
            PlagiarismResult plagiarismResult = allData.get(currArticleIndex.get());
            currArticleIndex.set(currArticleIndex.get() + 1);
            outputData.setText(plagiarismResult.getExistingDocument());
            currLineIndex.set(1);
            totalLineIndex.set(plagiarismResult.getPlagiarisedFragments().size());
            colorSimilarSentence(plagiarismResult, 0);
        }
    }

    @FXML
    private void handlePrevLineAction() {
        if (currLineIndex.get() > 1) {
            currLineIndex.set(currLineIndex.get() - 1);
            colorSimilarSentence(allData.get(currArticleIndex.get() - 1), currLineIndex.get() - 1);
        }
    }

    @FXML
    private void handleNextLineAction() {
        if (currLineIndex.get() < totalLineIndex.get()) {
            currLineIndex.set(currLineIndex.get() + 1);
            colorSimilarSentence(allData.get(currArticleIndex.get() - 1), currLineIndex.get() - 1);
        }
    }

    private void colorSimilarSentence(PlagiarismResult result, int  index) {
        inputData.setStyle("-fx-highlight-fill: orange; -fx-highlight-text-fill: firebrick;");
        outputData.setStyle("-fx-highlight-fill: red; -fx-highlight-text-fill: firebrick;");
        List<FragmentsPair> list = result.getPlagiarisedFragments();
        FragmentsPair pair = list.get(index);
        PlagiarismFragment fragment1 = pair.getInsertedFragment();
        PlagiarismFragment fragment2 = pair.getDatabaseFragment();

        inputData.selectRange(fragment1.getBegin(), fragment1.getEnd());
        outputData.selectRange(fragment2.getBegin(), fragment2.getEnd());
    }
}
