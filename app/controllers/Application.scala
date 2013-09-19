package controllers

/* Copyright 2013 Intelligent Technology Inc.
 *
 * Released under the MIT license
 * See http://opensource.org/licenses/mit-license.php for full text.
 */

import play.api._
import play.api.mvc._
import models._
import play.api.data._
import play.api.data.Forms._


object Application extends Controller {
  
  def index = Action {
    Ok("Hello World")
  }

  def posts = Action {
    Ok(views.html.index(Posts.all()))
  }

  def showNewPost = Action {
    Ok(views.html.newPost(postForm))
  }

  def newPost = Action { implicit request =>
    postForm.bindFromRequest.fold(
      errors => BadRequest(views.html.newPost(errors)),
      form => {
        Posts.create(form._1, form._2)
        Redirect(routes.Application.posts())
      }
    )
  }

  def deletePost(id: Long) = Action {
    Posts.delete(id)
    Redirect(routes.Application.posts())
  }

  def showPost(id: Long) = Action {
    Ok(views.html.post(Posts(id)))
  }

  val postForm = Form(
    tuple(
      "title" -> nonEmptyText,
      "text" -> nonEmptyText
    )
  )

}