package de.robertbloy.core;

import java.net.UnknownHostException;
import java.util.Date;
import com.mongodb.DBObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.File;
import java.net.URL;

/**
 * Java + MongoDB Hello world Example
 * 
 */
public class App {

  public static void main(String[] args) {
      
      new App().execute();
  }

    private void execute () {

    try {

	/**** Connect to MongoDB ****/
	// Since 2.10.0, uses MongoClient
	MongoClient mongo = new MongoClient("localhost", 27017);

	/**** Get database ****/
	// if database doesn't exists, MongoDB will create it for you
	DB db = mongo.getDB("boaportal");

	DBCollection fotosCollection = db.getCollection("fotos");
	int fotosImported = 0;
	if (fotosCollection.count() < 1) {
	   System.out.println("The database is empty.  We need to populate it");
	   try {
	       byte buf[] = new byte[4096];
	       ByteArrayOutputStream baos = new ByteArrayOutputStream();
	       InputStream is = this.getClass().getClassLoader().getResourceAsStream ( "fotos/foto1.png" );
	       while ( is.available() > 0 )
	       {
			int len = is.read(buf);
			baos.write ( buf, 0, len );
	       }
	       is.close();
	       baos.close();
       	       byte imageBytes[] = baos.toByteArray();
	       
	       System.out.println ( "Image foto1 mit " + imageBytes.length + " geladen." );
	       DBObject doc1 = new BasicDBObject("foto1", 1);
	       doc1.put("fileName", "foto1");
	       doc1.put("size", imageBytes.length);
	       doc1.put("data", imageBytes);
	       
	       fotosCollection.insert ( doc1 );
	       System.out.println("Successfully imported " + fotosImported + " fotos.");
	   } catch (Exception e) {
	     e.printStackTrace();
	   }
	}

	/**** Done ****/
	System.out.println("Done");

    } catch (UnknownHostException e) {
	e.printStackTrace();
    } catch (MongoException e) {
	e.printStackTrace();
    }

  }
}
