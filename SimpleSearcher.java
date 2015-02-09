import java.io.File;

import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.QueryBuilder;
import org.apache.lucene.util.Version;

public class SimpleSearcher {
    
    public static void main(String[] args) throws Exception {
        
        File indexDir = new File("c:/index/");
        String query = "lucene";
        int hits = 100;
        
        SimpleSearcher searcher = new SimpleSearcher();
        searcher.searchIndex(indexDir, query, hits);
        
    }
    
    private void searchIndex(File indexDir, String queryStr, int maxHits) 
            throws Exception {
        
        FSDirectory directory = FSDirectory.open(indexDir);
        IndexReader r = IndexReader.open(directory);

        IndexSearcher searcher = new IndexSearcher(r);
        QueryParser parser = new QueryParser("contents", new SimpleAnalyzer());
        Query query = parser.parse(queryStr);
        
        TopDocs topDocs = searcher.search(query, maxHits);
        
        ScoreDoc[] hits = topDocs.scoreDocs;
        for (int i = 0; i < hits.length; i++) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println(d.get("filename"));
        }
        
        System.out.println("Found " + hits.length);
        
    }

}
