package com.layfones.calculator.calc

import java.math.BigDecimal
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MixedOperation(  //混合运算类
    private val accuracy //保存设置的运算精度
    : Int
) {



    // private final Pattern p = Pattern.compile("(^-|(?<=\\D)-)?[\\d.]+|[+\\-*\\/%!()√^πe]|log|ln|a?sin|a?cos|a?tan"); //用于匹配运算符和参数的正则表达式
    private val p: Pattern =
        Pattern.compile("(^-|(?<=\\D)-)?[\\d.]+|[+\\-*/%!()√^]|log") //用于匹配运算符和参数的正则表达式
    private val priority = HashMap<String, Int?>() //存放运算符对应的优先级，查字典判断运算符优先级
    private val eq1 = ArrayList<String>() //保存算式的列表
    private val eq2 = ArrayList<String>() //保存算式的中间列表
    private val stk: Stack<String> =
        Stack<String>() //使用该堆栈将中缀表达式转为后缀表达式，这儿的Stack类型可以改成使用成BigDecimal

    // public BigDecimalMath bdm = new BigDecimalMath(34); //精确到小数点后34位
    private var bdm: BigDecimalMath = BigDecimalMath(accuracy)

    init { //构造函数，初始化运算符优先级及精度
        //调用BigDecimal的构造函数设置运算精度
        priority["("] = 0 //左括号优先级最低，但是遇到左括号直接入栈
        priority[")"] = 0 //不会用到右括号的优先级，因为右括号不可能入栈，在此只把右括号当作一个运算符

        //双目运算符
        priority["+"] = 1 //加法优先级为1
        priority["-"] = 1 //减法优先级为1
        priority["*"] = 2 //乘法优先级为2
        priority["/"] = 2 //除法优先级为2
        priority["%"] = 2 //取余优先级为2
        priority["^"] = 3 //n次方优先级为3
        priority["√"] = 4 //开根号优先级为4
        priority["log"] = 4 //log优先级为4
    }

    //预处理，将运算参数提取出来保存为单独一个字符串
    //比如输入"1.23+4.56"，整体为一个字符串，经过处理后被分割成三个字符串"1.23","+","4.56"
    private fun pretreatment(s: String) {
        val m: Matcher = p.matcher(s) //将输入的算式s与正则表达式进行匹配
        while (m.find()) {
            eq1.add(m.group()) //保存算式中运算符的位置
        }
    }

    //中缀表达式转后缀表达式
    //比如输入为1+5*7，转为后缀表达式为157*+
    private fun infix2Postfix() {
        for (s in eq1) {
            if (priority[s] != null) { //对运算符进行处理
                if (stk.empty() || s == "(") { //如果栈为空或者是左括号，直接入栈。为什么左括号也要直接入栈？考虑到括号嵌套问题，如6+((1+2)*3+4)*5
                    stk.push(s)
                } else if (s == ")") { //遇到右括号，将栈顶元素一直出栈直到遇到左括号为止，并将该左括号出栈
                    //栈顶元素不为左括号，将该运算符出栈并添加到列表中
                    while (!stk.peek().equals("(")) {
                        eq2.add(stk.pop())
                    }
                    stk.pop() //将左括号出栈
                } else if (priority[s]!! > priority[stk.peek()]!!) { //如果该运算符优先级高于栈顶运算符优先级,直接入栈
                    stk.push(s)
                } else { //该运算符优先级低于或等于栈顶运算符优先级，分四种情况
                    while (!stk.empty() && !stk.peek()
                            .equals("(") && priority[stk.peek()]!! >= priority[s]!!
                    ) {
                        eq2.add(stk.pop())
                    }
                    stk.push(s)
                }
            } else { //对参数进行处理，参数直接添加进列表
                eq2.add(s)
            }
        }
        while (!stk.empty()) {
            eq2.add(stk.pop())
        }
    }

    //对后缀表达式计算
    private fun postfixCalculate(): String {
        var p1: BigDecimal
        var p2: BigDecimal
        var p3: BigDecimal
        for (s in eq2) {
            if (priority[s] == null) { //对参数进行处理
                stk.push(s) //参数直接入栈
            } else { //对运算符进行处理
                p1 = BigDecimal(stk.pop())
                p2 = BigDecimal(stk.pop())
                p3 = when (s) {
                    "+" -> p2.add(p1)
                    "-" -> p2.subtract(p1)
                    "*" -> p2.multiply(p1)
                    "/" -> p2.divide(p1, 32, BigDecimal.ROUND_HALF_EVEN)
                    "%" -> p2.remainder(p1)
                    "^" -> bdm.pow(p2, p1)
                    "√" -> bdm.pow(
                        p1,
                        BigDecimal.ONE.divide(p2, bdm.getAccuracy(), BigDecimal.ROUND_HALF_EVEN)
                    )
                    "log" -> bdm.log(p1, p2)
                    else -> BigDecimal.ZERO
                }
                stk.push(p3.toString())
            }
        }
        return stk.pop()
    }

    //调用该函数计算结果
    fun getMixedOperationRes(s: String): BigDecimal {
        //运算前进行一些初始化操作，清空所用的列表和堆栈
        eq1.clear()
        eq2.clear()
        while (!stk.empty()) {
            stk.pop()
        }
        pretreatment(s)
        infix2Postfix()
        return BigDecimal(postfixCalculate())
    }
}
