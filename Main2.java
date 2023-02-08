import java.util.Arrays;

public class Main2 {


    static int[] prime_finder_in_java(int amount){
        amount =  Math.min(amount, 250000);
        int[] primes = new int[amount];
        int found = 0;
        boolean isFound = false;
        int number = 2;

//        System.out.println("found: " + found + "  amount: " + amount);

        while(found <= amount-1){
//            System.out.println("found: " + found + "  amount: " + amount);
            isFound=false;

            for(int i=0; i<found; i++){
                if(number % primes[i] == 0){
                    isFound=true;
                    break;
                }
            }
            if(!isFound){
                primes[found++] = number;

            }
            number++;

        }
        return primes;

    }

    public static void main(String args[]){
//        long startTime = System.currentTimeMillis();
////        prime_finder_in_java(500000);
//        Arrays.stream(prime_finder_in_java(250000)).forEach(s->System.out.print(s + "  "));
////        System.out.println( prime_finder_in_java(10));
//        System.out.println("\n" +(System.currentTimeMillis()-startTime)/1 );

        System.out.println(Piece.BLACK_KING.value);
        System.out.println(Piece.BLACK_KING == Piece.WHITE_KING);
    }
}
