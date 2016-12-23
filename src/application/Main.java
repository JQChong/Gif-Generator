package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import application.util.GifFrame;
import application.util.ImageExtractor;
import application.util.ImageUtil;
import application.view.Interface1;
import application.view.Interface2;
import application.view.Preview;
import application.view.TextDesign;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class Main extends Application {
	Stage primaryStage;
	BorderPane bp;

	@Override
	public void start(Stage primaryStage) {
		try {
			this.primaryStage = primaryStage;
			primaryStage.setTitle("GIF Generator");
			initRootLayout();
			showChooseFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showChooseFile() {
		try {
			FXMLLoader l = new FXMLLoader();
			l.setLocation(Main.class.getResource("view/interface.fxml"));
			AnchorPane ap = (AnchorPane) l.load();
			bp.setCenter(ap);

			Interface1 i1 = l.getController();
			i1.setMain(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean showEditDetails() {
		try {
			FXMLLoader l = new FXMLLoader();
			l.setLocation(Main.class.getResource("view/interface2.fxml"));
			AnchorPane ap = (AnchorPane) l.load();
			bp.setCenter(ap);

			Interface2 i2 = l.getController();
			i2.setMain(this);
			return i2.isPreviewClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void showPreview() {
		try {
			FXMLLoader l = new FXMLLoader();
			l.setLocation(Main.class.getResource("view/preview.fxml"));
			AnchorPane ap = (AnchorPane) l.load();
			bp.setCenter(ap);

			Preview p = l.getController();
			p.setMain(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean showTextDesign() {
		try {
			FXMLLoader l = new FXMLLoader();
			l.setLocation(Main.class.getResource("view/TextDesignLayout.fxml"));
			AnchorPane ap = (AnchorPane) l.load();

			Stage s = new Stage();
			s.setTitle("Design Text...");
			s.initModality(Modality.WINDOW_MODAL);
			s.initOwner(primaryStage);
			Scene scene = new Scene(ap);
			s.setScene(scene);

			TextDesign td = l.getController();
			td.setStage(s);

			s.showAndWait();

			return td.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private void initRootLayout() {
		try {
			FXMLLoader l = new FXMLLoader();
			l.setLocation(Main.class.getResource("view/layout.fxml"));
			bp = (BorderPane) l.load();

			Scene scene = new Scene(bp);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void save() {
		ArrayList<GifFrame> gf = new ArrayList<GifFrame>();
		for (BufferedImage bi : ImageExtractor.abi) {
			int transparentColor = 0xffffff;
			BufferedImage img = ImageUtil.convertRGBAToGIF(bi, transparentColor);
			long delay = (long) (1000.0 / ImageExtractor.frameRate);
			String disposal = GifFrame.RESTORE_TO_BGCOLOR;
			gf.add(new GifFrame(img, delay, disposal));
		}
		try {
			FileChooser fc = new FileChooser();
			fc.setTitle("Save GIF...");
			ExtensionFilter extFilter = new FileChooser.ExtensionFilter("GIF files (*.gif)", "*.gif");
			fc.getExtensionFilters().add(extFilter);
			File save = fc.showSaveDialog(primaryStage);
			if (save != null) {
				ImageUtil.saveAnimatedGIF(new FileOutputStream(save.getAbsolutePath()), gf, 0);
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("GIF Generator");
				alert.setHeaderText("Everything is ready.");
				alert.setContentText("Your GIF file is generated. Would you like to generate another file?");
				Optional<ButtonType> obt = alert.showAndWait();
				if (obt.get() == ButtonType.OK) {
					showChooseFile();
				} else {
					System.exit(0);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
