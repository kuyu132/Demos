package com.kuyuzhiqi.testdemo.pick;

/**
 * created by wangguoqun at 2019-06-21
 */
//结点类
public class Node<T> {

    T element; //数据域
    Node next;  //后继指针域
    Node prior; //前驱指针域

    public Node(T element) {
        this.element = element;
    }

    //头结点的构造方法
    public Node(Node nextval) {
        this.next = nextval;
    }

    //非头结点的构造方法
    public Node(T obj, Node nextval) {
        this.element = obj;
        this.next = nextval;
    }

    //获得当前结点的后继结点
    public Node getNext() {
        return this.next;
    }

    //获得当前结点的前驱结点
    public Node getPrior() {
        return this.prior;
    }

    //获得当前的数据域的值
    public T getElement() {
        return this.element;
    }

    //设置当前结点的后继指针域
    public void setNext(Node nextval) {
        this.next = nextval;
    }

    //设置当前结点的前驱指针域
    public void setPrior(Node priorval) {
        this.prior = priorval;
    }

    //设置当前结点的数据域
    public void setElement(T obj) {
        this.element = obj;
    }

    public String toString() {
        return this.element.toString();
    }
}