package services

import com.google.inject.Singleton
import models.Review


trait IReviewService extends BaseService[Review] {
  def insert(review: Review):Long
  def update(id:Long, review:Review): Boolean
  def remove(id: Long): Boolean
  def findById(id: Long): Option[Review]
  def findAll(): Option[List[Review]]
}

@Singleton
class ReviewService extends IReviewService {

  private def validateId(id: Long) = {
    val entry = inMemoryDB.get(id)
    if(entry == null) throw new RuntimeException("Could not find Review " + id)
  }

  override def insert(review: Review): Long = {
    val id = idCounter.incrementAndGet();
    review.id = Some(id)
    inMemoryDB.put(id, review)
    id
  }

  override def update(id: Long, review: Review): Boolean = {
    validateId(id)
    review.id = Some(id)
    inMemoryDB.put(id, review)
    true
  }

  override def remove(id: Long): Boolean = {
    validateId(id)
    inMemoryDB.remove(id)
    true
  }

  override def findById(id: Long): Option[Review] = {
    inMemoryDB.get(id)
  }

  override def findAll(): Option[List[Review]] = {
    if(inMemoryDB.values.toList == null || inMemoryDB.values.toList.size == 0)
      return None
    Some(inMemoryDB.values.toList)
  }
}
