package controllers

import play.api._
import data.Form
import data.Forms._
import libs.json.Json
import play.api.mvc._

object Application extends Controller with Secured{
  

  def check(mail: String, password: String) = mail == "tony@stark.com" && password == "ironman"

  val loginForm = Form(
    tuple(
      "mail" -> text,
      "password" -> text
    ) verifying ("Invalid email or password", result => result match {
      case (email, password) => check(email, password)
    })
  )

  def protectedResource = IsAuthenticated{
    username => _ =>

    Ok(Json.obj("test"->"1234"))
  }

  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => Forbidden,
      user => {
        Ok.withSession("mail" -> user._1)
      }
    )
  }

  def logout = Action{
    Ok.withNewSession
  }




}

/**
 * Provide security features
 */
trait Secured {

  /**
   * Retrieve the connected user email.
   */
  private def username(request: RequestHeader) = request.session.get("mail")

  /**
   * Redirect to login if the user in not authorized.
   */
  private def onUnauthorized(request: RequestHeader) = Results.Unauthorized

  // --

  /**
   * Action for authenticated users.
   */
  def IsAuthenticated(f: => String => Request[AnyContent] => Result) =
    Security.Authenticated(username, onUnauthorized){
      user => Action(request => f(user)(request))
    }


}
