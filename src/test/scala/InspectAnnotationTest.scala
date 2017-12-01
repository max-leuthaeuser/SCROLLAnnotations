import org.scalatest.{Matchers, WordSpec}
import scroll.internal.Compartment
import scroll.internal.annotations.Inspect

/**
  * Simple version of the robot example without defining different Views using Compartments.
  *
  * The [[scroll.internal.annotations.Inspect]] annotation is used here to extract role-specific behavior and structure
  * using reflection during the creation of the annotated Compartment or Role.
  * The result is cached within [[scroll.internal.util.ReflectiveHelper]] so further
  * Role calls to it do not invoke reflection-based behavior and structure queries any longer.
  */
class InspectAnnotationTest extends WordSpec with Matchers {

  case class Robot(name: String)

  @Inspect
  class VariousRobotBehavior extends Compartment {

    case class ServiceRole() {
      def move(): Unit = {
        val name: String = +this name()
        val target: String = +this getTarget()
        val sensorValue: Int = +this readSensor()
        val actor: String = +this getActor()

        info(s"I am $name and moving to the $target with my $actor w.r.t. sensor value of $sensorValue.")
      }
    }

    case class NavigationRole() {
      def getTarget: String = "kitchen"
    }

    case class ObservingEnvironmentRole() {
      def readSensor: Int = 100
    }

    case class DriveableRole() {
      def getActor: String = "wheels"
    }

  }

  "the annotation" should {
    "inspect roles" in {
      new VariousRobotBehavior {
        Robot("Pete") play ServiceRole() play NavigationRole() play ObservingEnvironmentRole() play DriveableRole() move()
      }
    }
  }
}
