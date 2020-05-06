package services

import java.awt.image.BufferedImage
import java.io.{ByteArrayInputStream, ByteArrayOutputStream, File, InputStream}

import com.xuggle.mediatool.event.IVideoPictureEvent
import com.xuggle.mediatool.{MediaListenerAdapter, ToolFactory}
import com.xuggle.xuggler.{Global, IContainer}
import javax.imageio.ImageIO
import javax.inject.{Inject, Singleton}
import XluggerService.IMAGE_FORMAT
import globals.{ApplicationResult, MapMarkerContext}
import converters._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class XluggerService @Inject()()(implicit ec: ExecutionContext) {

  def getVideoDuration(filepath: String): Long = {
    // first we create a Xuggler container object// first we create a Xuggler container object
    val container = IContainer.make
    // we attempt to open up the container
    val result = container.open(filepath, IContainer.Type.READ, null)
    // check if the operation was successful
    if (result < 0) throw new RuntimeException("Failed to open media file")
    // query for the total duration
    container.getDuration / 1000000
  }

  def generateThumbnail(
      filepath: String,
      frameAtSecond: Int
    )(implicit mapMarkerContext: MapMarkerContext
    ): ApplicationResult[InputStream] =
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
    } toApplicationResult ()

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
