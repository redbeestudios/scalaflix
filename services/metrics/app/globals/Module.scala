package globals

import akka.actor.ActorSystem
import com.google.inject.{AbstractModule, Provides}
import javax.inject.{Named, Singleton}
import play.api.libs.concurrent.CustomExecutionContext
import play.api.{Configuration, Environment}
import models.config._

import scala.concurrent.ExecutionContext

/**
  * Main Guice module.
  *
  * @param environment   Play Environment.
  * @param configuration Play Configuration instance.
  */
class Module(environment: Environment, configuration: Configuration) extends AbstractModule {

  override def configure(): Unit = {}

  /**
    * Bind a [[scala.concurrent.ExecutionContext ExecutionContext]] instance for dependency injection.
    *
    * @param system An [[akka.actor.ActorSystem ActorSystem]] instance.
    * @return A [[scala.concurrent.ExecutionContext ExecutionContext]] instance.
    */
  @Provides @Singleton @Named(DATABASE_DISPATCHER)
  def dbExecutionContext(system: ActorSystem): ExecutionContext =
    new CustomExecutionContext(system, DATABASE_DISPATCHER) {}

}
