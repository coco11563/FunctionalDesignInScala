package web

import java.io._
import java.net.{ServerSocket, Socket}
import java.util.StringTokenizer

class WebServer(bindingPort : Int) {
  def start() : Unit = {
    val socket = new ServerSocket(bindingPort)
    while(true) {
      val rec = socket.accept()
      new Thread(new requestHandler(rec)).start()
    }
  }

  class requestHandler(connection : Socket) extends Runnable{
    val CRLF : String= "\r\n"
    val RESPONSE_404_HEAD : String = "HTTP/1.0 404 Not Found" + CRLF
    val CONTENT_LINE : String = "Content-Type: text/html" + CRLF
    val RESPONSE_200_HEAD : String = "HTTP/1.0 200 OK" + CRLF
    override def run(): Unit = {
      val bos = new BufferedReader(new InputStreamReader(connection.getInputStream))
      val req = bos.readLine()
      println(req)
      val tokens = new StringTokenizer(req)
      tokens.nextToken
      val fileName = "." + tokens.nextToken
      val file = new File(fileName)
      val dos = new DataOutputStream(connection.getOutputStream)
      if (!file.exists()) {
        val head = RESPONSE_404_HEAD
        val content = CONTENT_LINE
        val entity = RESPONSE_404_HTML
        dos.writeBytes(head)
        dos.writeBytes(content)
        dos.writeBytes(entity)
      } else {
        val head = RESPONSE_200_HEAD
        val content = contentType(fileName)
        val fis = new FileInputStream(fileName)
        dos.writeBytes(head)
        dos.writeBytes(content)
        sendBytes(fis, dos)
      }
      dos.close()
      bos.close()
      connection.close()
    }

    private def contentType(fileName: String): String = {
      val contentType = if (fileName.endsWith(".htm") || fileName.endsWith(".html")) "text/html"
      else if (fileName.endsWith(".ram") || fileName.endsWith(".ra")) "audio/x-pn-realaudio"
      else "application/octet-stream"
      s"Content-Type: $contentType $CRLF"
    }

    import java.io.FileInputStream

    private def sendBytes(fis: FileInputStream, os: DataOutputStream): Unit = { // Construct a 1K buffer to hold bytes on their way to the socket.
      val buffer = new Array[Byte](1024)
      var bytes = 0
      // Copy requested file into the socket's output stream.
      while ( {
        bytes = fis.read(buffer)
        bytes != -1
      }) os.write(buffer, 0, bytes)
    }
    val RESPONSE_404_HTML : String = "<HTML>" +
      "<HEAD><TITLE>Not Found</TITLE></HEAD>" +
      "<BODY>Not Found</BODY></HTML>"
  }


}
object WebServer {
  def main(args: Array[String]): Unit = {
    val ws = new WebServer(1090)
    ws.start()
  }
}