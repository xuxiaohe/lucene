package com.lucene.core;

import java.io.File;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;
 


import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.sun.org.apache.bcel.internal.classfile.Constant;
 
/**
 * 创建索引
 * <a href="http://my.oschina.net/arthor" class="referer" target="_blank">@author</a>  zhanghaijun
 *
 */
public class Demo1 {
    public static void main(String[] args) throws Exception {
        //先在数据库中拿到要创建索引的数据 
        Mongo mongo = new Mongo("chat.yunxuetang.com:27017");
        DB db = mongo.getDB("yxtdev");
        DBCollection msg = db.getCollection("topic");
        DBCursor cursor = msg.find();
        //是否重新创建索引文件，false：在原有的基础上追加
        boolean create = true;
        //创建索引
        Directory directory = FSDirectory.open(new File("/lucene/index"));
        Analyzer analyzer = new IKAnalyzer();//IK中文分词器 
        IndexWriter indexWriter = new IndexWriter(directory,analyzer,create,MaxFieldLength.LIMITED);
        boolean exist = cursor.hasNext();
        while(exist){
        	cursor.next();
        	String id=cursor.curr().get("_id").toString();
        	String title=cursor.curr().get("title").toString();
        	String text=cursor.curr().get("content").toString();
           // System.out.println(cursor.curr().get("_id").toString()+cursor.curr().get("title").toString());
        	
            Document doc = new Document();
            Field fieldText2 = new Field("id",id,Field.Store.YES, 
                    Field.Index.NO);
            Field fieldText = new Field("title",title,Field.Store.YES, 
                      Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS);
            Field fieldText3 = new Field("text",text,Field.Store.YES, 
                    Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS);
           
            doc.add(fieldText);
            doc.add(fieldText2);
            doc.add(fieldText3);
            //indexWriter.addDocument(doc);
            indexWriter.updateDocument(new Term("Id", id), doc);
            
            exist = cursor.hasNext();
        }
        cursor = null;
        //optimize()方法是对索引进行优化
       // indexWriter.optimize();     
        //最后关闭索引
        indexWriter.close();
    }
}