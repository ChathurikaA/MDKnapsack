package SimulatedAnnealingKnapsack;

/**
 * Class implemented to Wrap the exception into MKProblem Exception.
 */
public class MKPImplemetationException extends Exception {
    public MKPImplemetationException(Exception e) {
        super(e);
    }

    public MKPImplemetationException(String message, Exception e) {
        super(message, e);
    }
}
