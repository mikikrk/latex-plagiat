package pl.edu.agh.kiro.latex_plagiat.client.controller;

import pl.edu.agh.kiro.latex_plagiat.client.model.ClientModel;
import pl.edu.agh.kiro.latex_plagiat.client.model.exceptions.CannotConnectToTheServerException;
import pl.edu.agh.kiro.latex_plagiat.client.model.factories.ClientFactory;
import pl.edu.agh.kiro.latex_plagiat.client.view.ViewUtils;
import pl.edu.agh.kiro.latex_plagiat.commons.protocol.ApplicationProperties;
import pl.edu.agh.kiro.latex_plagiat.commons.protocol.DocumentData;
import pl.edu.agh.kiro.latex_plagiat.commons.protocol.Message;
import pl.edu.agh.kiro.latex_plagiat.commons.protocol.ProtocolCode;
import pl.edu.agh.kiro.latex_plagiat.commons.protocol.plagiarism.PlagiarismDetectionResult;
import pl.edu.agh.kiro.latex_plagiat.commons.protocol.plagiarism.PlagiarismResult;
import pl.edu.agh.kiro.latex_plagiat.commons.util.TextUtils;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

public class MainSceneController implements Initializable, Controller, Observer {

    private static final ClientFactory clientFactory = new ClientFactory();
    private static Stage mainWindow = new Stage();
    @FXML
    Button button, clearButton;
    @FXML
    Button checkButton, codeReviewButton;
    private ClientModel model;
    private static String hostname = ApplicationProperties.HOSTNAME;
    private static int port = ApplicationProperties.PORT;
    static List<PlagiarismResult> allResults = new LinkedList<PlagiarismResult>();
    @FXML
    private Label label, appTitle, currentArticleNr, totalArticleCnt;
    @FXML
    private TextArea inputData, inputTitle, inputKeywords, inputCode;

    private List<String> codeList = new ArrayList<String>();
    private SimpleIntegerProperty currCodeIndex = new SimpleIntegerProperty(1), totalCodeIndex = new SimpleIntegerProperty(1);
    private Stage progressStage = new Stage();
    private static String articleTitle = new String(""), articleKeywords = new String("");

    public static void showMainWindow() {
        mainWindow.show();
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {

        final DocumentData documentData = getViewDocumentData();
        if (validate(documentData)) {

            try {
//                showProgressScene();

                ExecutorService executor = Executors.newFixedThreadPool(1);

                FutureTask<Object> task = new FutureTask(new Callable() {
                    @Override
                    public Object call() throws CannotConnectToTheServerException {
                        model.openConnection();
                        Message message = new Message(ProtocolCode.CHECK_FOR_PLAGIARISM, documentData);
                        model.sendMessage(message);
                        model.closeConnection();
                        return null;
                    }
                });

                Future<?> submit = executor.submit(task);
                task.get();
//                hideProgressScene();
                if (allResults.isEmpty()) {
                    showNotPlagiarismScene();
                } else {
                    articleTitle = documentData.getTitle();
                    articleKeywords = documentData.getKeywordsJoined();
                    mainWindow = (Stage) checkButton.getScene().getWindow();
                    mainWindow.hide();
                    showResultScene();
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                ViewUtils.showErrorDialog("Error", "Connection error", "Server is down or there are other issues with connection!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            ViewUtils.showWarningDialog("Validation error", "Data not valid", "Insert valid data before you proceed");
        }
    }

    private DocumentData getViewDocumentData() {
        String articleText = inputData.getText();
        String title = inputTitle.getText();
        String keywords = inputKeywords.getText();
        Set<String> keywordsSet = new HashSet<String>();
        if (!keywords.isEmpty()) {
            keywordsSet = TextUtils.splitIntoSet(keywords);
        }
        Set<String> codes = new HashSet<String>();
        for (String code : codeList) {
            if (!code.equals("") && code != null) {
                codes.add(code);
            }
        }

        return new DocumentData(title, keywordsSet, articleText, codes);
    }

    @FXML
    private void handleChangeServerButtonAction(ActionEvent event) {
        Parent root;
        System.out.println("You clicked me, bastard!");
        URL url = getClass().getResource("/fxml/ServerSettings.fxml");
        try {
            root = FXMLLoader.load(url);
            Stage stage = new Stage();
            stage.setTitle("Server settings");
            Scene scene = new Scene(root, 500, 200);
            scene.getStylesheets().add("/styles/Styles.css");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClearButtonAction(ActionEvent event) {
        inputData.setText(null);
        inputTitle.setText(null);
        inputKeywords.setText(null);
        inputCode.setText(null);
        codeList.clear();
        codeList.add(null);
        System.out.println(codeList.size());
        currCodeIndex.set(1);
        totalCodeIndex.set(1);
    }

    @FXML
    private void handleAddNewCodeAction() {
        currCodeIndex.set(currCodeIndex.get() + 1);
        totalCodeIndex.set(totalCodeIndex.get() + 1);
        codeList.add(currCodeIndex.get() - 1, null);
        inputCode.setText(null);
        System.out.println(totalCodeIndex.get());
    }

    @FXML
    private void handlePrevCodeAction() {
        if (currCodeIndex.get() > 1) {
            currCodeIndex.set(currCodeIndex.get() - 1);
            inputCode.setText(codeList.get(currCodeIndex.get() - 1));
        }
    }

    @FXML
    private void handleNextCodeAction() {
        if (currCodeIndex.get() < totalCodeIndex.get()) {
            currCodeIndex.set(currCodeIndex.get() + 1);
            inputCode.setText(codeList.get(currCodeIndex.get() - 1));
        }
    }

    @FXML
    private void handleRemoveCodeAction() {
        if (totalCodeIndex.get() > 1) {
            codeList.remove(currCodeIndex.get() - 1);
            if (currCodeIndex.get() == totalCodeIndex.get()) {
                currCodeIndex.set(currCodeIndex.get() - 1);
            }
            inputCode.setText(codeList.get(currCodeIndex.get() - 1));
            totalCodeIndex.set(totalCodeIndex.get() - 1);

        }

    }

    private void showResultScene() throws IOException {
        Parent root;
        URL url = getClass().getResource("/fxml/ResultScene.fxml");
        root = FXMLLoader.load(url);
        Stage stage = new Stage();
        stage.setTitle("Result");
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add("/styles/Styles.css");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void showNotPlagiarismScene() throws IOException {
        Parent root;
        URL url = getClass().getResource("/fxml/NotPlagiarismScene.fxml");
        root = FXMLLoader.load(url);
        Stage stage = new Stage();
        stage.setTitle("Plagiarism detection result");
        Scene scene = new Scene(root, 300, 150);
        scene.getStylesheets().add("/styles/Styles.css");
        stage.setScene(scene);
        stage.show();
    }

    private void showProgressScene() throws IOException {
        Parent root;
        URL url = getClass().getResource("/fxml/ProcessingScene.fxml");
        root = FXMLLoader.load(url);
        progressStage.setTitle("Processing");
        Scene scene = new Scene(root, 300, 150);
        scene.getStylesheets().add("/styles/Styles.css");
        progressStage.setScene(scene);
        progressStage.show();
    }

    private void hideProgressScene() {
        progressStage.hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = createModel();
        /*Bindings.bindBidirectional(currentArticleNr.textProperty(),
                currCodeIndex,
                new NumberStringConverter());
        Bindings.bindBidirectional(totalArticleCnt.textProperty(),
                totalCodeIndex,
                new NumberStringConverter());
        codeList.add("");
        inputCode.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(final ObservableValue<? extends Boolean> observable, final Boolean oldValue, final Boolean newValue) {
                if (!newValue) {
                    codeList.set(currCodeIndex.get() - 1, inputCode.getText());
                }
            }
        });*/
    }

    private ClientModel createModel() {
        ClientModel model = new ClientModel(clientFactory);
        model.addObserver(this);
        return model;
    }

    public static void setServerAndPort(String _hostname, int _port) {
        hostname = _hostname;
        port = _port;
    }

    public static String getHostname() {
        return hostname;
    }

    public static int getPort() {
        return port;
    }

    /**
     * Metoda która obsługuje komunikaty przychodzące od serwera
     *
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        Message message = (Message) arg;

        ProtocolCode code = message.getCode();
        if (code == ProtocolCode.PLAGIARISM_CHECK_RESULT) {
            PlagiarismDetectionResult plagiarismDetectionResult = (PlagiarismDetectionResult) message.getSentObject();
            allResults = plagiarismDetectionResult.getAllResults();
        }
    }

    public static List<PlagiarismResult> getAllResults() {
        return allResults;
    }

    private boolean validate(DocumentData data) {
        if (data.getTitle().equals("") || data.getTitle() == null) {
            return false;
        }
        if (data.getKeywordsJoined().equals("") || data.getKeywordsJoined() == null) {
            return false;
        }
        String article = data.getArticle();
        int codeCnt = data.getCodesCount();
        if ((article.equals("") || article == null) && codeCnt == 0) {
            return false;
        }
        return true;
    }
    public static String getTitle() {
        return articleTitle;
    }
    public static String getKeywords() {
        return articleKeywords;
    }
}
