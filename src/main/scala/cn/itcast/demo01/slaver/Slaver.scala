package cn.itcast.demo01.slaver

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory


/**
  * @author: Liyuxin wechat:13011800146.
  * @Title: slaver
  * @ProjectName akka-demo02
  * @date: 2019/3/14 21:40
  * @description:
  */
class Slaver(masterHost:String,masterPort:String) extends Actor {
  override def preStart(): Unit = {
    println("slaver已启动.")
    //获取master actor的引用
    //利用actorContext全局对象.可以再已存在的actor中,寻找目标actor.
    val master = context.actorSelection(s"akka.tcp://masterActorySystem@$masterHost:$masterPort/user/masterActor")
    master!"connect"
  }

  override def receive: Receive = {
    case "connect1"=>{
      println("a client connected1")
    }
    case "connect2"=>{
      println("a client connected2")
    }
    case "success"=>{
      println("注册成功.")
      sender!"slaver已注册成功,告知master"
    }


  }
}

object Slaver {
  def main(args: Array[String]): Unit = {
    val slaverHost = args(0)
    val slaverPort = args(1)
    val masterHost = args(2)
    val masterPort = args(3)


    val strConfig =
      s"""
         |akka.actor.provider="akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname="$slaverHost"
         |akka.remote.netty.tcp.port="$slaverPort"
       """.stripMargin
    val config = ConfigFactory.parseString(strConfig)
    val slaverActorySystem = ActorSystem("slaverActorySystem", config)

    val masterActor = slaverActorySystem.actorOf(Props(new Slaver(masterHost,masterPort)),"masterActor")

//    masterActor!"connect1"
  }
}
