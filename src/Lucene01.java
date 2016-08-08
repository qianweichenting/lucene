import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Lucene01 {

	private static String[] ids = { "1", "2" };

	private static String[] emails = { "sdfsdf@sdfcl.com", "sdfs.dfg.com" };

	private static String[] contents = { "sdgdfghdf sdgfgh fghghghkj", "dff fhjj jkk mv" };

	private static void index() {
		
		// 创建directory
		Directory dir = null;
		try {
			dir = FSDirectory.open(Paths.get("index"));
			Analyzer analyzer = new StandardAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			IndexWriter writer = new IndexWriter(dir, iwc);
			ByteArrayInputStream inputStream = new ByteArrayInputStream("".getBytes());
			for (int i = 0; i < 2; i++) {
				InputStream stream = Files.newInputStream(Paths.get("E:\\project\\Lucene\\src\\IndexFiles.java"));
				Document doc = new Document();
				doc.add(new StringField("ids", ids[i], Field.Store.YES));
				doc.add(new StringField("emails", emails[i], Field.Store.YES));
				doc.add(new TextField("contents", new BufferedReader(new InputStreamReader(stream, "UTF-8"))));
				writer.addDocument(doc);
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void search() {
		IndexReader reader;
		try {
			reader = DirectoryReader.open(FSDirectory.open(Paths.get("index")));
			IndexSearcher searcher = new IndexSearcher(reader);
			Analyzer analyzer = new StandardAnalyzer();
			QueryParser parser = new QueryParser("contents", analyzer);
			Query query = parser.parse("updateDocument");

			TopDocs results = searcher.search(query, 5 * 10);
			ScoreDoc[] hits = results.scoreDocs;

			for (int i = 0; i < hits.length; i++) {
				 Document doc = searcher.doc(hits[i].doc);
				 System.out.println(doc.get("contents"));
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		search();
	}

}
