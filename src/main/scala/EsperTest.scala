import java.util

import org.apache.spark.{SparkConf, SparkContext}
import com.espertech.esper.client.EPServiceProviderManager
import com.espertech.esper.client.EventBean
import com.espertech.esper.client.UpdateListener
import com.espertech.esper.client.EPRuntime
import scala.concurrent.JavaConversions._

object EsperTest {
  def main(args: Array[String]) {
    val file = "src/main/resources/data/users.txt"
    val conf = new SparkConf().setAppName("Simple Application")
    conf.setMaster("local")
    val sc = new SparkContext(conf)
    val users = sc.textFile(file)



    var n = 0
    users.foreach{ line =>
      val epService = EPServiceProviderManager.getDefaultProvider()
      val admin = epService.getEPAdministrator()
      if(n<1){


        val userType = new util.HashMap[String,Object]()
        userType.put("name", classOf[String])
        userType.put("age", classOf[Int])
        userType.put("hobby", classOf[String])
        admin.getConfiguration().addEventType("user", userType)

        //val epl = "select * from user where name='ww'"
        val epl = "select * from user where age > 20"
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
      }
      n += 1
      if(line != ""){
        val user = line.split(" ")
        val map = new util.HashMap[String,Any]()
        map.put("name", user(0))
        map.put("age", user(1).toInt)
        map.put("hobby",user(2))
        val runtime = epService.getEPRuntime()
        runtime.sendEvent(map, "user")
      }

    }
    sc.stop()
  }


}