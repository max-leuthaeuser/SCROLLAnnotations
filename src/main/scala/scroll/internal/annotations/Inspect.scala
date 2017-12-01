package scroll.internal.annotations

import scala.language.experimental.macros
import scala.annotation.StaticAnnotation
import scala.annotation.compileTimeOnly
import scala.reflect.macros.whitebox

/**
  * Use this annotation to extract role-specific behavior and structure using reflection during the creation
  * of the annotated Compartment or Role. The result is cached within [[scroll.internal.util.ReflectiveHelper]] so further
  * Role calls to it do not invoke reflection-based behavior and structure queries any longer.
  */
@compileTimeOnly("Enable macro paradise to expand macro annotations!")
class Inspect extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro InspectMacro.impl
}

object InspectMacro {
  def impl(c: whitebox.Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._

    val result = {
      annottees.head.tree match {
        case q"$mods class $tpname[..$tparams] $ctorMods(...$paramss) extends Compartment with ..$parents { ..$body }" =>
          c.info(c.enclosingPosition, s"Encountered @Inspect annotation at Compartment '${tpname.toString()}'!", force = true)
          val containedRoles = body.collect {
            case cd@ClassDef(_, name, _, _) =>
              c.info(cd.pos, s"Adding fields and methods for role '${name.decodedName.toString}' to the cache.", force = true)
              q"""
                 ReflectiveHelper.addToMethodCache(classOf[$name])
                 ReflectiveHelper.addToFieldCache(classOf[$name])
                """
          }
          q"""$mods class $tpname[..$tparams] $ctorMods(...$paramss) extends Compartment with ..$parents{
            import scroll.internal.util.ReflectiveHelper
            $containedRoles
            ..$body
          }"""
        case role@q"$mods class $tpname[..$tparams] $ctorMods(...$paramss) extends $supers with ..$parents { ..$body }" =>
          role match {
            case ClassDef(_, name, _, _) =>
              c.info(c.enclosingPosition, s"Encountered @Inspect annotation at role '${name.decodedName.toString}' and adding its fields and methods to the cache.", force = true)
              q"""$mods class $tpname[..$tparams] $ctorMods(...$paramss) extends $supers with ..$parents {
                import scroll.internal.util.ReflectiveHelper
                ReflectiveHelper.addToMethodCache(classOf[$name])
                ReflectiveHelper.addToFieldCache(classOf[$name])
                ..$body
                }"""
            case _ => role
          }
        case _ => c.abort(c.enclosingPosition, "Annotation @Inspect can only be used with (case) classes or traits!")
      }
    }
    c.Expr[Any](result)
  }
}