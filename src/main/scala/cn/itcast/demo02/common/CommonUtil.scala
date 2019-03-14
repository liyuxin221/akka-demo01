package cn.itcast.demo02.common

/**
  */
class WorkerInfo(val id:String, val memory:Int, val cpu:Int) extends BaseMsg {
  var WORKER_STATE_TIME:Long=_

  override def toString: String=
  s"id:$id,memory:$memory,cpu:$cpu."
}


class BaseMsg extends  Serializable

case class  RegistMsg(workerInfo: WorkerInfo) extends BaseMsg

case object  CheckTimeOut

case class RegistedMsg(msg:String) extends BaseMsg

case class SenderHeartBeat(workerId:String) extends BaseMsg

case object HeartBeat


