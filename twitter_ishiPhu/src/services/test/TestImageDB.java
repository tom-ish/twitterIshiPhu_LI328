package services.test;

import java.awt.FlowLayout;
//import java.net.MalformedURLException;
//import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.gridfs.GridFS;

import bd.DBMongoStatic;
import bd.image.BytesTools;

public class TestImageDB {

	public static void main(String[] args) {
		/*
		// Test de la fonction getByteFromURL
		URL url = null;
		try {
			url = new URL("http://img1.cfstatic.com/wallpapers/67c8e60db0191962259a741cceb07b98_large.jpeg");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		byte[] data = BytesTools.getByteFromURL(url, null);
		
		// Test de la fonction getByteFromURL
		String ImageName = "/Users/Tomohiro/Pictures/skateGirlWallpaper.jpg";
		byte[] data = null;
		try {
			data = BytesTools.getBytesFromFile(ImageName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JFrame frame = new JFrame();
		frame.setSize(1000, 1000);
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(new JLabel(new ImageIcon(data)));
		frame.pack();
		frame.setVisible(true);*/

		Mongo m = DBMongoStatic.getMongoConnection(null);
		
		System.out.println("-----------------------------------------------------------");
		System.out.println("------------------- Mongo DB : fs.files -------------------");
		System.out.println("-----------------------------------------------------------");
		
		//Show tables

		DBCollection col = DBMongoStatic.getCollectionFromMongoDB(m, "fs.files");

		//col.remove(new BasicDBObject());
		
		DBCursor cursor= col.find();
		GridFS gridFS = new GridFS(m.getDB("gr1_phu_ishiwata"));

		while(cursor.hasNext()) {
			System.out.println(cursor.next().toString());
		}
		byte[] data = null;
		DBObject c = gridFS.findOne(new BasicDBObject("_id", new ObjectId("5508aeece4b03b6cb89bbc70")));
		if(c != null){
			data = BytesTools.loadBytesFromDBProfile(c, null);

			JFrame frame = new JFrame();
			frame.setSize(1000, 1000);
			frame.getContentPane().setLayout(new FlowLayout());
			frame.getContentPane().add(new JLabel(new ImageIcon(data)));
			frame.pack();
			frame.setVisible(true);
		}
		else
			System.out.println(" ----------- null ............... ");
		
	}

}
