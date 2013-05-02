package com.twitter.finaglezk.protocol.commands

import java.io.{ByteArrayOutputStream, DataOutputStream}

import org.jboss.netty.buffer.ChannelBuffer

import com.twitter.finaglezk.protocol.basecommand


object Connect extends basecommand{
  var protocolVersion : Int = 0
  var lastZxidSeen : Long = 0
  var timeOut : Int = 10000
  var sessionId : Long = 0
  var passwd = Array[Byte](0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)

  override def serialize(): Array[Byte] = {
    val data = new ByteArrayOutputStream()
    val stream = new DataOutputStream(data)

    stream.writeInt(protocolVersion)
    stream.writeLong(lastZxidSeen)
    stream.writeInt(timeOut)
    stream.writeLong(sessionId)
    stream.writeInt(passwd.length)
    stream.write(passwd)

    val b = data.toByteArray
    data.reset
    stream.writeInt(b.length)
    stream.write(b)

    return data.toByteArray
  }

  override def deserialize(msg:ChannelBuffer) = {
      var buff = msg.asInstanceOf[ChannelBuffer]
      var size = buff.readInt()
      protocolVersion = buff.readInt()
      timeOut = buff.readInt()
      sessionId = buff.readLong()
  }
}

