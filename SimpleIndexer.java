import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
 
public class SimpleFileIndexer {
     
    public static void main(String[] args) throws Exception {
        File indexDir = new File("C:/index/");
        File dataDir = new File("C:/Users/default.default-PC/Desktop/test");
        String suffix = "java";
         
        SimpleFileIndexer indexer = new SimpleFileIndexer();
         
        int numIndex = indexer.index(indexDir, dataDir, suffix);
         
        System.out.println("Total files indexed " + numIndex);
    }

    private int index(File indexDir, File dataDir, String suffix) throws Exception {
    	IndexWriterConfig IWC = new IndexWriterConfig(Version.LUCENE_4_10_3, new SimpleAnalyzer());
        IndexWriter indexWriter = new IndexWriter(
                FSDirectory.open(indexDir),
                IWC);

        indexDirectory(indexWriter, dataDir, suffix);

        int numIndexed = indexWriter.maxDoc();
        indexWriter.close();
         
        return numIndexed;
    }

    private void indexDirectory(IndexWriter indexWriter, File dataDir,
    		String suffix) throws IOException {
        File[] files = dataDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isDirectory()) {
                indexDirectory(indexWriter, f, suffix);
            }
            else {
                indexFileWithIndexWriter(indexWriter, f, suffix);
            }
        }
    }
    private void indexFileWithIndexWriter(IndexWriter indexWriter, File f,
            String suffix) throws IOException {
        if (f.isHidden() || f.isDirectory() || !f.canRead() || !f.exists()) {
            return;
        }
        if (suffix!=null && !f.getName().endsWith(suffix)) {
            return;
        }
        System.out.println("Indexing file " + f.getCanonicalPath());
        
        String t = "parse";
        String e = "test";
        String m = "AST";
        String p = "lucene";
        Document doc = new Document();
        FieldType type = new FieldType();
        type.setStored(true);
        type.setIndexed(true);
        doc.add(new Field("contents", t, type));
        doc.add(new Field("contents", e, type));
        doc.add(new Field("contents", m, type));
        doc.add(new Field("contents", p, type));
        doc.add(new Field("filename", f.getCanonicalPath(),
           Field.Store.YES, Field.Index.ANALYZED));
        indexWriter.addDocument(doc);
    }
}
