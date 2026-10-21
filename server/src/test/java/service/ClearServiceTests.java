package service;

import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.ClearHandler;

public class ClearServiceTests {

    @Test
    @DisplayName("Clear Runs Successfully")
    public void clearWorks(){
        DataAccess dataAccess = new MemoryDataAccess();
        ClearService clearService = new ClearService(dataAccess);
        Assertions.assertDoesNotThrow(clearService::clear);
    }
}
