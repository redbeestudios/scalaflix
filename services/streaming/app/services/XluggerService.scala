package services

import java.awt.image.BufferedImage
import java.io.{ByteArrayInputStream, ByteArrayOutputStream, File, InputStream}

import com.xuggle.mediatool.event.IVideoPictureEvent
import com.xuggle.mediatool.{MediaListenerAdapter, ToolFactory}
import com.xuggle.xuggler.Global
import javax.imageio.ImageIO
import javax.inject.{Inject, Singleton}
import XluggerService.IMAGE_FORMAT
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class XluggerService @Inject()()(implicit ec: ExecutionContext) {

  def generateThumbnail(filepath: String, frameAtSecond: Int): Future[InputStream] =
    Future {
      // Create the media reader for the input file
      val mediaReader = ToolFactory.makeReader(filepath)
      // Set the buffered image type
      mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR)
      // Instantiate the custom MediaListener
      val isListener = new ImageSnapListener()
      mediaReader.addListener(isListener)
      // Read packet until the image is grabbed
      while (!isListener.isImageGrabbed) mediaReader.readPacket
      // return the BufferedImage as InputStream
      toInputString(isListener.thumbnail)
    }

  private def toInputString(bufferedImage: BufferedImage): InputStream = {
    val ouputStream = new ByteArrayOutputStream
    ImageIO.write(bufferedImage, IMAGE_FORMAT, ouputStream)
    new ByteArrayInputStream(ouputStream.toByteArray)
  }

  private class ImageSnapListener(frameAtSecond: Int = 1) extends MediaListenerAdapter {
    private val frameAtMillisecond: Long = Global.DEFAULT_PTS_PER_SECOND * frameAtSecond
    var isImageGrabbed                   = false
    var thumbnail: BufferedImage         = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR)

    override def onVideoPicture(event: IVideoPictureEvent): Unit =
      // if it's time to write the frame
      if (event.getTimeStamp >= frameAtMillisecond) {
        // get the buffered image
        thumbnail = event.getImage
        // set this var to true once an image is grabbed out of the movie
        this.isImageGrabbed = true
      }
  }

}

object XluggerService {
  final val IMAGE_FORMAT = "png"
}
