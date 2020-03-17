import java.util.function.IntBinaryOperator;

public class p5{
    private static int[] scores = {10, 50, 3};

    public static int maxOrMin(IntBinaryOperator op){
        int result = scores[0];
        for(int score : scores)
        {
            result = op.applyAsInt(result, score);
        }
        return result;
    }

    public static void main(String[] args){
        int max = maxOrMin(
            (a, b) -> {
                if(a > b)
                {
                    return a;
                }
                else
                {
                    return b;
                }
            }
        );
        int min = maxOrMin(
            (a, b) -> {
                if(a < b) { return a; }
                else { return b; }
            }
        );

        System.out.println("Max: " + max);
        System.out.println("Min: " + min);
    }
}