package bd.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.servlet.http.Part;

import org.bson.types.ObjectId;

import bd.DBMongoStatic;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;

public class BytesTools {

	public static byte[] toByteArray(Object obj) throws IOException {
		byte[] bytes = null;
		ByteArrayOutputStream bos = null;
		ObjectOutputStream oos = null;
		try {
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();
			bytes = bos.toByteArray();
		} finally {
			if (oos != null) {
				oos.close();
			}
			if (bos != null) {
				bos.close();
			}
		}
		return bytes;
	}

	public static Object toObject(byte[] bytes) throws IOException, ClassNotFoundException {
		Object obj = null;
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		try {
			bis = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bis);
			obj = ois.readObject();
		} finally {
			if (bis != null) {
				bis.close();
			}
			if (ois != null) {
				ois.close();
			}
		}
		return obj;
	}

	public static String getStringFromBytes(byte[] data){
		StringBuffer buffer = new StringBuffer();

		for(byte b : data) {
			buffer.append(Integer.toHexString(b));
			buffer.append(" ");
		}
		return  buffer.toString().toUpperCase();
	}

	public static String toString(byte[] bytes) {
		return new String(bytes);
	}

	
	public static byte[] getByteFromURL(URL url, PrintWriter printWriter){
		byte[] data = null;

		BufferedImage img;
		try {
			// 1). Lecture de l'image depuis l'url
			img = ImageIO.read(url);

			// Recuperation de l'extension de l'image
			String extension = url.getFile().substring(url.getFile().lastIndexOf(".") + 1);

			// 2). Conversion de l'image en tableau de byte (=>data)
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(img, extension, baos);
			baos.flush();
			data = baos.toByteArray();
			baos.close();
			return data;
		} catch (IOException e) {
			printWriter.print(e.getMessage());
			e.printStackTrace();
		}

		return data;
	}
	
	public static byte[] getBytesFromFile(String ImageName) throws IOException {
		File filename = new File(ImageName);
		BufferedImage image = ImageIO.read(filename);
		// Recuperation de l'extension de l'image
		String extension = filename.getName().substring(filename.getName().lastIndexOf(".") + 1);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, extension, baos);
		byte[] res = baos.toByteArray();
		
		baos.flush();
		baos.close();

		return res;
	}
	
	public static byte[] getBytesFromPartFile(Part filePart){
        try {
    		int fileSize= (int) filePart.getSize();
    		InputStream in;

    		in = filePart.getInputStream();
            byte[] data = new byte[fileSize];
			in.read(data);
	        in.close();
	        return data;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
	}
	
	public static byte[] loadBytesFromDBProfile(DBObject object, PrintWriter printWriter){
		Mongo m = DBMongoStatic.getMongoConnection(printWriter);
		GridFS gridFS = new GridFS(m.getDB("gr1_phu_ishiwata"));
		
		// On suppose que le DBObject passe en parametre est un object de la collection "Profile"
		DBObject obj = (DBObject) object.get("profilePicture");
		DBObject search = gridFS.findOne(new BasicDBObject("_id", new ObjectId("5508aeece4b03b6cb89bbc70")));

		// On cherche dans la collection "fs.files" le DBObject possedant l'id contenu dans ProfilePicture
		GridFSDBFile found = gridFS.findOne(search);
		
		byte[] data = null;
		
		if(found == null)
			return null;
		else{
			try {
				int size = Integer.parseInt(found.get("length").toString());
				ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
				found.writeTo(baos);
				
				data = baos.toByteArray();
				
				baos.flush();
				baos.close();
				return data;
			} catch (IOException e) {
				printWriter.print(e.getMessage());
				e.printStackTrace();
			}
		}
        return data;
	}
	
}
