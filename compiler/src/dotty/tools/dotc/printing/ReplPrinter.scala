package dotty.tools.dotc.printing

import dotty.tools.dotc.core.Constants
import dotty.tools.dotc.core.Constants.Constant
import dotty.tools.dotc.core.Contexts._
import dotty.tools.dotc.core.Flags._
import dotty.tools.dotc.core.NameOps._
import dotty.tools.dotc.core.Symbols._
import dotty.tools.dotc.core.Types.{ExprType, TypeAlias}
import dotty.tools.dotc.printing.Texts._

import scala.language.implicitConversions

class ReplPrinter(_ctx: Context) extends DecompilerPrinter(_ctx) {

  override protected def exprToText(tp: ExprType): Text =
    ": " ~ toText(tp.resType)

  override def toText(sym: Symbol): Text =
    if (sym.name.isReplAssignName) nameString(sym.name)
    else keyString(sym) ~~ nameString(sym.name.stripModuleClassSuffix)

  override def toText(const: Constant): Text =
    if (const.tag == Constants.StringTag) Str('"' + const.value.toString + '"')
    else Str(const.value.toString)

  override def dclText(sym: Symbol): Text = {
    toText(sym) ~ {
      if (sym.is(Method)) toText(sym.info)
      else if (sym.isType && sym.info.isInstanceOf[TypeAlias]) toText(sym.info)
      else if (sym.isType || sym.isClass) ""
      else ":" ~~ toText(sym.info)
    }
  }
}
