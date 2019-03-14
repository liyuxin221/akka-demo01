package cn.itcast.demo01.master

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

/**
  * @author: Liyuxin wechat:13011800146.
  * @Title: Master
  * @ProjectName akka-demo02
  * @date: 2019/3/14 21:18
  * @description:
  */
class Master() extends Actor {
  override def preStart(): Unit = {
    println("master方法已启动")
  }

  override def receive: Receive = {
    case "actor已创建." => println("已接收信息")

    case "connect" => {
      println("slaver已连接")
      sender!"success"
    }
  }


}

object Master {
  def main(args: Array[String]): Unit = {
    val masterHost = args(0)
    val masterPort = args(1)

    val strConfig =
      s"""
         |akka.actor.provider="akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname="$masterHost"
         |akka.remote.netty.tcp.port="$masterPort"
      """.stripMargin

    //akka 配置的字符串
    val config = ConfigFactory.parseString(strConfig)
    //创建 actorSystem
    val masterActorySystem = ActorSystem.apply("masterActorySystem", config)

    val masterActor = masterActorySystem.actorOf(Props(new Master()), "masterActor")

//    masterActor ! "actor已创建."
  }
}
