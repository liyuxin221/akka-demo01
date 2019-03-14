package cn.itcast.demo02.master

import akka.actor.{Actor, ActorSystem, Props}
import cn.itcast.demo02.common._
import com.typesafe.config.ConfigFactory

import scala.collection.mutable
import scala.concurrent.duration._

/**
  * @author: Liyuxin wechat:13011800146.
  * @Title: Master
  * @ProjectName akka-demo02
  * @date: 2019/3/14 23:33
  * @description:
  */
class Master extends Actor {
  val interval_times = 5000
  val times_out = 10000

  val map = new mutable.HashMap[String, WorkerInfo]

  override def preStart(): Unit = {
    println("启动定时任务,清理worker")

    //导入隐式转换
    import context.dispatcher
    context.system.scheduler.schedule(0 millis, interval_times millis, self, CheckTimeOut)

  }

  override def receive: Receive = {
    case RegistMsg(workerInfo) => {
      if (!map.contains(workerInfo.id)) {
        map += (workerInfo.id -> workerInfo)

        sender ! RegistedMsg("注册成功")
      }
    }

    case SenderHeartBeat(workerId) => {
      if (map.contains(workerId)) {
        val workInfo = map.get(workerId).get
        workInfo.WORKER_STATE_TIME = System.currentTimeMillis()
      }
    }

    case CheckTimeOut => {
      for (workerInfo <- map.values) {
        if (System.currentTimeMillis() - workerInfo.WORKER_STATE_TIME > times_out) {
          map -= (workerInfo.id)
        }
      }
        println("----------")
        println(s"当前worker数量:"+map.size)
        map.foreach(t => println(t._2))
      }
    }
}


object Master {
  def main(args: Array[String]): Unit = {
    val host = args(0)
    val port = args(1)

    val configStr =
      s"""
akka.actor.provider="akka.remote.RemoteActorRefProvider"
akka.remote.netty.tcp.hostname="$host"
akka.remote.netty.tcp.port="$port"
    """.stripMargin
    val config = ConfigFactory.parseString(configStr)

    val masterActorSystem = ActorSystem.apply("masterActorSystem", config)

    val masterActor = masterActorSystem.actorOf(Props(new Master), "masterActor")

  }
}
