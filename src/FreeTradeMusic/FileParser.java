package FreeTradeMusic;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.DefaultHandler;

public class FileParser
{
    private static FileParser instance = null;

    private FileParser()
    {
    }

    public static FileParser getInstance()
    {
        if(instance == null)
            instance = new FileParser();
        return instance;
    }

    public Metadata getMetadata(String filePath)
    {
        Metadata metadata = null;
        try
        {
            InputStream input = new FileInputStream(new File(filePath));
            ContentHandler handler = new DefaultHandler();
            metadata = new Metadata();
            Parser parser = new Mp3Parser();
            ParseContext parseCtx = new ParseContext();
            parser.parse(input, handler, metadata, parseCtx);
            input.close();

//            // List all metadata
//            String[] metadataNames = metadata.names();
//
//            for(String name : metadataNames){
//                System.out.println(name + ": " + metadata.get(name));
//            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return metadata;
    }
}
