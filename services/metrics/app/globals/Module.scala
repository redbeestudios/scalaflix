package globals

import com.google.inject.AbstractModule
import play.api.{Configuration, Environment}

/**
  * Main Guice module.
  *
  * @param environment   Play Environment.
  * @param configuration Play Configuration instance.
  */
class Module(environment: Environment, configuration: Configuration) extends AbstractModule {

  override def configure(): Unit = {}
}
