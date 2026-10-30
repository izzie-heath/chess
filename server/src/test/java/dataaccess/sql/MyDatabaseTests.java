package dataaccess.sql;
import dataaccess.DataAccessException;
import dataaccess.sql.SqlDataAccess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MyDatabaseTests {
    private SqlDataAccess data;

    @Test
    @DisplayName("Clear throws no errors")
    public void clearWorks() throws DataAccessException {
        data = new SqlDataAccess();
        data.clear();

        assertDoesNotThrow(() -> data.clear());
    }
}
