package application.view;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xuggle.xuggler.IContainer;

import application.Main;
import application.util.SubtitleObj;
import application.util.SubtitleReader;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Interface2 {
	@FXML
	private RadioButton time;
	@FXML
	private RadioButton line;
	@FXML
	private TextField timeStart;
	@FXML
	private TextField timeEnd;
	@FXML
	private TextField txt;
	@FXML
	private TextField lines;
	@FXML
	private Button generate;
	@FXML
	private Button back;
	@FXML
	private Button design;

	private ToggleGroup tg;

	public static long length;

	private IContainer ic;

	private Date sdfstart;
	private Date sdfend;

	private static final String TIME_PATTERN = "\\d+:[0-5][0-9]:[0-5][0-9]:[0-9]{3}";
	private Pattern pattern = Pattern.compile(TIME_PATTERN);
	private Matcher matcher;
	private Matcher matcher1;

	private Main main;

	public static String str = "";

	public static long gifLength;
	public static long ti;
	public static long tf;

	private boolean previewClicked;

	@FXML
	private void initialize() {
		tg = new ToggleGroup();
		line.setToggleGroup(tg);
		time.setToggleGroup(tg);

		if (Interface1.sub == null) {
			line.setSelected(false);
			line.setDisable(true);
			lines.setDisable(true);
		}

		timeStart.disableProperty().bind(Bindings.not(time.selectedProperty()));
		timeEnd.disableProperty().bind(Bindings.not(time.selectedProperty()));
		txt.disableProperty().bind(Bindings.not(time.selectedProperty()));

		lines.disableProperty().bind(Bindings.not(line.selectedProperty()));

		timeStart.setPromptText("hh:mm:ss:SSS");
		timeEnd.setPromptText("hh:mm:ss:SSS");
		txt.setPromptText("Maximum 140 characters");
		txt.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				if (txt.getText().length() > 140) {
					String s = txt.getText().substring(0, 140);
					txt.setText(s);
				}
			}
		});

		lines.setPromptText("Insert a line from the script...");

		ic = IContainer.make();
		if (ic.open(Interface1.vid.getAbsolutePath(), IContainer.Type.READ, null) < 0) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Oops! Something's wrong...");
			alert.setContentText("The file(s) you have chosen is/are corrupted. Click OK to choose other file(s).");
			alert.showAndWait();

			main.showChooseFile();
		} else {
			length = ic.getDuration();
		}
	}

	public boolean isPreviewClicked(){
		return previewClicked;
	}

	@FXML
	private void handleGenerateGIF() throws IOException, ParseException {
		if (checkInput()) {
			main.showPreview();
		}
	}

	@FXML
	private void handleBack(){
		main.showChooseFile();
	}

	@FXML
	private void handleTextDesign(){
		main.showTextDesign();
	}

	public void setMain(Main main){
		this.main = main;
	}

	public boolean checkInput() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));// This line is to prevent
														// time zone problems,
														// as I am using date
														// class.
		String error = "";
		if (time.isSelected()) {
			str = txt.getText();
			matcher = pattern.matcher(timeStart.getCharacters());
			if (!matcher.matches()) {
				error += "Start time format is invalid.\n";
			}
			matcher1 = pattern.matcher(timeEnd.getCharacters());
			if (!matcher1.matches()) {
				error += "End time format is invalid.\n";
			}
			if (matcher.matches() && matcher1.matches()) {
				try {
					sdfstart = sdf.parse(timeStart.getText());
					sdfend = sdf.parse(timeEnd.getText());
					if (sdfstart.getTime() < 0 || sdfend.getTime() > length
							|| !(sdfstart.getTime() < sdfend.getTime())) {
						error += "Time range provided is invalid.\n";
					} else {
						ti = sdfstart.getTime();
						tf = sdfend.getTime();
						gifLength = tf - ti;
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}

			}
		} else if (line.isSelected()) {
			boolean matchFound = false;
			ArrayList<String> as = SubtitleReader.read(Interface1.sub);
			ArrayList<SubtitleObj> aso = SubtitleReader.subtitleExtractor(as, Interface1.sub);
			for (int i = 0; i < aso.size(); i++) {
				String stri = aso.get(i).getLines().substring(0, aso.get(i).getLines().length()-1);
				if (lines.getText().matches("(<\\w+>)*"+stri+"(</\\w+>)?")) {
					matchFound = true;
					str = lines.getText();
					String startTime = aso.get(i).getTimeStart();
					String endTime = aso.get(i).getTimeEnd();
					Date startT, endT;
					SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss,SSS");
					sdf1.setTimeZone(TimeZone.getTimeZone("UTC"));
					SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss.SSS");
					sdf2.setTimeZone(TimeZone.getTimeZone("UTC"));
					if (aso.get(i).getTimeStart().contains(",")) {
						startT = sdf1.parse(startTime);
						endT = sdf1.parse(endTime);
					} else {
						startT = sdf2.parse(startTime);
						endT = sdf2.parse(endTime);
					}
					ti = startT.getTime();
					tf = endT.getTime();
					gifLength = tf - ti;
					break;
				}
			}
			if (!matchFound) {
				error += "No such line found.\n";
			}
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
