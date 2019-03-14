package cn.itcast.demo02.slaver

import java.util.UUID

import akka.actor.{Actor, ActorSelection, ActorSystem, Props}
import cn.itcast.demo02.common._
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration._

/**
  * @author: Liyuxin wechat:13011800146.
  * @Title: Slaver
  * @ProjectName akka-demo02
  * @date: 2019/3/15 0:34
  * @description:
  */
class Worker(masterHost:String,masterPort:String) extends Actor{
  var masterActor:ActorSelection=null
  val workerId=UUID.randomUUID().toString
  val memory=100+(math.random*100).toInt
  val cpu: Int = 6+(math.random*10).toInt

  val HEARTBEAT_TIME=2000

  override def preStart(): Unit = {
    masterActor = context.actorSelection(s"akka.tcp://masterActorSystem@$masterHost:$masterPort/user/masterActor")
    masterActor!RegistMsg(new WorkerInfo(workerId,memory,cpu))
  }

  override def receive: Receive = {
    case RegistedMsg(msg)=>{
      println(msg)
      import context.dispatcher
      context.system.scheduler.schedule(0 millis,HEARTBEAT_TIME millis,self,HeartBeat)
    }

    case HeartBeat=>{
      masterActor!SenderHeartBeat(workerId)
    }
  }

}

object Worker{
  def main(args: Array[String]): Unit = {
    //akka配置信息中需要用到变量
    val host=args(0)
    val port=args(1)
    val masterHost=args(2)
    val masterPort=args(3)
    //akka配置信息
    val configStr=
      s"""
         |akka.actor.provider="akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname="$host"
         |akka.remote.netty.tcp.port="$port"
       """.stripMargin
    //创建config对象
    val config=ConfigFactory.parseString(configStr)
    //创建ActorSystem 对象
    val workerActorSystem=ActorSystem("workerActorSystem",config)
    //创建actor
    val workerActor =workerActorSystem.actorOf(Props(new Worker(masterHost,masterPort)),"workerActor")
  }
}
