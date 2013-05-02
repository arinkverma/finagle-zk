package com.twitter.finaglezk.protocol

import org.jboss.netty.buffer.ChannelBuffer

abstract class basecommand{
  def serialize(): Array[Byte]
  def deserialize(msg:ChannelBuffer)

}

