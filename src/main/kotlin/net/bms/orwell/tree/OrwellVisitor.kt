package net.bms.orwell.tree

import OrwellBaseVisitor
import OrwellLexer
import net.bms.orwell.valStore

open class OrwellVisitor: OrwellBaseVisitor<Node>() {
    override fun visitExpr(ctx: OrwellParser.ExprContext?): Node? =
        if (ctx != null) {
            visit(ctx.e())
        } else null

    override fun visitStat(ctx: OrwellParser.StatContext?): Node? =
        if (ctx != null) {
            visit(ctx.s())
        } else null

    override fun visitVal_def(ctx: OrwellParser.Val_defContext?): Node? =
        if (ctx != null) {
            val node = ValNode()
            val dec = visit(ctx.name) as ValNode
            node.id = dec.id
            node.value = visit(ctx.`val`)
            node
        } else null

    override fun visitVal_dec(ctx: OrwellParser.Val_decContext?): Node? =
        if (ctx != null) {
            val node = ValNode()
            node.id = ctx.name.text
            node.value = null
            node
        } else null

    override fun visitAssignment(ctx: OrwellParser.AssignmentContext?): Node? =
        if (ctx != null) {
            val node = ValNode()
            node.id = ctx.name.text
            node.value = visit(ctx.`val`)
            node
        } else null

    override fun visitNumber(ctx: OrwellParser.NumberContext?): Node? =
        if (ctx != null) {
            val node = NumberNode()
            node.value = ctx.`val`.text.toDouble()
            node
        } else null

    override fun visitNegExpr(ctx: OrwellParser.NegExprContext?): Node? =
        if (ctx != null) {
            val node = NegateNode()
            node.innerNode = visit(ctx.e())
            node
        } else null

    override fun visitBinExpr(ctx: OrwellParser.BinExprContext?): Node? =
        if (ctx != null) {
            val node = when (ctx.op.type) {
                OrwellLexer.OP_ADD -> {
                    AdditionNode()
                }
                OrwellLexer.OP_SUB -> {
                    SubtractionNode()
                }
                OrwellLexer.OP_DIV -> {
                    DivisionNode()
                }
                OrwellLexer.OP_MUL -> {
                    MultiplicationNode()
                }
                else -> null
            }
            if (node != null) {
                node.left = visit(ctx.left)
                node.right = visit(ctx.right)
                node
            } else null
        } else null

    override fun visitParenExpr(ctx: OrwellParser.ParenExprContext?): Node? =
        if (ctx != null) {
            visit(ctx.e())
        } else null

    override fun visitIdentifier(ctx: OrwellParser.IdentifierContext?): Node? =
        if(ctx != null) {
            val node: Node
            if (valStore.containsKey(ctx.name.text))  {
                val value = valStore[ctx.name.text]
                node = NumberNode()
                node.value = value as Double
                node
            }
            else {
                println("\tERROR: The variable ${ctx.name.text} does not exist.")
                node = NumberNode()
                node.value = Double.NaN
                node
            }
        } else null

    override fun visitFun_def(ctx: OrwellParser.Fun_defContext?): Node? =
        if (ctx != null) {
            val node = FunDefNode()
            for (i in 0 until ctx.names.size) {
                val arg = ValNode()
                arg.value = null
                arg.id = ctx.names[i].text
                node.arg.add(arg)
            }
            node.`fun` = ctx.name.text
            node.expr = ctx.e()
            node
        } else null

    override fun visitFun_call(ctx: OrwellParser.Fun_callContext?): Node? =
        if (ctx != null) {
            val node = FunCallNode()
            for (e in ctx.args) {
                visit(e)?.let { node.args.add(it.toValNode()) }
            }
            node.`fun` = ctx.name.text
            node
        } else null
}