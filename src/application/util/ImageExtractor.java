package application.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.Global;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;

import application.view.Interface2;
import application.view.TextDesign;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class ImageExtractor {
	public static String fileName;
	public static ArrayList<WritableImage> ai = new ArrayList<WritableImage>();// for
																				// preview
	public static ArrayList<BufferedImage> abi = new ArrayList<BufferedImage>();// for
																				// export

	private static int videoStreamIndex = -1;
	private static double lastWrite = (double) Global.NO_PTS;

	public static int width;
	public static int height;

	public static double frameRate;

	public static double timeElapsed;

	public ImageExtractor(String fileName) {
		ImageExtractor.fileName = fileName;
	}

	public void extract() {
		IMediaReader mr = ToolFactory.makeReader(fileName);
		mr.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
		mr.addListener(new Listener());
		while (mr.readPacket() == null)
			;
	}

	public static double getTimeElapsed() {
		return timeElapsed;
	}

	public static int style(String style) {
		if (style.equals("Bold")) {
			return Font.BOLD;
		} else if (style.equals("Oblique")) {
			return Font.ITALIC;
		} else if (style.equals("Bold Oblique")) {
			return Font.BOLD | Font.ITALIC;
		} else {
			return Font.PLAIN;
		}
	}

	private static class Listener extends MediaListenerAdapter {
		public void onVideoPicture(IVideoPictureEvent e) {
			IContainer ic = IContainer.make();
			ic.open(fileName, IContainer.Type.READ, null);
			IStream is = ic.getStream(0);
			IStreamCoder isc = is.getStreamCoder();
			frameRate = isc.getFrameRate().getDouble();
			width = isc.getWidth();
			height = isc.getHeight();
			if (e.getStreamIndex() != videoStreamIndex) {
				if (videoStreamIndex == -1) {
					videoStreamIndex = e.getStreamIndex();
				} else {
					return;
				}
			}
			if (lastWrite == Global.NO_PTS) {
				lastWrite = e.getTimeStamp() - Global.DEFAULT_PTS_PER_SECOND / frameRate;
			}
			timeElapsed += Global.DEFAULT_PTS_PER_SECOND / frameRate;
			if (timeElapsed >= Interface2.ti / 1000.0 * Global.DEFAULT_PTS_PER_SECOND
					&& timeElapsed <= Interface2.tf / 1000 * Global.DEFAULT_PTS_PER_SECOND) {
				if (e.getTimeStamp() - lastWrite >= Global.DEFAULT_PTS_PER_SECOND / frameRate) {

					BufferedImage br = e.getImage();
					Graphics2D g2d = br.createGraphics();
					g2d.drawImage(br, 0, 0, null);
					g2d.setPaint(Color.decode(TextDesign.fontColor));
					g2d.setFont(new Font(TextDesign.fontName, style(TextDesign.fontStyle), TextDesign.fontSize));
					FontMetrics fm = g2d.getFontMetrics();
					String s = Interface2.str;
					int x = br.getWidth() / 2 - fm.stringWidth(s) / 2;
					int y = br.getHeight() - fm.getHeight() - 10;
					g2d.drawString(s, x, y);
					g2d.dispose();
					abi.add(br);
					WritableImage wr = null;
					if (br != null) {
						wr = new WritableImage(br.getWidth(), br.getHeight());
						PixelWriter pw = wr.getPixelWriter();
						for (int i = 0; i < br.getWidth(); i++) {
							for (int j = 0; j < br.getHeight(); j++) {
								pw.setArgb(i, j, br.getRGB(i, j));
							}
						}
						ai.add(wr);
					}
				}
			}
		}
	}
}
