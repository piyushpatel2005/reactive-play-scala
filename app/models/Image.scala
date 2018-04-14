package models

/**
  * This class specifies details of an image
  */
case class Image(
                var id: Option[Long],
                var productId: Option[Long],
                var url: String
                ) {
  override def toString: String = {
    "Image {productId: " + productId.getOrElse(0) +
    ", url: " + url + "}"
  }
}
