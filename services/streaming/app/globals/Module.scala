package globals

import akka.actor.ActorSystem
import com.google.inject.{AbstractModule, Provides}
import configurations._
import io.minio.MinioClient
import javax.inject.{Named, Singleton}
import play.api.libs.concurrent.CustomExecutionContext
import play.api.{Configuration, Environment}

import scala.concurrent.ExecutionContext

/**
  * Main Guice module
  * Add Configuration & Environment to Module Parameters when necessary.
  *
  * @param environment   Play Environment
  * @param configuration Play Configuration instance
  */
class Module(environment: Environment, configuration: Configuration) extends AbstractModule {

  val _ = environment // Just to not break at compile time for not using the environment field.

  override def configure(): Unit = bind(classOf[OnStopHook]).asEagerSingleton()

  /**
    * Bind a [[io.minio.MinioClient]] instance for dependency injection.
    *
    * @return A [[io.minio.MinioClient]] instance.
    */
  @Provides
  def minioClient: MinioClient = new MinioClient(
    this.configuration.get[String](MINIO_ENDPOINT),
    this.configuration.get[String](MINIO_ACCESS_KEY),
    this.configuration.get[String](MINIO_SECRET_KEY)
  )

  /**
    * Bind a [[scala.concurrent.ExecutionContext]] instance for external integration for dependency injection.
    *
    * @param system A [[akka.actor.ActorSystem]] instance.
    * @return A [[scala.concurrent.ExecutionContext]] instance for kafka integration.
    */
  @Provides @Singleton @Named(EXTERNAL_DISPATCHER)
  def externalExecutionContext(system: ActorSystem): ExecutionContext =
    new CustomExecutionContext(system, EXTERNAL_DISPATCHER) {}

  /**
    * Bind a [[scala.concurrent.ExecutionContext]] instance for database integration for dependency injection.
    *
    * @param system A [[akka.actor.ActorSystem]] instance.
    * @return A [[scala.concurrent.ExecutionContext]] instance for database integration.
    */
  @Provides @Singleton @Named(DATABASE_DISPATCHER)
  def databaseExecutionContext(system: ActorSystem): ExecutionContext =
    new CustomExecutionContext(system, DATABASE_DISPATCHER) {}

}
