package com.github.hackerwin7.libjava.cases;

/**
 * @author : wenqi.jk
 * @since : 3/1/24, 15:42
 **/
public class Trian implements Shape {
    @Override
    public void draw() {
        System.out.println("Trian");
    }

    public static void main(String[] args) {
        System.out.println(Trian.class.isAssignableFrom(Shape.class));
        System.out.println(Shape.class.isAssignableFrom(Trian.class));
        System.out.println(Trian.class.isAssignableFrom(T3.class));
        System.out.println(T3.class.isAssignableFrom(Trian.class));
        System.out.println(Shape.class.isAssignableFrom(Shape.class));
        System.out.println(T3.class.isAssignableFrom(Shape.class));
        System.out.println(AbstractTrian.class.isAssignableFrom(Shape.class));
        System.out.println(T4.class.isAssignableFrom(Shape.class));
    }

    static class T3 extends Trian {
        @Override
        public void draw() {
            System.out.println("T3");
        }
    }

    static abstract  class AbstractTrian implements Shape {

    }

    static class T4 extends AbstractTrian {
        @Override
        public void draw() {
            System.out.println("T4");
        }
    }
}
