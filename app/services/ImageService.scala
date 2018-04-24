package services

import com.google.inject.Singleton
import models.Image


trait IImageService extends BaseService[Image] {
  def insert(image: Image): Long
  def update(id: Long, image: Image): Boolean
  def remove(id: Long): Boolean
  def findById(id: Long): Option[Image]
  def findAll(): Option[List[Image]]
}

@Singleton
class ImageService extends IImageService{

  private def validateId(id: Long): Unit = {
    val entry = inMemoryDB.get(id)
    if(entry == null)
      throw new RuntimeException("Could not find Image: " + id)
  }
  override def insert(image: Image): Long = {
    val id = idCounter.incrementAndGet()
    image.id = Some(id)
    inMemoryDB.put(id,image)
    id
  }

  override def update(id: Long, image: Image): Boolean = {
    validateId(id)
    image.id = Some(id)
    inMemoryDB.put(id, image)
    true
  }

  override def remove(id: Long): Boolean = {
    validateId(id)
    inMemoryDB.remove(id)
    true
  }

  override def findById(id: Long): Option[Image] = {
    inMemoryDB.get(id)
  }

  override def findAll(): Option[List[Image]] = {
    if(inMemoryDB.values.toList == null || inMemoryDB.values.toList.size == 0)
      return None
    Some(inMemoryDB.values.toList)
  }
}
