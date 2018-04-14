package models

/**
  * This class defines Review domain class for a product
  */
case class Review(
                 var id: Option[Long],
                 var productId: Option[Long],
                 var author: String,
                 var comment: String
                 ) {
  override def toString: String = {
    "Review {id: " + id +
    ", productId: " + productId +
    ", author: " + author +
    ", comment: " + comment + "}"
  }
}

