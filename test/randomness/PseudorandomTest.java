package randomness;

import org.junit.jupiter.api.Test;

class PseudorandomTest {

    @Test
    void testNextIntPositiveBound() {
        int bound = 100;
        int hypotheticalOccurAverage = 10;
        int maxOccurrencePerNumber = 2 * hypotheticalOccurAverage;
        int callTotal = hypotheticalOccurAverage * bound;
        int[] occurrences = new int[bound];
        for (int i = 0; i < callTotal; i++) {
            int n = Pseudorandom.nextInt(bound);
            String boundMsg = "Pseudorandom number " + n
                    + " should be at least 0 but not equal to nor more than "
                    + bound;
            assert n >= 0 && n < bound : boundMsg;
            int occurCount = ++occurrences[n];
            String occurMsg = "Pseudorandom number " + n + " occurs "
                    + occurCount + " times, should not occur more than "
                    + maxOccurrencePerNumber + " times in " + callTotal
                    + " calls";
            assert occurCount <= maxOccurrencePerNumber : occurMsg;
        }
    }

    @Test
    void testFlipCoin() {
        final int tries = 1000;
        final int minimumExpectation = 3 * tries / 10;
        final int maximumExpectation = 7 * tries / 10;
        int headCount = 0;
        int tailCount = 0;
        for (int i = 0; i < tries; i++) {
            CoinSide side = Pseudorandom.flipCoin();
            if (side.equals(CoinSide.HEADS)) {
                headCount++;
            } else {
                tailCount++;
            }
        }
        String msg = "Flipped heads " + headCount + " times, tails " + tailCount
                + " times, should be at least " + minimumExpectation
                + " times, at most " + maximumExpectation + " times";
        assert headCount >= minimumExpectation : msg;
        assert headCount <= maximumExpectation : msg;
        assert tailCount >= minimumExpectation : msg;
        assert tailCount <= maximumExpectation : msg;
    }

}
