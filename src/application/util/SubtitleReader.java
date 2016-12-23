package application.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SubtitleReader {
	public static ArrayList<String> read(File f) {
		InputStreamReader fr = null;
		BufferedReader br = null;
		try {
			ArrayList<String> lines = new ArrayList<String>();
			ArrayList<String> lines2 = new ArrayList<String>();
			fr = new InputStreamReader(new FileInputStream(f));
			br = new BufferedReader(fr);
			String l;
			while ((l = br.readLine()) != null) {
				lines.add(l);
			}
			String str = "";
			for (int i = 0; i < lines.size(); i++) {
				str += lines.get(i) + System.getProperty("line.separator");
				if (lines.get(i).equals("") || i == lines.size() - 1) {
					lines2.add(str);
					str = "";
				}
			}
			return lines2;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (null != fr) {
					fr.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				if (br != null) {
					br.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static ArrayList<SubtitleObj> subtitleExtractor(ArrayList<String> subtitles, File f) {
		ArrayList<SubtitleObj> lines = new ArrayList<SubtitleObj>();
		String fileName = f.getAbsolutePath();
		String ext = fileName.substring(fileName.lastIndexOf('.')+1);
		for (int i = 0; i < subtitles.size(); i++) {
			String[] spl = subtitles.get(i).split(System.getProperty("line.separator"));
			if (spl.length > 0) {
				if (ext.equals("srt") || ext.equals("vtt")) {
					String number = spl[0].replaceAll("﻿", "");
					int no = Integer.parseInt(number);
					int j = 1;
					String[] time = spl[1].split(" --> ");
					j++;
					String line = "";
					for (; j < spl.length; j++) {
						line += spl[j] + " ";
					}
					lines.add(new SubtitleObj(no, time[0], time[1], line));
				} else {
					String[] time = spl[1].split(",");
					String line = "";
					for (int j = 1; j < spl.length; j++) {
						line += spl[j] + " ";
					}
					lines.add(new SubtitleObj(time[0], time[1], line));
				}
			}
		}
		return lines;
	}
}
