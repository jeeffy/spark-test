import java.util

import org.apache.spark.{SparkConf, SparkContext}
import com.espertech.esper.client.EPServiceProviderManager
import com.espertech.esper.client.EventBean
import com.espertech.esper.client.UpdateListener

object EsperTest {
  def main(args: Array[String]) {
    val file = "src/main/resources/data/users.txt"
    val conf = new SparkConf().setAppName("Simple Application")
    conf.setMaster("local")
    val sc = new SparkContext(conf)
    val users = sc.textFile(file)

    users.foreach{ line =>
      val user = line.split(" ")
      val map = new util.HashMap[String,Object]()
      map.put("name", user(0))
      map.put("age", user(1))
      map.put("hobby",user(2))

      esperHandle(map)
    }
    sc.stop()
  }

  def esperHandle(map: util.HashMap[String,Object]): Unit = {
    val epService = EPServiceProviderManager.getDefaultProvider()
    val admin = epService.getEPAdministrator()

    val userType = new util.HashMap[String,Object]()
    userType.put("name", classOf[String])
    userType.put("age", classOf[Int])
    userType.put("hobby", classOf[String])
    admin.getConfiguration().addEventType("user", userType)

    val epl = "select * from user where name='zs'"


    val state = admin.createEPL(epl)
    state.addListener(new UpdateListener() {
      override def update(newEvents: Array[EventBean], oldEvents: Array[EventBean]): Unit = {
        if(newEvents != null){
          val age = newEvents(0).get("age")
          val hobby = newEvents(0).get("hobby")
          println(age,hobby)
        }
      }
    })
    val runtime = epService.getEPRuntime()
    runtime.sendEvent(map, "user")
  }
}