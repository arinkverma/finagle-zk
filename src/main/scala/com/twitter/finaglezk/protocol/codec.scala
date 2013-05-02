package com.twitter.finaglezk.protocol

import com.twitter.finagle.{Codec, CodecFactory}

import org.jboss.netty.channel.{Channels, ChannelPipelineFactory}


class ZkeeperCodec extends CodecFactory[Array[Byte], AnyRef] {
  def server = throw new Exception("Not yet implemented...")

  def client = Function.const {
    new Codec[Array[Byte], AnyRef] {
      def pipelineFactory = new ChannelPipelineFactory {
        def getPipeline = {
          val pipeline = Channels.pipeline()
          // add string encoder (downstream) /decoders (upstream)
          //Decodes a received ChannelBuffer into a byte array
          pipeline.addLast("encoder", new ByteEncoder())
          pipeline.addLast("decoder", new ByteDecoder())
          pipeline
        }
      }
    }
  }
}


object ZkeeperCodec extends ZkeeperCodec