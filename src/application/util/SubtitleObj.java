package application.util;

public class SubtitleObj {
	private int number;
	private String timeStart;
	private String timeEnd;
	private String lines;

	public SubtitleObj(String timeStart, String timeEnd, String lines){
		this.timeStart=timeStart;
		this.timeEnd=timeEnd;
		this.lines=lines;
	}

	public SubtitleObj(int number, String timeStart, String timeEnd, String lines){
		this.number=number;
		this.timeStart=timeStart;
		this.timeEnd=timeEnd;
		this.lines=lines;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(String timeStart) {
		this.timeStart = timeStart;
	}

	public String getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}

	public String getLines() {
		return lines;
	}

	public void setLines(String lines) {
		this.lines = lines;
	}

	@Override
	public String toString() {
		return "SubtitleObj [number=" + number + ", timeStart=" + timeStart + ", timeEnd=" + timeEnd + ", lines="
				+ lines + "]";
	}
}
