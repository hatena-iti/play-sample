package models

/* Copyright 2013 Intelligent Technology Inc.
 *
 * Released under the MIT license
 * See http://opensource.org/licenses/mit-license.php for full text.
 */

/**
 * Created with IntelliJ IDEA.
 * User: sakura
 * Date: 2013/09/18
 * Time: 17:27
 */
import play.api.db.slick.Config.driver.simple._
import play.api.Play.current

case class Post(id: Long, title: String, text: String)

object Posts extends Table[Post]("post") {
  def id = column[Long]("post_id", O.PrimaryKey, O.AutoInc)
  def title = column[String]("title")
  def text = column[String]("text")
  def * = id ~ title ~ text <> (Post, Post.unapply _)
  def ins = title ~ text returning id

  def withSession[T](f: Session => T): T = {
    play.api.db.slick.DB.withSession{ implicit session:Session =>
      f(session)
    }
  }

  def all() = {
    play.api.db.slick.DB.withSession{ implicit session:Session =>
      Query(Posts).sortBy(_.id).list
    }
  }

  def create(title: String, text: String) = withSession { implicit session: Session =>
    Posts.ins.insert(title, text)
  }

  def delete(id: Long) = withSession { implicit session: Session =>
    Posts.where(_.id === id).delete
  }

  def apply(id: Long): Post = withSession { implicit session: Session =>
    Query(Posts).where(_.id === id).list.head
  }
}
