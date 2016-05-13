package bd.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.imageio.ImageIO;

import bd.DBMongoStatic;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class ImagesTools {

	public static byte[] saveImageFromURLToDB(String username, URL url, PrintWriter printWriter){
		// On recupere la date et le temps sous la forme: "JJ/MM/AA HH:MM:SS"
		GregorianCalendar calendar = new java.util.GregorianCalendar();
		Date date = calendar.getTime();
		String dateNow = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.MEDIUM).format(date);
		
		byte[] data = BytesTools.getByteFromURL(url, printWriter);
		
		// Stokage de data dans la BD
		Mongo m = DBMongoStatic.getMongoConnection(printWriter);
		DBCollection collection = DBMongoStatic.getCollectionFromMongoDB(m, "Image");
		DBObject object = new BasicDBObject();

		object.put("author_login", username);
		object.put("date", dateNow);
		object.put("url", url.getFile());
		object.put("image", data);

		collection.insert(object);
		m.close();
		return data;
	}
	
	public static BufferedImage[] loadAllImageFromDB_username(String username, PrintWriter printWriter) throws MongoException, IOException{
		BufferedImage[] img = null;
		byte[][] data = null;
		Mongo m = DBMongoStatic.getMongoConnection(printWriter);
		DBCollection collection = DBMongoStatic.getCollectionFromMongoDB(m, "Image");
		
		DBObject object = new BasicDBObject();
		object.put("author_login", username);
		DBCursor curseur = collection.find(object);
		int i = 0;
		
		data = new byte[curseur.size()][];
		while(curseur.hasNext()){
			 data[i] = BytesTools.toByteArray(curseur.next().get("image"));
			 i++;
		}
		
		int j = 0;
		ByteArrayInputStream bais;
		img = new BufferedImage[data.length];
		
		while(j < data.length){
			bais = new ByteArrayInputStream(data[j]);
			img[j] = ImageIO.read(bais);
			j++;
		}
		return img;
	}
	
	public static String getImageID(String author, String url, PrintWriter printWriter){
		Mongo m = DBMongoStatic.getMongoConnection(printWriter);
		DBCollection collection = DBMongoStatic.getCollectionFromMongoDB(m, "Image");
		DBObject object = new BasicDBObject();
		object.put("author_login", author);
		object.put("url", url);
		DBCursor curseur = collection.find(object);
		return (String) curseur.next().get("_id");
	}
}
