package application.view;

import java.io.IOException;
import java.util.ArrayList;
import application.Main;
import application.util.ImageExtractor;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Preview {
	@FXML
	private Pane pane;
	@FXML
	private ImageView iv;
	@FXML
	private Button generate;
	@FXML
	private Button back;

	private ArrayList<WritableImage> awi = new ArrayList<WritableImage>();

	private Main main;

	int i = 0;

	@FXML
	private void initialize() throws IOException {
		ImageExtractor ie = new ImageExtractor(Interface1.vid.getCanonicalPath());
		ie.extract();
		awi = ImageExtractor.ai;
		Timeline t = new Timeline();
		t.setCycleCount(Timeline.INDEFINITE);
		KeyFrame k = new KeyFrame(Duration.millis(1000 / ImageExtractor.frameRate), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				iv.setImage(awi.get(i % awi.size()));
				i++;
			}

		});
		t.getKeyFrames().add(k);
		t.play();
	}

	@FXML
	private void handleGenerate() {
		main.save();
	}

	@FXML
	private void handleBack() throws IOException {
		main.showEditDetails();
	}

	public void setMain(Main main) {
		this.main = main;
	}
}
