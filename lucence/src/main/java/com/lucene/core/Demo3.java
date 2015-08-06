package com.lucene.core;

import java.net.UnknownHostException;
 
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
 
public class Demo3 {
    public static void main(String[] args) throws Exception{
        Mongo mongo = new Mongo("chat.yunxuetang.com:27017");
        DB db = mongo.getDB("yxtdev");
        DBCollection dbc = db.getCollection("topic");
        DBObject basicdb = new BasicDBObject();
        basicdb.put("$regex","各");
        basicdb.put("$options","");
        long startTime = System.currentTimeMillis();
        DBCursor cursor = dbc.find(new BasicDBObject("title",basicdb));
        int j =0;
        while(cursor.hasNext()){
            System.out.println(cursor.next().get("title"));
        	//cursor.next().get("title");
            j++;
        }
        System.out.println("查找数据量"+j);
        long endTime = System.currentTimeMillis();
        System.out.println("未优化前:"+(endTime-startTime));
    }
}