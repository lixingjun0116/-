import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoTest {

    public static void main(String[] args) {
        //链接mongo服务器
        MongoClient mongoClient=new MongoClient("127.0.0.1");
        //连接到数据库
        MongoDatabase database = mongoClient.getDatabase("test");
        //得到要操作的集合
        MongoCollection<Document> spit=database.getCollection("user");
        //得到集合中的所有元素
        FindIterable<Document> documents= spit.find();
        //把所有元素进行遍历
        for(Document document : documents ){
            System.out.println("操作用户"+document.getString("args"));
            System.out.println("请求方式"+document.getString("operationType"));
            System.out.println("请求地址"+document.getString("method"));
            System.out.println("创建时间"+document.getString("createTime"));
        }
        mongoClient.close();
    }
}
