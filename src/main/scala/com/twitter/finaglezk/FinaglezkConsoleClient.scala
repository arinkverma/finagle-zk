package com.twitter.finaglezk

import com.twitter.conversions.time._
import com.twitter.finagle.thrift.ThriftClientFramedCodec
import java.net.InetSocketAddress
import scala.tools.nsc.interpreter._
import scala.tools.nsc.Settings

import org.jboss.netty.buffer.ChannelBuffer

import com.twitter.finagle.Service
import com.twitter.finagle.builder.ClientBuilder
import com.twitter.logging.Logger

import com.twitter.finaglezk.protocol.ZkeeperCodec
import com.twitter.finaglezk.protocol.commands.{ Connect, Ping, Close}
import scala.concurrent.ops._


class InterfeaceClass(address:String){

  var client: Service[Array[Byte], AnyRef] = null;
  def start()={
    client = ClientBuilder()
      .codec(ZkeeperCodec)
      .hosts(address)
      .hostConnectionLimit(1)
      .build()

    client(Connect.serialize()) onSuccess { result =>
        Connect.deserialize(result.asInstanceOf[ChannelBuffer])
        println("\nStart with\n>Session ID:"+Connect.sessionId)
        println(">timeOut:"+Connect.timeOut)
        println(">This demo will make ping 3 times, then will close the connection")
      } onFailure { error =>
        error.printStackTrace()
      } ensure {
         spawn {
          var a = 0;
          for( a <- 1 to 3){
            println( "Ping " + a );
            Thread.sleep(Connect.timeOut*2/3);
            ping()
          }
          close()
        }
    }

  }

  def ping()={

    client(Ping.serialize()) onSuccess { result =>
        Ping.deserialize(result.asInstanceOf[ChannelBuffer])
       // println("\nStart with\nSession ID:"+Connect.sessionId)
       // println("timeOut:"+Connect.timeOut)
    } onFailure { error =>
        error.printStackTrace()
    }

  }

  def close()={
    println( "<Sending close signal");
    client(Close.serialize()) onSuccess { result =>
        Close.deserialize(result.asInstanceOf[ChannelBuffer])
        println( ">Got response to close");
       // println("\nStart with\nSession ID:"+Connect.sessionId)
       // println("timeOut:"+Connect.timeOut)
    } onFailure { error =>
        error.printStackTrace()
    }

  }

}


object FinaglezkConsoleClient extends App {

  val client = new InterfeaceClass(args(0))

  val intLoop = new ILoop()

  Console.println("\n\n\n'client' is bound to your thrift zk.\nUse zk.start to start client connection\n[note] Ping doesnt start as deamon process, use ctrl+C to exit.")
  intLoop.setPrompt("\nzk-client> ")

  intLoop.settings = {
    val s = new Settings(Console.println)
    s.embeddedDefaults[InterfeaceClass]
    s.Yreplsync.value = true
    s
  }

  intLoop.createInterpreter()
  intLoop.in = new JLineReader(new JLineCompletion(intLoop))

  intLoop.intp.beQuietDuring {
    intLoop.intp.interpret("""def exit = println("Type :quit to resume program execution.")""")
    intLoop.intp.bind(NamedParam("zk", client))
  }

  intLoop.loop()
  intLoop.closeInterpreter()
}
