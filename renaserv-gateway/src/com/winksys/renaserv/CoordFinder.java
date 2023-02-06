package com.winksys.renaserv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class CoordFinder {
	
	private static final double OFFSET = 0.005;
	
	private StringBuilder ret = new StringBuilder();

	public static class Coord {
		private double lat, lon;
		
		public Coord(double lat, double lon) {
			this.lat = lat;
			this.lon = lon;
		}
		
		public double getLat() {
			return lat;
		}
		
		public double getLon() {
			return lon;
		}
	}

	private File dir;
	private Coord[] coords;
	
	public CoordFinder(File dir, Coord[] coords) {
		this.dir = dir;
		this.coords = coords;
	}
	
	public void execute() throws IOException {
		
		File[] files = dir.listFiles();
		Arrays.sort(files, 0, files.length, new Comparator<File>() {

			@Override
			public int compare(File o1, File o2) {
				int n1 = Integer.parseInt(o1.getName().substring(1,o1.getName().length()-5));
				int n2 = Integer.parseInt(o2.getName().substring(1,o2.getName().length()-5));
				return n1-n2;
			}
		});
		
		for (File file : files) {
			if (file.isFile()) {
				try {
					checkCoords(file);
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}
		
	}
	
	private void checkCoords(File file) throws IOException {
		
		FileInputStream fis = new FileInputStream(file);
		byte[] buf = new byte[(int) file.length()];
		fis.read(buf);
				
		
		JSONArray posicoes = (JSONArray) JSONSerializer.toJSON(new String(buf));
		for(int i = 0; i < posicoes.size(); i++) {
			JSONObject posicao = posicoes.getJSONObject(i);
			Date date = new Date(posicao.getLong("data"));
			double lat = posicao.getDouble("lat");
			double lon = posicao.getDouble("lon");
			double vel = posicao.getDouble("velocidade");
			
			for(Coord coord : coords) {
				
				double lat1 = coord.lat - OFFSET;
				double lat2 = coord.lat + OFFSET;
				double lon1 = coord.lon - OFFSET;
				double lon2 = coord.lon + OFFSET;
				
				if (lat >= lat1 && lat <= lat2 && lon >= lon1 && lon <= lon2) {
					ret.append(String.format("%4$s\t%3$td/%3$tm/%3$ty %3$tH:%3$tM\t%1$f,%2$f\t%5$f km/h\n", lat, lon, date, this.dir.getName(), vel));
				}
				
				
			}
			
			
		}
		
	}
	
	public StringBuilder getRet() {
		return ret;
	}

	public static void main(String[] args) throws IOException {
		FileOutputStream result = new FileOutputStream("results.csv");
		
		File f = new File("output");
		File[] dirs = f.listFiles();
		for(File dir : dirs) {
			
			if (dir.isDirectory() && !".".equals(dir.getName()) && !"..".equals(dir)) {
				
				CoordFinder finder = new CoordFinder(dir, new Coord[] {
						new Coord(-27.0655555556, -52.2397222222),
						new Coord(-27.0741666667, -52.2472222222)
						
						
				});
				finder.execute();
				StringBuilder ret = finder.getRet();
				result.write(ret.toString().getBytes());
				
			}
			
		}
		
		result.close();
	}
	
}
