package application.view;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import application.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class Interface1 {
	@FXML
	private Button button1;
	@FXML
	private Button button2;
	@FXML
	private Button continued;
	@FXML
	private Label video;
	@FXML
	private Label subtitle;

	private FileChooser fc;
	private Main main;

	public static File vid = null;
	public static File sub = null;

	private boolean contClicked = false;
	private List<String> vidExt = Arrays.asList("*.avi", "*.mpg", "*.mov", "*.wav");
	private List<String> subExt = Arrays.asList("*.srt", "*.sbv", "*.vtt");

	private String defaultStr = "No file chosen.";

	@FXML
	public void initialize() {
		video.setText(defaultStr);
		subtitle.setText(defaultStr);
	}
	
	public void setMain(Main main){
		this.main = main;
	}

	public boolean isContClicked() {
		return contClicked;
	}

	@FXML
	public void handleVideoInput() {
		fc = new FileChooser();
		fc.setTitle("Choose a video file...");
		ExtensionFilter ef = new ExtensionFilter("Video Files (*.avi, *.mpg, *.mov, *.wav)", vidExt);
		fc.getExtensionFilters().add(ef);
		vid = fc.showOpenDialog(main.getPrimaryStage());
		if (vid != null) {
			video.setText(vid.toString());
		}
	}

	@FXML
	public void handleSubtitleInput() {
		fc = new FileChooser();
		fc.setTitle("Choose a subtitle file...");
		fc.getExtensionFilters().add(new ExtensionFilter("Subtitle Files (*.srt, *.sbv, *.vtt)", subExt));
		sub = fc.showOpenDialog(main.getPrimaryStage());
		if (sub != null) {
			subtitle.setText(sub.toString());
		}
	}

	@FXML
	public void handleCont() throws IOException {
		if (checkInput()) {
			contClicked = true;
			main.showEditDetails();
		}
	}

	/*
	 * I made this program in such a way that subtitle files are optional, but
	 * w/o subtitle files, they cannot generate gif by subtitle lines.
	 */
	public boolean checkInput() {
		String error = "";
		if (vid == null || video.getText().equals(defaultStr)) {
			error += "No video file chosen.";
		}
		if (error.length() == 0) {
			return true;
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(main.getPrimaryStage());
			alert.setTitle("Error");
			alert.setHeaderText("Oops! Something's wrong...");
			alert.setContentText(error);

			alert.showAndWait();

			return false;
		}
	}
}
