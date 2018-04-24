package controllers

import com.google.inject.{Inject, Singleton}
import models.Image
import play.Logger
import play.api.data
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.mvc.{Action, Controller}
import services.{IImageService, IProductService}

@Singleton
class ImageController @Inject()
(val messagesApi: MessagesApi,
 val productService: IProductService,
 val service: IImageService) extends Controller with I18nSupport {
  val imageForm: Form[Image] = data.Form (
    mapping (
      "id" -> optional(longNumber),
      "productId" -> optional(longNumber),
      "url" -> text
    )(Image.apply)(Image.unapply)
  )

  def index = Action { implicit request =>
    val images = service.findAll().getOrElse(Seq())
    Logger.info("info called. Images: " + images)
    Ok(views.html.image_index(images))
  }

  def blank = Action { implicit request =>
    Logger.info("blank called.")
    Ok(views.html.image_details(None, imageForm, productService.findAllProducts()))
  }

  def details(id:Long) = Action {implicit request =>
    Logger.info("details called. id: "+ id)
    val image = service.findById(id).get
    Ok(views.html.image_details(Some(id), imageForm.fill(image), productService.findAllProducts()))
  }

  def insert() = Action { implicit request =>
    Logger.info("insert called.")
    imageForm.bindFromRequest().fold(
      form => {
        BadRequest(views.html.image_details(None, form, productService.findAllProducts()))
      },
      image => {
        if(image.productId == null || image.productId.getOrElse(0) == 0) {
          Redirect(routes.ImageController.blank).flashing("error" -> "Product Id cannot be null")
        } else {
          if(image.url == null || "".equals(image.url))
            image.url = "/assets/images/default_product.png"
          val id = service.insert(image)
          Redirect(routes.ImageController.index).flashing("success" -> Messages("success.insert",  id))
        }
      }
    )
  }

  def update(id: Long) = Action { implicit request =>
    Logger.info("update called. id: " + id)
    imageForm.bindFromRequest().fold(
      form => {
        Ok(views.html.image_details(Some(id), form, null)).flashing("error" -> "Fix the errors!")
      },
      image => {
        service.update(id, image)
        Redirect(routes.ImageController.index).flashing("success" -> Messages("success.update", image.id))
      }
    )
  }

  def remove(id: Long) = Action {
    service.findById(id).map { image =>
      service.remove(id)
      Redirect(routes.ImageController.index).flashing("success" -> Messages("success.delete", image.id))
    }.getOrElse(NotFound)
  }
}
