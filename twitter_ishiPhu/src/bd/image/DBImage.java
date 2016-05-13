package bd.image;

import java.io.PrintWriter;

import javax.servlet.http.Part;

import bd.DBMongoStatic;

import com.mongodb.Mongo;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;

public class DBImage {

	public static String addPartFileToGridFS(String username, Part imgPart, PrintWriter printWriter) {
		/* ---------- Gestion Image ---------- */
		Mongo m = DBMongoStatic.getMongoConnection(printWriter);

		byte[] data = BytesTools.getBytesFromPartFile(imgPart);
		String category = imgPart.getName();
		String filetype = imgPart.getContentType();

		GridFS fs = new GridFS(m.getDB("gr1_phu_ishiwata"));
		GridFSInputFile object = fs.createFile(data);
		object.put("author_login", username);
		object.put("category", category);
		object.put("contentType", filetype);
		object.save();
		/* ----------------------------------- */
		
		return object.getId().toString();
	}
	
}
