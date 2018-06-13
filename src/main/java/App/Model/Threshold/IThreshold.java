package App.Model.Threshold;
import org.apache.lucene.search.ScoreDoc;

public interface IThreshold {
    /**
     * Filter the results based on the defined threshold
     * @param hits Set of unfiltered results
     * @return Filtered results
     */
    ScoreDoc[] getTopResults(ScoreDoc[] hits);

    /**
     * Get the name for this threshold (for output)
     * @return Pretty string to represent the threshold
     */
    String getName();

    /**
     * Get the defined parameter for this threshold
     * @return pParameter that was set for this threshold
     */
    Double getValue();
}