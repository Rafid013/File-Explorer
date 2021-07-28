package FileExplorerPackage;


import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;

/**
 * Created by rafid on 18/4/2017.
 */
public class FileInfo {
    private File file;
    private String fileName;
    private String fileModifiedDate;
    private String fileAbsolutePath;
    private long fileSize;
    private ObservableList<FileInfo> childFiles;
    private ImageView fileImage;


    public FileInfo(String fileAbsolutePath){
        file = null;
        this.fileAbsolutePath = fileAbsolutePath;
    }

    public FileInfo(File file) {
        this.file = file;
        fileName = file.getName();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        fileModifiedDate = simpleDateFormat.format(file.lastModified());
        fileAbsolutePath = file.getAbsolutePath();
        fileSize = file.length();
        ImageIcon icon = (ImageIcon) FileSystemView.getFileSystemView().getSystemIcon(file);
        BufferedImage buffImage = (BufferedImage)icon.getImage();
        WritableImage fxImage = new WritableImage(icon.getIconWidth(), icon.getIconHeight());
        Image image = SwingFXUtils.toFXImage(buffImage, fxImage);
        fileImage = new ImageView(image);
    }

    public String getFileAbsolutePath() {
        return fileAbsolutePath;
    }

    public void setFileAbsolutePath(String fileAbsolutePath) {
        this.fileAbsolutePath = fileAbsolutePath;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileModifiedDate() {
        return fileModifiedDate;
    }

    public void setFileModifiedDate(String fileModifiedDate) {
        this.fileModifiedDate = fileModifiedDate;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public void setChildFiles(ObservableList<FileInfo> childFiles) {
        this.childFiles = childFiles;
    }

    public ObservableList<FileInfo> getChildFiles() {
        return childFiles;
    }

    public ImageView getFileImage() {
        return fileImage;
    }

    public void setFileImage(ImageView fileImage) {
        this.fileImage = fileImage;
    }

    @Override
    public String toString() {
        return fileAbsolutePath;
    }

}
