package com.twitter.finaglezk.protocol

import org.jboss.netty.buffer.{ChannelBuffers, ChannelBufferInputStream}
import org.jboss.netty.channel.{Channel ,ChannelHandlerContext, ChannelPipeline}
import org.jboss.netty.handler.codec.oneone.{OneToOneEncoder, OneToOneDecoder}

class ByteEncoder extends OneToOneEncoder {

    override def encode(ctx: ChannelHandlerContext, channel: Channel, msg: Object): Object = {
      
      val  buf = ChannelBuffers.buffer(1048575); //default zk buffer size 0xFFFFF
      val f = msg.asInstanceOf[Array[Byte]]
      buf.writeBytes(f);
      return buf
    }

    private def removeAllPipelineHandlers(pipe: ChannelPipeline) {
      while (pipe.getFirst != null) {
        pipe.removeFirst();
      }
    }
}

class ByteDecoder extends OneToOneDecoder {
    def decode(ctx: ChannelHandlerContext, channel: Channel, msg: AnyRef) = {
      msg
    }
}