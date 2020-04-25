package com.company;
import static java.lang.Math.*;

public class Main {
    interface Function {
        double calculate(double x);

        Function derivative();
    }

    static class ConstFunction implements Function {
        private final double value;

        public ConstFunction(double value) {
            this.value = value;
        }

        @Override
        public double calculate(double x) {
            return value;
        }

        @Override
        public Function derivative() {
            return ZERO;
        }

        public static final ConstFunction ZERO = new ConstFunction(0);
    }
    static class LinearFunction implements Function {
        public final double k;

        public LinearFunction(double k) {
            this.k = k;
        }

        @Override
        public double calculate(double x) {
            return k * x;
        }

        @Override
        public Function derivative() {
            return new ConstFunction(k);
        }
    }
    static class SinFunction implements Function {
        public final Function f;

        public SinFunction(Function f) {
            this.f = f;
        }

        @Override
        public double calculate(double x) {
            return sin(f.calculate(x));
        }

        @Override
        public Function derivative() {
            return new MultFunction(new CosFunction(f),f.derivative());
        }
    }
    static class CosFunction implements Function {
        public final Function f;

        public CosFunction(Function f) {
            this.f = f;
        }

        @Override
        public double calculate(double x) {
            return cos(f.calculate(x));
        }

        @Override
        public Function derivative() {
            return new MultFunction(new DiffFunction(new ConstFunction(0),new SinFunction(f)),f.derivative());
        }
    }
    static class TanFunction implements Function {
        public final Function f;

        public TanFunction(Function f) {
            this.f = f;
        }

        @Override
        public double calculate(double x) {
            return tan(f.calculate(x));
        }

        @Override
        public Function derivative() {
            return new FracFunction(new ConstFunction(1),new PowerFunction(2,new CosFunction(f)));
        }
    }
    static class CtanFunction implements Function {
        public final Function f;

        public CtanFunction(Function f) {
            this.f = f;
        }

        @Override
        public double calculate(double x) {
            return 1/tan(f.calculate(x));
        }

        @Override
        public Function derivative() {
            return new DiffFunction(new ConstFunction(0),new FracFunction(new ConstFunction(1),new PowerFunction(2,new SinFunction(f))));
        }
    }
    static class MultFunction implements Function {
        public final Function a,b;

        public MultFunction(Function a,Function b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public double calculate(double x) {
            return a.calculate(x)*b.calculate(x);
        }

        @Override
        public Function derivative() {
            return new SumFunction(new MultFunction(a,b.derivative()),new MultFunction(a.derivative(),b));
        }
    }
    static class FracFunction implements Function {
        public final Function a,b;

        public FracFunction(Function a,Function b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public double calculate(double x) {
            return a.calculate(x)/b.calculate(x);
        }

        @Override
        public Function derivative() {
            return (new MultFunction(a,new PowerFunction(-1,b))).derivative();
        }
    }
    static class PowerFunction implements Function {
        public final double r;
        public final Function f;

        public PowerFunction(double k, Function f) {
            this.r = k;
            this.f = f;
        }

        @Override
        public double calculate(double x) {
            return  pow(f.calculate(x),r);
        }

        @Override
        public Function derivative() {
            return new MultFunction(new ConstFunction(r),new MultFunction(f.derivative(),new PowerFunction(r-1,f)));
        }
    }
    static class SumFunction implements Function {
        private final Function a, b;

        public SumFunction(Function a, Function b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public double calculate(double x) {
            return a.calculate(x) + b.calculate(x);
        }

        @Override
        public Function derivative() {
            return new SumFunction(a.derivative(), b.derivative());
        }

    }
    static class DiffFunction implements Function {
        private final Function a, b;

        public DiffFunction(Function a, Function b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public double calculate(double x) {
            return a.calculate(x) - b.calculate(x);
        }

        @Override
        public Function derivative() {
            return new DiffFunction(a.derivative(), b.derivative());
        }

    }
    public static void main(String[] args) {
        double b = 1.5;
        double a = 15.5;
        double x = -2.8;
        final Function expression = new DiffFunction(new PowerFunction(1.0/2,new SumFunction(new PowerFunction(2,new LinearFunction(1)),new ConstFunction(b))), new MultFunction(new PowerFunction(2,new ConstFunction(b)),new FracFunction(new PowerFunction(3,new SinFunction(new SumFunction(new LinearFunction(1),new ConstFunction(a)))),new LinearFunction(1))));
        System.out.println("f(x) = " + expression.calculate(x));
        System.out.println("f'(x) = " + expression.derivative().calculate(x));
        final Function expression1 = new DiffFunction(new PowerFunction(2,new CosFunction(new PowerFunction(3,new LinearFunction(1)))),new MultFunction(new LinearFunction(1),new PowerFunction(-1.0/2,new SumFunction(new PowerFunction(2,new ConstFunction(a)),new PowerFunction(2,new ConstFunction(b))))));
        System.out.println("f(x) = " + expression1.calculate(x));
        System.out.println("f'(x) = " + expression1.derivative().calculate(x));
    }



}
