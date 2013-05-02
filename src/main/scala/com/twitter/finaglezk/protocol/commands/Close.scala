package com.twitter.finaglezk.protocol.commands

import java.io.{ByteArrayOutputStream, DataOutputStream}
import org.jboss.netty.buffer.ChannelBuffer;

import com.twitter.finaglezk.protocol.basecommand

object Close extends basecommand {
  override def serialize(): Array[Byte] = {
    val data = new ByteArrayOutputStream()
    val stream = new DataOutputStream(data)
    stream.writeInt(1)
    stream.writeInt(-11)
    val b = data.toByteArray
    data.reset
    stream.writeInt(b.length)
    stream.write(b)
    return data.toByteArray
  }

  override def deserialize(msg:ChannelBuffer) = {

  }
}