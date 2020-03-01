package globals

import akka.actor.ActorSystem
import javax.inject.{Inject, Singleton}
import play.api.inject.ApplicationLifecycle

/**
  * Configure what to execute on application stops.
  *
  * @param lifecycle   An [[play.api.inject.ApplicationLifecycle]] register.
  * @param system      An [[akka.actor.ActorSystem]] instance.
  */
@Singleton
class OnStopHook @Inject()(lifecycle: ApplicationLifecycle, system: ActorSystem) {

  lifecycle.addStopHook { () =>
    system.terminate()
    system.whenTerminated
  }

}
