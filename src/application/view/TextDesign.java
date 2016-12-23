package application.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class TextDesign {
	@FXML
	private ComboBox<String> font;
	@FXML
	private ComboBox<String> style;
	@FXML
	private Spinner<Integer> size;
	@FXML
	private ColorPicker cp;
	@FXML
	private Button ok;
	@FXML
	private Button cancel;
	@FXML
	private Label sample;

	private Stage stage;

	private boolean okClicked;

	public static String fontName = "Calibri";
	public static String fontStyle = "Plain";
	public static int fontSize = 48;
	public static String fontColor = "#000000";

	@FXML
	private void initialize() {
		font.getItems().addAll(Font.getFamilies());
		font.getSelectionModel().selectFirst();
		font.setVisibleRowCount(6);
		style.getItems().addAll("Plain", "Bold", "Oblique", "Bold Oblique");
		style.getSelectionModel().selectFirst();
		SpinnerValueFactory<Integer> svf = new SpinnerValueFactory.IntegerSpinnerValueFactory(8, 96, 12);
		size.setValueFactory(svf);
		font.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				FontWeight i= FontWeight.BOLD;
				FontPosture j = FontPosture.ITALIC;
				if(style.getSelectionModel().getSelectedIndex()==0){
					sample.setFont(new Font(font.getSelectionModel().getSelectedItem(), 12));
				}else if(style.getSelectionModel().getSelectedIndex()==1){
					sample.setFont(Font.font(font.getSelectionModel().getSelectedItem(), i, 12));
				}else if(style.getSelectionModel().getSelectedIndex()==2){
					sample.setFont(Font.font(font.getSelectionModel().getSelectedItem(), j, 12));
				}else{
					sample.setFont(Font.font(font.getSelectionModel().getSelectedItem(), i, j, 12));
				}
			}

		});
		style.valueProperty().addListener(new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				FontWeight i= FontWeight.BOLD;
				FontPosture j = FontPosture.ITALIC;
				if(style.getSelectionModel().getSelectedIndex()==0){
					sample.setFont(new Font(font.getSelectionModel().getSelectedItem(), 12));
				}else if(style.getSelectionModel().getSelectedIndex()==1){
					sample.setFont(Font.font(font.getSelectionModel().getSelectedItem(), i, 12));
				}else if(style.getSelectionModel().getSelectedIndex()==2){
					sample.setFont(Font.font(font.getSelectionModel().getSelectedItem(), j, 12));
				}else{
					sample.setFont(Font.font(font.getSelectionModel().getSelectedItem(), i, j, 12));
				}
			}

		});
		cp.valueProperty().addListener(new ChangeListener<Color>() {

			@Override
			public void changed(ObservableValue<? extends Color> arg0, Color arg1, Color arg2) {
				sample.setTextFill(cp.getValue());
			}

		});
	}

	public boolean isOkClicked() {
		return okClicked;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	@FXML
	public void handleOk() {
		fontName = font.getSelectionModel().getSelectedItem();
		fontStyle = style.getSelectionModel().getSelectedItem();
		fontSize = size.getValue();
		fontColor = "#" + Integer.toHexString(cp.getValue().hashCode()).substring(0, 6).toUpperCase();

		stage.close();
	}

	@FXML
	public void handleCancel() {
		stage.close();
	}
}
