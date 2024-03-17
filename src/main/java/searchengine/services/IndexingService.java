package searchengine.services;

import java.io.IOException;

public interface IndexingService {
    void startIndexing() throws IOException;
    void stopIndexing();
}
