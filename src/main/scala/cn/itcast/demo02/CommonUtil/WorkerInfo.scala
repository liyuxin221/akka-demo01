package cn.itcast.demo02.CommonUtil

/**
  * @author: Liyuxin wechat:13011800146.
  * @Title: WorkerInfo
  * @ProjectName akka-demo02
  * @date: 2019/3/14 23:35
  * @description:
  */
class WorkerInfo(val workerId:String,val memory:Int,val cpu:Int) {
  var lastHeartBeatTime:Long=_
}
