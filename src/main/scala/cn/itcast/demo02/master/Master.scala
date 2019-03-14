package cn.itcast.demo02.master

import akka.actor.Actor

import scala.collection.mutable

/**
  * @author: Liyuxin wechat:13011800146.
  * @Title: Master
  * @ProjectName akka-demo02
  * @date: 2019/3/14 23:33
  * @description:
  */
class Master extends Actor{
  override def preStart(): Unit = {
    println("master constructor invoked.")

    private val workerMap=new mutable.HashMap[String,WorkerInfo]()
  }

}
