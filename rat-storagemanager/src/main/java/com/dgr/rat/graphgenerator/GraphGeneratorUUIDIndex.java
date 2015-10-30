/**
 * @author Daniele Grignani (dgr)
 * @date Sep 7, 2015
 */

package com.dgr.rat.graphgenerator;

import java.io.File;
import java.io.IOException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
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
import org.apache.lucene.util.Version;

public class GraphGeneratorUUIDIndex {
	private IndexWriter _writer = null;
	private IndexReader _reader = null;
	private IndexSearcher _searcher = null;
	
	private static final String RATField = "field";
	private static final String RATContent = "content";
	
	public GraphGeneratorUUIDIndex() {
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("deprecation")
	public void initWriter(String folder) throws IOException{
		//Directory dir = FSDirectory.open(path);
		Directory indexDirectory = FSDirectory.open(new File(folder));
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
//		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_CURRENT, analyzer);
		_writer = new IndexWriter(indexDirectory, config);
//		iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
//		_writer = new IndexWriter(dir, iwc);
	}
	
	public void addText(String commandName, String value) throws IOException{
		Document doc = new Document();
		Field ratField = new StringField(GraphGeneratorUUIDIndex.RATField, commandName, Field.Store.YES);
		Field ratContent = new StringField(GraphGeneratorUUIDIndex.RATContent, value, Field.Store.YES);
		
		doc.add(ratField);
		doc.add(ratContent);
		_writer.addDocument(doc);
//		_writer.updateDocument(new Term(GraphGeneratorUUIDIndex.RATContent, value), doc);
	}
	
	public void closeWriter() throws IOException{
		if(_writer != null){
			_writer.close();
		}
	}
	
	public void initSearch(String indexPath) throws IOException{
		if(_reader == null){
			FSDirectory dir = FSDirectory.open(new File(indexPath));
			String[] files = dir.listAll();
			if(files.length == 0){
				return;
			}
			_reader = DirectoryReader.open(dir);
		}
		if(_searcher == null){
			_searcher = new IndexSearcher(_reader);
		}
	}
	
	public String search(String searchQuery) throws IOException, ParseException{
		String result = null;
		TopDocs results = this.searcher(searchQuery);
		if(results == null){
			return result;
		}
		ScoreDoc[] hits = results.scoreDocs;
		int numTotalHits = results.totalHits;
//		System.out.println("searchQuery: " + searchQuery + "; " + numTotalHits);
		
		for(ScoreDoc scoreDoc : results.scoreDocs) {
			int i = scoreDoc.doc;
			 Document doc = _searcher.doc(i);
			 result = doc.get(RATContent);
//			 System.out.println(result);
		}
		
		return result;
	}
	
	private TopDocs searcher(String searchQuery) throws IOException, ParseException{
		if(_searcher == null){
			return null;
		}
		
		Analyzer analyzer = new KeywordAnalyzer();
		QueryParser parser = new QueryParser(Version.LUCENE_CURRENT, RATField, analyzer);
		Query query = parser.parse(searchQuery);
		return _searcher.search(query, 100);
	}
	
	public void closeSearcher() throws IOException{
		if(_reader != null){
			_reader.close();
			_reader = null;
		}
		
		_searcher = null;
	}
}
