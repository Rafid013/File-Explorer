package FileExplorerPackage;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;

public class Controller implements Initializable {


    @FXML
    public TreeView<FileInfo> fileTreeView;

    @FXML
    public TableView<FileInfo> fileTableView;

    @FXML
    public TableColumn<FileInfo, ImageView> iconColumn;

    @FXML
    public TableColumn<FileInfo, String> nameColumn;

    @FXML
    public TableColumn<FileInfo, Long> sizeColumn;

    @FXML
    public TableColumn<FileInfo, String> dateColumn;

    @FXML
    public TextField filePath;

    @FXML
    public StackPane stackPane;

    @FXML
    public TilePane fileTilesView;

    public FileInfo currentFile;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //get current folder
        currentFile = new FileInfo(new File(System.getProperty("user.dir")));
        //make Table
        setTableColumns();
        setFileTableView(currentFile);
        //set table event handler
        setTableAction();
        //make tiles
        setFileTilesView(currentFile);
        //make Tree
        makeTree();
        //set tree event handler
        setTreeAction();
        //show File Path
        filePath.setText(currentFile.getFileAbsolutePath());
    }



    private void setFileTableView(FileInfo fileInfo) {
        ObservableList<FileInfo> tableInfo;
        fileInfo.setChildFiles(getChildFiles(fileInfo));
        tableInfo = fileInfo.getChildFiles();
        tableInfo.sort(new Comparator<FileInfo>() {
            @Override
            public int compare(FileInfo o1, FileInfo o2) {
                if(o1.getFileSize() < o2.getFileSize()) return -1;
                else return 1;
            }
        });
        fileTableView.getItems().clear();
        fileTableView.setItems(tableInfo);
    }

    private void setTilesAction(FileInfo fileInfo, VBox vBox) {
        vBox.setOnMousePressed(event -> {
            if(fileInfo.getFile().isDirectory() && event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                currentFile = fileInfo;
                setFileTilesView(currentFile);
                setFileTableView(currentFile);
                filePath.setText(currentFile.getFileAbsolutePath());
            }
        });
    }

    private void setFileTilesView(FileInfo fileInfo) {
        ObservableList<FileInfo> tileInfo;
        fileInfo.setChildFiles(getChildFiles(fileInfo));
        tileInfo = fileInfo.getChildFiles();
        fileTilesView.getChildren().clear();
        for(FileInfo f : tileInfo) {
            ImageView temp = new ImageView(f.getFileImage().getImage());
            temp.setFitWidth(30);
            temp.setFitHeight(20);
            VBox vBox = new VBox();
            vBox.getChildren().add(temp);
            char[] tempStr = new char[6];
            if(f.getFileName().length() >= 5) {
                for(int i = 0; i < 4; ++i) tempStr[i] = f.getFileName().charAt(i);
                tempStr[4] = tempStr[5] = '.';
                vBox.getChildren().add(new Text(new String(tempStr)));
            }
            else vBox.getChildren().add(new Text(f.getFileName()));
            setTilesAction(f, vBox);
            fileTilesView.getChildren().add(vBox);
        }
    }
    private void makeTree() {
        FileInfo fileInfo = new FileInfo("My PC");
        TreeItem<FileInfo> root = new TreeItem<>(fileInfo);
        root.setExpanded(true);
        File[] drives = File.listRoots();
        for(File f : drives) {
            ImageIcon icon = (ImageIcon) FileSystemView.getFileSystemView().getSystemIcon(f);
            BufferedImage buffImage = (BufferedImage)icon.getImage();
            WritableImage fxImage = new WritableImage(icon.getIconWidth(), icon.getIconHeight());
            Image image = SwingFXUtils.toFXImage(buffImage, fxImage);
            root.getChildren().add(new TreeItem<>(new FileInfo(f), new ImageView(image)));
        }
        fileTreeView.setShowRoot(false);
        fileTreeView.setRoot(root);
    }

    private void setTableColumns() {
        iconColumn.setCellValueFactory(new PropertyValueFactory<>("fileImage"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("fileSize"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("fileModifiedDate"));
    }

    private void setTableAction() {
        fileTableView.setOnMousePressed(event -> {
            if(event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                FileInfo fileInfoTemp = fileTableView.getSelectionModel().getSelectedItem();
                if(fileInfoTemp.getFile().isDirectory()) {
                    currentFile = fileInfoTemp;
                    setFileTilesView(currentFile);
                    setFileTableView(currentFile);
                    filePath.setText(currentFile.getFileAbsolutePath());
                }
            }
        });
    }

    private void setTreeAction() {
        fileTreeView.setOnMousePressed(event -> {
            if(event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                TreeItem<FileInfo> selectedNode = fileTreeView.getSelectionModel().getSelectedItem();
                FileInfo fileInfoTemp = selectedNode.getValue();
                if (fileInfoTemp.getFile().isDirectory()) {
                    fileInfoTemp.setChildFiles(getChildFiles(fileInfoTemp));
                    ObservableList<FileInfo> childList = fileInfoTemp.getChildFiles();
                    if (selectedNode.getChildren().size() == 0) {
                        for (FileInfo f : childList) {
                            if (f.getFile().isDirectory())
                                selectedNode.getChildren().add(new TreeItem<>(f, f.getFileImage()));
                        }
                    }
                    currentFile = fileInfoTemp;
                    setFileTableView(currentFile);
                    setFileTilesView(currentFile);
                    filePath.setText(currentFile.getFileAbsolutePath());
                }
            }
        });
    }

    public ObservableList<FileInfo> getChildFiles(FileInfo file){
        File[] childList = file.getFile().listFiles();
        ObservableList<FileInfo> list = FXCollections.observableArrayList();
        if(childList != null) {
            for(File f : childList){
                list.add(new FileInfo(f));
            }
            return list;
        }
        else return null;
    }
}
