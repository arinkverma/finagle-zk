package com.twitter.finaglezk

import org.jboss.netty.buffer.ChannelBuffer

import com.twitter.finagle.Service
import com.twitter.finagle.builder.ClientBuilder
import com.twitter.logging.Logger

import com.twitter.finaglezk.protocol.ZkeeperCodec
import com.twitter.finaglezk.protocol.commands.{ Connect, Ping, Close}



object  Main  {

  def main(args: Array[String]) {
      // Construct a client, and connect it to localhost:8080
      val client: Service[Array[Byte], AnyRef] = ClientBuilder()
        .codec(ZkeeperCodec)
        .hosts("127.0.0.1:2181")
        .hostConnectionLimit(1)
        .build()

      // Issue a newline-delimited request, respond to the result
      // asynchronously:
      client(Connect.serialize()) onSuccess { result =>
        Connect.deserialize(result.asInstanceOf[ChannelBuffer])
      } onFailure { error =>
        error.printStackTrace()
      } 


      client(Ping.serialize()) onSuccess { result =>
        Ping.deserialize(result.asInstanceOf[ChannelBuffer])
      } onFailure { error =>
        error.printStackTrace()
      }

      client(Close.serialize()) onSuccess { result =>
        Close.deserialize(result.asInstanceOf[ChannelBuffer])
      } onFailure { error =>
        error.printStackTrace()
      } ensure {
        // All done! Close TCP connection(s):
        client.release()
      }
  }

}
