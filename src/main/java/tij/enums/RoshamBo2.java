package tij.enums;

import tij.enums.util.Enums;

import static tij.enums.Outcome.*;

public enum RoshamBo2 implements Competitor<RoshamBo2> {

    PAPER(DRAW,LOSE,WIN),//始终要记得，这里的参数；列表可不是形参，而是实参（此处为Outcome对象实例）
    SCISSORS(WIN,DRAW,LOSE),
    ROCK(LOSE,WIN,DRAW);

    private Outcome vPAPER, vSCISSORS, vROCK;

    RoshamBo2(Outcome paper, Outcome scissors,Outcome rock){
        this.vPAPER = paper;
        this.vSCISSORS = scissors;
        this.vROCK = rock;
    }

    @Override
    public Outcome compete(RoshamBo2 competitor) {
        switch (competitor){//在此时，a（即调用者）的类型已经被确定了，也就是说，a已经被初始化
            default:
            case PAPER:return vPAPER;
            case SCISSORS:return vSCISSORS;
            case ROCK:return vROCK;
        }
    }

    public static void main(String[] args) {
        RoshamBo.play(RoshamBo2.class, 20);
    }
}
enum Outcome{
    WIN, LOSE, DRAW
}
interface Competitor<T extends Competitor<T>>{
    Outcome compete(T competitor);
}
class RoshamBo{
    public static <T extends Competitor<T>>void match(T a, T b){
        System.out.println(a+" vs "+b+": "+a.compete(b));
    }
    public static <T extends Enum<T> & Competitor<T>> void play(Class<T> rsbClass,int size){
        for (int i = 0;i<size;i++){
            match(Enums.random(rsbClass),Enums.random(rsbClass));
        }
    }
}
