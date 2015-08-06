package com.lucene.core;

import java.io.File;
 




import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.wltea.analyzer.lucene.IKQueryParser;
import org.wltea.analyzer.lucene.IKSimilarity;
 
/**
 * 查找索引
 */
public class Demo2 {
    public static void main(String[] args) throws Exception {
        // onlysearching, so read-only=true
        long starttime = System.currentTimeMillis();
        IndexReader reader =IndexReader.open(FSDirectory.open(new File("/lucene/index")),true); 
        IndexSearcher searcher = new IndexSearcher(reader);
        searcher.setSimilarity(new IKSimilarity());   //在索引器中使用IKSimilarity相似度评估器 
        String[] keys = {"话题好玩吗","话题好玩吗"};      //关键字数组
        String[] fields = {"title","title"};  //搜索的字段
        BooleanClause.Occur[] flags = {BooleanClause.Occur.MUST,BooleanClause.Occur.MUST};    //BooleanClause.Occur[]数组,它表示多个条件之间的关系 
        //使用 IKQueryParser类提供的parseMultiField方法构建多字段多条件查询
        //Query query = IKQueryParser.parseMultiField(fields,keys, flags);     //IKQueryParser多个字段搜索  
       // Query query =IKQueryParser.parse("text","各种");  //IK搜索单个字段       
        IKAnalyzer analyzer = new IKAnalyzer();
        Query query =MultiFieldQueryParser.parse(Version.LUCENE_CURRENT, keys, fields, flags,analyzer);   //用MultiFieldQueryParser得到query对象   
        System.out.println("query:"+query.toString()); //查询条件    
        /*TopScoreDocCollector topCollector = TopScoreDocCollector.create(searcher.maxDoc(), false);
        searcher.search(query,topCollector);
         
        ScoreDoc[] docs = topCollector.topDocs(3).scoreDocs;
        System.out.println(docs.length);*/
         
        /**
        *得到TopDocs对象之后,可以获取它的成员变量totalHits和scoreDocs.这两个成员变量的访问权限是public的,所以可以直接访问
        */
        TopDocs topDocs = searcher.search(query,1000001);
        Integer count = topDocs.totalHits;
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for(int i = 0;i<count;i++){
            ScoreDoc scoreDoc = scoreDocs[i];
            Document document = searcher.doc(scoreDoc.doc);
//            document.get("text");
//            document.get("id");
            System.out.println(document.get("text")+"id:"+ document.get("id"));
        }
        System.out.println("查找数据量:"+count);
        long endtime = System.currentTimeMillis();
        System.out.println(endtime-starttime);
        reader.close(); //关闭索引   
    }
}