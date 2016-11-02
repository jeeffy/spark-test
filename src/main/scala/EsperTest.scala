import java.util

import org.apache.spark.{SparkConf, SparkContext}
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

object EsperTest {
  def main(args: Array[String]) {
    val file = "/Users/jeeffy/projects/scala-test/src/main/resources/data/users.txt" // Should be some file on your system
    val conf = new SparkConf().setAppName("Simple Application")
    conf.setMaster("local[2]")
    val sc = new SparkContext(conf)
    val users = sc.textFile(file)
    /*users.map{ line =>
        line.split(" ")
    }.foreach(user =>println(user(0)))*/

    users.foreach{ line =>
      val user = line.split(" ")
      val map = new util.HashMap[String,Object]()
      map.put("name", user(0))
      map.put("age", user(1))
      map.put("hobby",user(2))


      val epService = EPServiceProviderManager.getDefaultProvider()
      val admin = epService.getEPAdministrator()

      val userType = new util.HashMap[String,Object]()
      userType.put("name", classOf[String])
      userType.put("age", classOf[Int])
      userType.put("hobby", classOf[String])
      admin.getConfiguration().addEventType("user", userType)

      val epl = "select * from user where name='zs'";


      val state = admin.createEPL(epl);
      state.addListener(new UpdateListener() {
        override def update(newEvents: Array[EventBean], oldEvents: Array[EventBean]): Unit = {
          if(newEvents != null){
            val age = newEvents(0).get("age")
            println(age)
          }
        }
      });
      val runtime = epService.getEPRuntime();
      runtime.sendEvent(map, "user");
    }
    sc.stop()
  }
}