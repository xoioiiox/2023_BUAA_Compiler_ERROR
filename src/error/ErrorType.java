package error;

public enum ErrorType {
    a, //非法符号
    b, //名字重定义
    c, //名字未定义
    d, //函数参数个数不匹配
    e, //函数参数类型不匹配
    f, //无返回值的函数存在不匹配的return
    g, //有返回值的函数缺少return
    h, //不能改变常量的值
    i, // 缺少分号
    j, // 缺少右小括号
    k, // 缺少右中括号
    l, // print中格式符数量不匹配
    m // 在非循环块中使用break, continue
}
