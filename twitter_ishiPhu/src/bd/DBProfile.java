package bd;

import java.io.PrintWriter;

import javax.servlet.http.Part;

import bd.image.BytesTools;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;

public class DBProfile {

	public static void createUserProfile(String username, String firstName, String lastName,
			String birthDate, String email, Part imgPart, PrintWriter printWriter){
	}

	public static void addProfilePicture(String key, String username, String email, Part imgPart, PrintWriter printWriter) {
		Mongo m = DBMongoStatic.getMongoConnection(printWriter);
		DBCollection collection = DBMongoStatic.getCollectionFromMongoDB(m, "Profile");

		DBObject query = new BasicDBObject();
		query.put("author_login", username);
		query.put("email", email);
		DBObject object = collection.findOne(query);

		/* ---------- Gestion Image ---------- */
		GridFS fs = new GridFS(m.getDB("gr1_phu_ishiwata"));
		byte[] data = BytesTools.getBytesFromPartFile(imgPart);
		String category = imgPart.getName();
		String filetype = imgPart.getContentType();
		
		
		GridFSInputFile tmp = fs.createFile(data);
		tmp.put("author_login", username);
		tmp.put("fileName", category);
		tmp.put("fileType", filetype);
		tmp.save();
		/* ---------- Gestion Image ---------- */
		object.put("profilePicture", tmp.getId());

		collection.insert(object);
	}
	
}
